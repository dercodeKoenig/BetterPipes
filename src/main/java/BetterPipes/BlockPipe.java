package BetterPipes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static BetterPipes.Registry.*;

public class BlockPipe extends Block implements EntityBlock {

    public BlockPipe(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ENTITY_PIPE.get().create(pos, state);
    }
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(PIPE.get(),1));
        return drops;
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide && player.getMainHandItem().isEmpty()) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof EntityPipe pipe) {
                if (player.isShiftKeyDown()) {
                    pipe.toggleExtractionMode();
                } else {
                    pipe.toggleExtractionActive();
                }
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        updateFromNeighbourShapes(state, level, pos);
    }

    protected int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 2;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.create(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
    }


    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        IFluidHandler fluidHandler = null;

            BlockEntity e = level.getBlockEntity(neighborPos);
            if (e != null) {
                fluidHandler = e.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, neighborState, e, direction.getOpposite());
            }
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof EntityPipe pipe) {
                pipe.connections.get(direction).neighborFluidHandler = fluidHandler;
                if (fluidHandler != null) {
                    pipe.connections.get(direction).isEnabled = true;
                } else {
                    pipe.connections.get(direction).isEnabled = false;
                    if (!level.isClientSide()) {
                        pipe.connections.get(direction).tank.setFluid(FluidStack.EMPTY);
                        pipe.connections.get(direction).isExtraction = false;
                        boolean hasAnyExtraction = false;
                        for (Direction i : Direction.values()) {
                            if (pipe.connections.get(i).isExtraction)
                                hasAnyExtraction = true;
                        }
                        if (!hasAnyExtraction) {
                            pipe.setExtractionMode(false);
                        }
                    }
                }
            }
        return state;
    }
    //@Override
    //public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    //return EntityPipe::tick;
    //}

}