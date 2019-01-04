package Model;

import java.util.LinkedList;
import java.util.List;

public class FieldObservable<T extends FieldObserver> {
    protected final List<T> observers = new LinkedList<>();

    public void registerObserver(T o) {
        assert (o != null);
        observers.add(o);
    }

    public void notifyMinedCellOpened(int posX, int posY) {
        for (T o : observers)
            o.openMined(posX, posY);
    }

    public void notifyEmptyCellOpened(int posX, int posY) {
        for (T o : observers)
            o.openEmpty(posX, posY);
    }

    public void notifyNumberedCellOpened(int posX, int posY, int number) {
        for (T o : observers)
            o.openNumbered(posX, posY, number);
    }

    public void notifyFinishOpen() {
        for (T o : observers)
            o.finishOpen();
    }
}
