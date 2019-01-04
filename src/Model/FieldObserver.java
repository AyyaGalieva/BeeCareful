package Model;

public interface FieldObserver {
    void openMined(int posX, int posY);

    void openEmpty(int posX, int posY);

    void openNumbered(int posX, int posY, int number);

    void finishOpen();
}
