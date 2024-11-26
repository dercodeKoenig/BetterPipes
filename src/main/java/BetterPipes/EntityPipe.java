package BetterPipes;

import ARLib.network.INetworkTagReceiver;
import ARLib.network.PacketBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.*;

import static BetterPipes.Registry.ENTITY_PIPE;

public class EntityPipe extends BlockEntity implements INetworkTagReceiver {
    private static final List<EntityPipe> ACTIVE_PIPES = new ArrayList<>();

    public static int MAX_OUTPUT_RATE = 5;
    public static int REQUIRED_FILL_FOR_MAX_OUTPUT = 100;
    public static int MAIN_CAPACITY = 200;

    public static int CONNECTION_MAX_OUTPUT_RATE = 5;
    public static int CONNECTION_REQUIRED_FILL_FOR_MAX_OUTPUT = 50;
    public static int CONNECTION_CAPACITY = 100;

    public static int STATE_UPDATE_TICKS = 60;
    public static int FORCE_OUTPUT_AFTER_TICKS = 20;

    public Map<Direction, PipeConnection> connections = new HashMap<>();
    FluidTank mainTank = new simpleBlockEntityTank(MAIN_CAPACITY, this);
    FluidStack last_tankFluid = FluidStack.EMPTY;
    int lastFill;
    int ticksWithFluidInTank = 0;


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
        if (level.isClientSide) {
            UUID from = Minecraft.getInstance().player.getUUID();
            CompoundTag tag = new CompoundTag();
            tag.putUUID("client_onload", from);
            PacketDistributor.sendToServer(PacketBlockEntity.getBlockEntityPacket(this, tag));
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
    public static void onServerTick(ServerTickEvent.Pre event) {
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
                        double transferRateMultiplier = (double) conn.lastFill / CONNECTION_REQUIRED_FILL_FOR_MAX_OUTPUT;
                        int toTransfer = (int) (CONNECTION_MAX_OUTPUT_RATE * transferRateMultiplier);
                        if (toTransfer > CONNECTION_MAX_OUTPUT_RATE && conn.lastInputWasFromAnotherPipe)
                            toTransfer = CONNECTION_MAX_OUTPUT_RATE + 1;
                        if (toTransfer > CONNECTION_MAX_OUTPUT_RATE && !conn.lastInputWasFromAnotherPipe)
                            toTransfer = CONNECTION_MAX_OUTPUT_RATE;
                        if (toTransfer == 0 && conn.ticksWithFluidInTank >= FORCE_OUTPUT_AFTER_TICKS)
                            toTransfer = 1;

                        FluidStack drained = conn.tank.drain(toTransfer, IFluidHandler.FluidAction.SIMULATE);
                        int filled = mainTank.fill(drained, IFluidHandler.FluidAction.SIMULATE);
                        toTransfer = Math.min(filled, toTransfer);
                        //drain into main tank
                        mainTank.fill(conn.drain(toTransfer, IFluidHandler.FluidAction.EXECUTE, true), IFluidHandler.FluidAction.EXECUTE);
                    }

                    if (!conn.getsInputFromOutside) {
                        if (conn.neighborFluidHandler != null) {
                            double transferRateMultiplier = (double) conn.lastFill / CONNECTION_REQUIRED_FILL_FOR_MAX_OUTPUT;
                            int toTransfer = (int) (CONNECTION_MAX_OUTPUT_RATE * transferRateMultiplier);
                            if (toTransfer > CONNECTION_MAX_OUTPUT_RATE)
                                toTransfer = CONNECTION_MAX_OUTPUT_RATE + 1;
                            if (toTransfer == 0 && conn.ticksWithFluidInTank >= FORCE_OUTPUT_AFTER_TICKS)
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
                        double transferRateMultiplier = (double) lastFill / REQUIRED_FILL_FOR_MAX_OUTPUT;
                        int toTransfer = (int) (MAX_OUTPUT_RATE * transferRateMultiplier);
                        if (toTransfer > MAX_OUTPUT_RATE)
                            toTransfer = MAX_OUTPUT_RATE + 1;
                        if (toTransfer == 0 && ticksWithFluidInTank >= FORCE_OUTPUT_AFTER_TICKS)
                            toTransfer = 1;

                        FluidStack drained = mainTank.drain(toTransfer, IFluidHandler.FluidAction.SIMULATE);
                        int filled = conn.tank.fill(drained, IFluidHandler.FluidAction.SIMULATE);
                        toTransfer = Math.min(filled, toTransfer);
                        //drain main tank into connection
                        conn.fill(mainTank.drain(toTransfer, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE, true);
                    }
                }
            }
        }
    }

    public void tick_complete() {
        if (!mainTank.isEmpty() && ticksWithFluidInTank < FORCE_OUTPUT_AFTER_TICKS + 1)
            ticksWithFluidInTank++;
        else if (mainTank.isEmpty()) {
            ticksWithFluidInTank = 0;
        }
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
        for (Direction direction : Direction.allShuffled(level.random)) {
            PipeConnection conn = connections.get(direction);
            if (conn.isEnabled) {
                //System.out.println(conn.tank.getFluidAmount()+":"+direction);
                conn.update();
                if (conn.needsSync()) {
                    updateTag.put(direction.getName(), conn.getUpdateTag(level.registryAccess()));
                    needsSendUpdate = true;
                }
            }
        }
        if (needsSendUpdate) {
            updateTag.putLong("time", System.currentTimeMillis());
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
                    CompoundTag tag = conn.getUpdateTag(level.registryAccess());
                    updateTag.put(direction.getName(), tag);
                }
            }
            updateTag.putLong("time", System.currentTimeMillis());
            PacketDistributor.sendToPlayer(playerFrom, PacketBlockEntity.getBlockEntityPacket(this, updateTag));
        }
    }

    long lastUpdate = 0;

    @Override
    public void readClient(CompoundTag compoundTag) {
        if (compoundTag.contains(("time"))) {
            long updateTime = compoundTag.getLong("time");
            if (updateTime >= lastUpdate) {
                lastUpdate = updateTime;

                if (compoundTag.contains("mainTank")) {
                    mainTank.readFromNBT(level.registryAccess(), compoundTag.getCompound("mainTank"));
                }
                for (Direction direction : Direction.values()) {
                    if (compoundTag.contains(direction.getName())) {
                        connections.get(direction).handleUpdateTag(compoundTag.getCompound(direction.getName()),level.registryAccess());
                    }
                }
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        mainTank.readFromNBT(registries, tag.getCompound("mainTank"));

        for (Direction direction : Direction.values()) {
            PipeConnection conn = connections.get(direction);
            conn.handleUpdateTag(tag.getCompound(direction.getName()),registries);
        }

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        CompoundTag tankTag = new CompoundTag();
        mainTank.writeToNBT(registries, tankTag);
        tag.put("mainTank", tankTag);

        for (Direction direction : Direction.values()) {
            PipeConnection conn = connections.get(direction);
            tag.put(direction.getName(), conn.getUpdateTag(registries));
        }
    }
}