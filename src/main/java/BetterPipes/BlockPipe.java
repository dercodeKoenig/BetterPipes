package BetterPipes;

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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BetterPipes.Registry.*;

public class BlockPipe extends Block implements EntityBlock {
    public static Map<Direction, BooleanProperty> connections = new HashMap<>();
    public static Map<Direction, BooleanProperty> connections_isExtraction = new HashMap<>();
    public static BooleanProperty pipe_is_extraction = BooleanProperty.create("main_isextraction");
    public static BooleanProperty pipe_is_extraction_active = BooleanProperty.create("main_isextractionactive");

    static{
        for(Direction i : Direction.values()){
            connections.put(i,BooleanProperty.create(i.getName()));
            connections_isExtraction.put(i,BooleanProperty.create(i.getName()+"_extraction"));
        }
    }


    public BlockPipe(Properties properties) {
        super(properties);
        BlockState state = this.stateDefinition.any();
        for(Direction i : Direction.values()){
            state = state.setValue(connections.get(i),false);
            state = state.setValue(connections_isExtraction.get(i),false);
        }
        state = state.setValue(pipe_is_extraction, false);
        state = state.setValue(pipe_is_extraction_active, false);
        this.registerDefaultState(state);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        for(Direction i : Direction.values()){
            builder.add(connections.get(i));
            builder.add(connections_isExtraction.get(i));
        }
        builder.add(pipe_is_extraction);
        builder.add(pipe_is_extraction_active);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ENTITY_PIPE.get().create(pos, state);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(this, 1));
        return drops;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player.getMainHandItem().isEmpty()) {
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

        BlockEntity tile = level.getBlockEntity(pos);
        if (!(tile instanceof EntityPipe pipe)) return state;

        IFluidHandler fluidHandler = pipe.connections.get(direction).neighborFluidHandler();

        if (fluidHandler != null) {
            state = state.setValue(connections.get(direction), true);
        } else {
            state = state.setValue(connections.get(direction), false);
            state = state.setValue(connections_isExtraction.get(direction), false);

            pipe.connections.get(direction).tank.setFluid(FluidStack.EMPTY);
            boolean hasAnyExtraction = false;
            for (Direction i : Direction.values()) {
                if (state.getValue(connections.get(i)) && !(pipe.connections.get(i).neighborFluidHandler() instanceof PipeConnection))
                    hasAnyExtraction = true;
            }
            if (!hasAnyExtraction) {
                state = pipe.setExtractionMode(state, false);
            }
        }

        return state;
    }
    //@Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return EntityPipe::tick;
    }
}