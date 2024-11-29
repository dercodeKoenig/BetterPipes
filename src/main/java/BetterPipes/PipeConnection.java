package BetterPipes;

import BetterPipes.networkPackets.PacketFluidAmountUpdate;
import BetterPipes.networkPackets.PacketFluidUpdate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import static BetterPipes.EntityPipe.*;
import static BetterPipes.RenderPipe.makeFluidRenderType;

public class PipeConnection implements IFluidHandler {
    Direction myDirection;

    public boolean isEnabled;
    public boolean isExtraction = false;

    public int lastInputFromInside;
    public int lastInputFromOutside;
    public boolean getsInputFromInside;
    public boolean getsInputFromOutside;
    public boolean lastInputWasFromAnotherPipe;

    public int lastOutputToOutside;
    public int lastOutputToInside;
    public boolean outputsToInside;
    public boolean outputsToOutside;

    public int ticksWithFluidInTank = 0;

    simpleBlockEntityTank tank;
    int lastFill;
    IFluidHandler neighborFluidHandler;


    fluidRenderData renderData;
    EntityPipe parent;


    public PipeConnection(EntityPipe parent, Direction myDirection) {
        this.myDirection = myDirection;
        tank = new simpleBlockEntityTank(CONNECTION_CAPACITY, parent);
        this.parent = parent;
        if (FMLEnvironment.dist == Dist.CLIENT) {
            renderData = makeFluidRenderType(Fluids.WATER, parent.getBlockPos().toString());
        }
    }

    boolean last_getsInputFromInside;
    boolean last_getsInputFromOutside;
    boolean last_outputsToInside;
    boolean last_outputsToOutside;
    FluidStack last_tankFluid = FluidStack.EMPTY;

