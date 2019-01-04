package Model.View;

import java.util.LinkedList;
import java.util.List;

public class ViewObservable {
    protected final List<ViewObserver> observers = new LinkedList<>();

    public void registerObserver(ViewObserver o) {
        assert (o != null);
        observers.add(o);
    }

    public void notifyNewGame() {
        for (ViewObserver o : observers) {
            o.newGame();
        }
    }

    public void notifyCellPressed(int posX, int posY) {
        for (ViewObserver o : observers) {
            o.cellPressed(posX, posY);
        }
    }

    public void notifyCellMarkIterated(int posX, int posY) {
        for (ViewObserver o : observers) {
            o.cellMarkIterated(posX, posY);
        }
    }

    public void notifyOpenCellDoubleClick(int posX, int posY) {
        for (ViewObserver o : observers) {
            o.doubleClickCell(posX, posY);
        }

    }
}
