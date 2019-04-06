package simulation.Controller.SellingStrategies;

import java.util.Random;

public class RandomSellingStrategy implements ISellingStrategy {
    private Random random = new Random();

    @Override
    public boolean sell(double speed) {
        return random.nextInt(2000) == 42;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public int sellingInterval() {
        return 0;
    }

    @Override
    public void reset() {

    }

    @Override
    public String toString() {
        return "Random";
    }
}
