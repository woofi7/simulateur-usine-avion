package simulation.Controller.SellingStrategies;

public interface ISellingStrategy {
    boolean sell(double speed);
    int getProgress();
    int sellingInterval();
    void reset();
}
