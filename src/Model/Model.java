package Model;

public class Model {
    private ModelObservable observable = new ModelObservable();
    private Field field = new Field(observable);

    public Model() {
    }

    public void newGame() {
        field.initGame();
        observable.notifyNewGame();
    }

    public EventType doubleClickCell(int posX, int posY) {
        return field.cellDoubleClick(posX, posY);
    }

    public EventType clickCell(int posX, int posY) {
        return field.makeMove(posX, posY);
    }

    public ModelObservable getObservable() {
        return observable;
    }

    public void registerObserver(ModelObserver o) {
        observable.registerObserver(o);
    }

    public void victory() {
        observable.notifyVictory();
    }

    public void gameOver() {
        observable.notifyGameOver();
    }

    public boolean cellIsOpen(int posX, int posY) {
        return field.cellIsOpen(posX, posY);
    }

    public void iterateCellMark(int posX, int posY) {
        field.iterateCellMark(posX, posY);
    }

    public CellMark getCellMark(int posX, int posY) {
        return field.getCellMark(posX, posY);
    }

    public int getScore() {
        return field.getOpenCells() * 10;
    }

    public void updateScore() {
        observable.notifyUpdateScore(getScore());
    }
}
