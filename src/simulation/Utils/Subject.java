package simulation.Utils;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();

    public void attach(Observer o) {
        this.observers.add(o);
    }

    public void dettach(Observer o) {
        this.observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    protected List<Observer> getObservers() {
        return observers;
    }
}
