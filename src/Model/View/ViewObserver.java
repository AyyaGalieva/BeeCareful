package Model.View;

public interface ViewObserver {
    void newGame();

    void cellPressed(int posX, int posY);

    void cellMarkIterated(int posX, int posY);

    void doubleClickCell(int posX, int posY);
}
