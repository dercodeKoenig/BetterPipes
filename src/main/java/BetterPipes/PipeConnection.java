package BetterPipes;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class PipeConnection implements IFluidHandler {
    public boolean isEnabled;

    public int lastInputFromInside;
    public int lastInputFromOutside;
    public boolean getsInputFromInside;
    public boolean getsInputFromOutside;
    public boolean lastInputWasFromAnotherPipe;

    public int lastOutputToOutside;
    public int lastOutputToInside;
    public boolean outputsToInside;
    public boolean outputsToOutside;

    simpleBlockEntityTank tank;
    int lastFill;
    IFluidHandler neighborFluidHandler;

    public PipeConnection(EntityPipe parent) {
        tank = new simpleBlockEntityTank(500, parent);
    }

    void update() {
        if (lastInputFromOutside < 20)
            lastInputFromOutside++;
        else if (getsInputFromOutside)
            getsInputFromOutside = false;

        if (lastInputFromInside < 20)
            lastInputFromInside++;
        else if (getsInputFromInside)
            getsInputFromInside = false;

        if (lastOutputToInside < 20)
            lastOutputToInside++;
        else if (outputsToInside)
            outputsToInside = false;

        if (lastOutputToOutside < 20)
            lastOutputToOutside++;
        else if (outputsToOutside)
            outputsToOutside = false;
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