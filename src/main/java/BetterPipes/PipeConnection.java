package BetterPipes;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class PipeConnection {
    public boolean isEnabled;
    public int lastInputFromInside;
    public int lastInputFromOutside;
    public boolean getsInputFromInside;
    public boolean getsInputFromOutside;
    public boolean OutputsToInside;
    public boolean OutputsToOutside;
    public FluidStack myFluid;
    FluidTank tank = new FluidTank(1000);
}