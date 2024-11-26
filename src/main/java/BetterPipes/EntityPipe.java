package BetterPipes;

import ARLib.network.INetworkTagReceiver;
import ARLib.network.PacketBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

import static BetterPipes.Registry.ENTITY_PIPE;

public class EntityPipe extends BlockEntity implements INetworkTagReceiver {
    private static final List<EntityPipe> ACTIVE_PIPES = new ArrayList<>();

    public Map<Direction, PipeConnection> connections = new HashMap<>();
    FluidTank mainTank = new FluidTank(1000);
    FluidStack last_tankFluid = FluidStack.EMPTY;
    int lastFill;
    int ticksWithFluidInTank = 0;
    int maxOutputRate = 10;
    int fillRequiredForFullOutputRate = 400;
    int connectionMaxOutputRate = 10;
    int connectionFillRequiredForFullOutputRate = 200;

    public EntityPipe(BlockPos pos, BlockState blockState) {
        super(ENTITY_PIPE.get(), pos, blockState);
        connections.put(Direction.UP, new PipeConnection(this));
        connections.put(Direction.DOWN, new PipeConnection(this));
        connections.put(Direction.EAST, new PipeConnection(this));
        connections.put(Direction.WEST, new PipeConnection(this));
        connections.put(Direction.SOUTH, new PipeConnection(this));
        connections.put(Direction.NORTH, new PipeConnection(this));
    }

    public IFluidHandler getFluidHandler(Direction side) {
        return connections.get(side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        BlockState state = level.getBlockState(getBlockPos());

        for (Direction direction : Direction.values()) {
            BlockPos neighbor = getBlockPos().offset(direction.getStepX(), direction.getStepY(), direction.getStepZ());
            state.updateShape(direction, level.getBlockState(neighbor), level, getBlockPos(), neighbor);
        }

        if (!level.isClientSide) {
            ACTIVE_PIPES.add(this);
        }
        if(level.isClientSide){
            UUID from = Minecraft.getInstance().player.getUUID();
            CompoundTag tag = new CompoundTag();
            tag.putUUID("client_onload", from);
            PacketDistributor.sendToServer(PacketBlockEntity.getBlockEntityPacket(this,tag));
        }
    }

    @Override
    public void setRemoved() {
        if (!level.isClientSide) {
            ACTIVE_PIPES.remove(this);
        }
        super.setRemoved();
    }

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Pre event) {
        if (!event.getLevel().isClientSide) {
            for (EntityPipe i : EntityPipe.ACTIVE_PIPES) {
                i.tick_start();
            }
            for (EntityPipe i : EntityPipe.ACTIVE_PIPES) {
                i.tick_update_tanks();
            }
            for (EntityPipe i : EntityPipe.ACTIVE_PIPES) {
                i.tick_complete();
            }
        }
    }

    public void tick_start() {
        lastFill = mainTank.getFluidAmount();
        for (Direction direction : Direction.values()) {
            connections.get(direction).lastFill = connections.get(direction).tank.getFluidAmount();
        }
    }

