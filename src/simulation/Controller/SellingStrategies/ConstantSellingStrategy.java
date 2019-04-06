package simulation.Controller.SellingStrategies;

public class ConstantSellingStrategy implements ISellingStrategy {
    public static final int SELLING_INTERVAL = 1000;
    private int tickCount;

    @Override
    public boolean sell(double speed) {
        tickCount++;
        if (tickCount >= SELLING_INTERVAL / speed) {
            tickCount = 0;
            return true;
        }
        return false;
    }

    @Override
    public int getProgress() {
        return tickCount;
    }

    @Override
    public int sellingInterval() {
        return SELLING_INTERVAL;
    }

    @Override
    public void reset() {
        this.tickCount = 0;
    }

    @Override
    public String toString() {
        return "Constant";
    }
}
