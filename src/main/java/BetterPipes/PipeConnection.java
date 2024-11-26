package BetterPipes;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class PipeConnection implements IFluidHandler {
    public boolean isEnabled;
    public int lastInputFromInside;
    public int lastInputFromOutside;
    public boolean getsInputFromInside;
    public boolean getsInputFromOutside;
    public boolean OutputsToInside;
    public boolean OutputsToOutside;
    public FluidStack myFluid;

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}