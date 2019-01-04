package Model;

public interface ModelObserver extends FieldObserver {
    void newGame();

    void gameOver();

    void victory();

    void updateScore(int score);

    void updateTime(long sec);

}