    public void tick_update_tanks() {
        for (Direction direction : Direction.allShuffled(level.random)) {
            PipeConnection conn = connections.get(direction);
            if (conn.isEnabled) {
                if (conn.lastFill > 0) {
                    if (!conn.getsInputFromInside) {
                        double transferRateMultiplier = (double) conn.lastFill / connectionFillRequiredForFullOutputRate;
                        int toTransfer = (int) (connectionMaxOutputRate * transferRateMultiplier);
                        if (toTransfer > connectionMaxOutputRate && conn.lastInputWasFromAnotherPipe)
                            toTransfer = connectionMaxOutputRate + 1;
                        if (toTransfer > connectionMaxOutputRate && !conn.lastInputWasFromAnotherPipe)
                            toTransfer = connectionMaxOutputRate;
                        if (toTransfer == 0 && conn.ticksWithFluidInTank>=60)
                            toTransfer = 1;

                        FluidStack drained = conn.tank.drain(toTransfer, IFluidHandler.FluidAction.SIMULATE);
                        int filled = mainTank.fill(drained, IFluidHandler.FluidAction.SIMULATE);
                        toTransfer = Math.min(filled, toTransfer);
                        //drain into main tank
                        mainTank.fill(conn.drain(toTransfer, IFluidHandler.FluidAction.EXECUTE, true), IFluidHandler.FluidAction.EXECUTE);
                    }

                    if (!conn.getsInputFromOutside) {
                        if (conn.neighborFluidHandler != null) {
                            double transferRateMultiplier = (double) conn.lastFill / connectionFillRequiredForFullOutputRate;
                            int toTransfer = (int) (connectionMaxOutputRate * transferRateMultiplier);
                            if (toTransfer > connectionMaxOutputRate)
                                toTransfer = connectionMaxOutputRate + 1;
                            if (toTransfer == 0 && conn.ticksWithFluidInTank>=60)
                                toTransfer = 1;

                            FluidStack drained = conn.tank.drain(toTransfer, IFluidHandler.FluidAction.SIMULATE);
                            int filled = conn.neighborFluidHandler.fill(drained, IFluidHandler.FluidAction.SIMULATE);
                            toTransfer = Math.min(filled, toTransfer);
                            //drain to outside tank
                            if (conn.neighborFluidHandler instanceof PipeConnection pipeconn)
                                pipeconn.fillFromOtherPipe(conn.drain(toTransfer, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                            else
                                conn.neighborFluidHandler.fill(conn.drain(toTransfer, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
                if (lastFill > 0) {
                    if (!conn.outputsToInside) {
                        double transferRateMultiplier = (double) lastFill / fillRequiredForFullOutputRate;
                        int toTransfer = (int) (maxOutputRate * transferRateMultiplier);
                        if (toTransfer > maxOutputRate)
                            toTransfer = maxOutputRate + 1;
                        if (toTransfer == 0 && ticksWithFluidInTank>=60)
                            toTransfer = 1;

                        FluidStack drained = mainTank.drain(toTransfer, IFluidHandler.FluidAction.SIMULATE);
                        int filled = conn.tank.fill(drained, IFluidHandler.FluidAction.SIMULATE);
                        toTransfer = Math.min(filled, toTransfer);
                        //drain main tank into neighbor
                        conn.fill(mainTank.drain(toTransfer, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE, true);
                    }
                }
            }
        }
    }

    public void tick_complete() {
        //System.out.println(mainTank.getFluidAmount());
        CompoundTag updateTag = new CompoundTag();
        boolean needsSendUpdate = false;

        // Check if the tank fluid stack has changed
        if (!FluidStack.isSameFluidSameComponents(last_tankFluid, mainTank.getFluid()) || last_tankFluid.getAmount() != mainTank.getFluidAmount()) {
            needsSendUpdate = true;
            last_tankFluid = mainTank.getFluid().copy();
            CompoundTag tag = new CompoundTag();
            mainTank.writeToNBT(level.registryAccess(), tag);
            updateTag.put("mainTank", tag);
        }
        if(!mainTank.isEmpty() && ticksWithFluidInTank < 60)
            ticksWithFluidInTank++;
        else if (ticksWithFluidInTank != 0)
            ticksWithFluidInTank=0;

        for (Direction direction : Direction.allShuffled(level.random)) {
            PipeConnection conn = connections.get(direction);
            if (conn.isEnabled) {
                //System.out.println(conn.tank.getFluidAmount()+":"+direction);
                conn.update();
                if (conn.needsSync()) {
                    CompoundTag tag = conn.getUpdateTag();
                    updateTag.put(direction.getName(), tag);
                    needsSendUpdate = true;
                }
            }
        }
        if (needsSendUpdate) {
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(getBlockPos()), PacketBlockEntity.getBlockEntityPacket(this, updateTag));
        }
    }

    @Override
    public void readServer(CompoundTag compoundTag) {
        if (compoundTag.contains("client_onload")) {
            UUID from = compoundTag.getUUID("client_onload");
            ServerPlayer playerFrom = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(from);
            CompoundTag updateTag = new CompoundTag();
            CompoundTag tankTag = new CompoundTag();
            mainTank.writeToNBT(level.registryAccess(), tankTag);
            updateTag.put("mainTank", tankTag);
            for (Direction direction : Direction.allShuffled(level.random)) {
                PipeConnection conn = connections.get(direction);
                if (conn.isEnabled) {
                    CompoundTag tag = conn.getUpdateTag();
                    updateTag.put(direction.getName(), tag);
                }
            }
            PacketDistributor.sendToPlayer(playerFrom,PacketBlockEntity.getBlockEntityPacket(this,updateTag));
        }
    }

    @Override
    public void readClient(CompoundTag compoundTag) {
        if (compoundTag.contains("mainTank")) {
            mainTank.readFromNBT(level.registryAccess(), compoundTag.getCompound("mainTank"));
        }
        for (Direction direction : Direction.values()) {
            if (compoundTag.contains(direction.getName())) {
                connections.get(direction).handleUpdateTag(compoundTag.getCompound(direction.getName()));
            }
        }
    }
}