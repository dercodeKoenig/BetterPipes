package BetterPipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.HashMap;
import java.util.Map;

import static BetterPipes.Registry.ENTITY_PIPE;

public class EntityPipe extends BlockEntity {

    public Map<Direction, PipeConnection> connections = new HashMap<>();
    FluidTank mainTank = new FluidTank(1000);

    public EntityPipe(BlockPos pos, BlockState blockState) {
        super(ENTITY_PIPE.get(), pos, blockState);
        connections.put(Direction.UP, new PipeConnection());
        connections.put(Direction.DOWN, new PipeConnection());
        connections.put(Direction.EAST, new PipeConnection());
        connections.put(Direction.WEST, new PipeConnection());
        connections.put(Direction.SOUTH, new PipeConnection());
        connections.put(Direction.NORTH, new PipeConnection());
    }
    public IFluidHandler getFluidHandler(Direction side){
     return   connections.get(side).tank;
    }
}
