package Model;

public class ModelObservable extends FieldObservable<ModelObserver> {
    public void notifyNewGame() {
        for (ModelObserver o : observers)
            o.newGame();
    }

    public void notifyGameOver() {
        for (ModelObserver o : observers)
            o.gameOver();
    }

    public void notifyUpdateScore(int score) {
        for (ModelObserver o : observers)
            o.updateScore(score);
    }

    public void notifyVictory() {
        for (ModelObserver o : observers)
            o.victory();
    }

    public void notifyUpdateTime(long sec) {
        for (ModelObserver o : observers)
            o.updateTime(sec);
    }
}
