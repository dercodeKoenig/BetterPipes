package BetterPipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import static BetterPipes.Registry.ENTITY_PIPE;

public class BlockPipe extends Block implements EntityBlock {

    public BlockPipe(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ENTITY_PIPE.get().create(pos, state);
    }


    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        // Custom logic here, e.g., initializing block-specific data
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        IFluidHandler fluidHandler = null;
        BlockEntity e = level.getBlockEntity(neighborPos);
        if(e != null){
            fluidHandler= e.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, neighborState, e, direction);
        }
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof EntityPipe pipe) {
            if (fluidHandler != null) {
                pipe.connections.get(direction).isEnabled = true;
            } else {
                pipe.connections.get(direction).isEnabled = false;
            }
        }
        return state;
    }
}