    public boolean needsSync() {
        boolean needsUpdate = false;

        // Check if the input from inside state has changed
        if (last_getsInputFromInside != getsInputFromInside) {
            needsUpdate = true;
            last_getsInputFromInside = getsInputFromInside;
        }

        // Check if the input from outside state has changed
        if (last_getsInputFromOutside != getsInputFromOutside) {
            needsUpdate = true;
            last_getsInputFromOutside = getsInputFromOutside;
        }

        // Check if the output to inside state has changed
        if (last_outputsToInside != outputsToInside) {
            needsUpdate = true;
            last_outputsToInside = outputsToInside;
        }

        // Check if the output to outside state has changed
        if (last_outputsToOutside != outputsToOutside) {
            needsUpdate = true;
            last_outputsToOutside = outputsToOutside;
        }
        return needsUpdate;
    }
    public void saveAdditional(HolderLookup.Provider registries,CompoundTag tag){
        CompoundTag myTag = getUpdateTag(registries);
        tank.writeToNBT(registries, myTag);
        tag.put(myDirection.getName(), myTag);
    }
    public void loadAdditional(HolderLookup.Provider registries,CompoundTag tag){
        CompoundTag myTag = tag.getCompound(myDirection.getName());
        tank.readFromNBT(registries, myTag);
        handleUpdateTag(myTag,registries);
    }
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("getsInputFromInside", getsInputFromInside);
        tag.putBoolean("getsInputFromOutside", getsInputFromOutside);
        tag.putBoolean("outputsToInside", outputsToInside);
        tag.putBoolean("outputsToOutside", outputsToOutside);
        tag.putBoolean("isExtraction", isExtraction);
        tag.putBoolean("isEnabled", isEnabled);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        getsInputFromInside = tag.getBoolean("getsInputFromInside");
        getsInputFromOutside = tag.getBoolean("getsInputFromOutside");
        outputsToInside = tag.getBoolean("outputsToInside");
        outputsToOutside = tag.getBoolean("outputsToOutside");
        isExtraction = tag.getBoolean("isExtraction");
        isEnabled = tag.getBoolean("isEnabled");
    }

    void update() {
        if (lastInputFromOutside < STATE_UPDATE_TICKS + 1)
            lastInputFromOutside++;
        else if (getsInputFromOutside)
            getsInputFromOutside = false;

        if (lastInputFromInside < STATE_UPDATE_TICKS + 1)
            lastInputFromInside++;
        else if (getsInputFromInside)
            getsInputFromInside = false;

        if (lastOutputToInside < STATE_UPDATE_TICKS + 1)
            lastOutputToInside++;
        else if (outputsToInside)
            outputsToInside = false;

        if (lastOutputToOutside < STATE_UPDATE_TICKS + 1)
            lastOutputToOutside++;
        else if (outputsToOutside)
            outputsToOutside = false;

        if (!tank.isEmpty() && ticksWithFluidInTank < FORCE_OUTPUT_AFTER_TICKS + 1)
            ticksWithFluidInTank++;
        else if (tank.isEmpty()) {
            ticksWithFluidInTank = 0;
        }

        // Check if the tank fluid stack has changed
        // this has it's own packet now for efficiency
        // to not always send the large nbt
        if (!FluidStack.isSameFluidSameComponents(last_tankFluid, tank.getFluid())) {
            if(!tank.getFluid().isEmpty())
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) parent.getLevel(), new ChunkPos(parent.getBlockPos()), PacketFluidUpdate.getPacketFluidUpdate(parent.getBlockPos(),myDirection,tank.getFluid().getFluid()));
        }
        if(last_tankFluid.getAmount() != tank.getFluidAmount()){
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) parent.getLevel(), new ChunkPos(parent.getBlockPos()), PacketFluidAmountUpdate.getPacketFluidUpdate(parent.getBlockPos(),myDirection,tank.getFluidAmount()));
        }
        last_tankFluid = tank.getFluid().copy(); // Update the last known tank fluid

    }
    public void sendInitialTankUpdates(ServerPlayer player){
        PacketDistributor.sendToPlayer(player, PacketFluidAmountUpdate.getPacketFluidUpdate(parent.getBlockPos(),myDirection,tank.getFluidAmount()));
        if(!tank.getFluid().isEmpty())
            PacketDistributor.sendToPlayer(player, PacketFluidUpdate.getPacketFluidUpdate(parent.getBlockPos(),myDirection,tank.getFluid().getFluid()));
    }

    long lastFluidInTankUpdate;
    public void setFluidInTank(Fluid f, long time){
        if(time > lastFluidInTankUpdate) {
            lastFluidInTankUpdate = time;
            tank.setFluid(new FluidStack(f, tank.getFluidAmount()));
            renderData = makeFluidRenderType(tank.getFluid().getFluid(), parent.getBlockPos().toString());
        }
    }

    long lastFluidAmountUpdate;
    public void setFluidAmountInTank(int amount, long time) {
        if (time > lastFluidAmountUpdate) {
            lastFluidAmountUpdate = time;
            Fluid myFluid = tank.getFluid().getFluid();
            if (myFluid == Fluids.EMPTY && amount > 0) myFluid = Fluids.WATER;
            if(amount <= 0) myFluid = Fluids.EMPTY;
            tank.setFluid(new FluidStack(myFluid, amount));
        }
    }

    @Override
    public int getTanks() {
        return this.tank.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.tank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return this.tank.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        lastInputWasFromAnotherPipe = false;
        return fill(resource, action, false);
    }

    public int fillFromOtherPipe(FluidStack resource, FluidAction action) {
        lastInputWasFromAnotherPipe = true;
        return fill(resource, action, false);
    }

    public int fill(FluidStack resource, FluidAction action, boolean wasFromMaster) {
        int filled = this.tank.fill(resource, action);
        if (filled > 0 && action == FluidAction.EXECUTE) {
            if (wasFromMaster) {
                lastInputFromInside = 0;
                getsInputFromInside = true;
            } else {
                lastInputFromOutside = 0;
                getsInputFromOutside = true;
            }
        }
        return filled;
    }


    void drainUpdate(boolean wasFromMaster) {
        if (wasFromMaster) {
            outputsToInside = true;
            lastOutputToInside = 0;
        } else {
            outputsToOutside = true;
            lastOutputToOutside = 0;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource, action, false);
    }

    public FluidStack drain(FluidStack resource, FluidAction action, boolean wasFromMaster) {
        FluidStack drained = this.tank.drain(resource, action);
        if (!drained.isEmpty() && action == FluidAction.EXECUTE) {
            drainUpdate(wasFromMaster);
        }
        return drained;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return drain(maxDrain, action, false);
    }

    public FluidStack drain(int maxDrain, FluidAction action, boolean wasFromMaster) {
        FluidStack drained = this.tank.drain(maxDrain, action);
        if (!drained.isEmpty() && action == FluidAction.EXECUTE) {
            drainUpdate(wasFromMaster);
        }
        return drained;
    }
}