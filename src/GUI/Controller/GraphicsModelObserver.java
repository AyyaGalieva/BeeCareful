package GUI.Controller;

import GUI.View.View;
import Model.ModelObserver;

public class GraphicsModelObserver implements ModelObserver {
    private final View view;

    public GraphicsModelObserver(final Controller controller) {
        view = controller.getView();
    }

    @Override
    public void openMined(int posX, int posY) {
        view.openMine(posX, posY);
    }

    @Override
    public void openEmpty(int posX, int posY) {
        view.openEmptyCell(posX, posY);
    }

    @Override
    public void openNumbered(int xPos, int yPos, int number) {
        view.openNumberedCell(xPos, yPos, number);
    }

    @Override
    public void updateScore(int score) {
        view.updateScore(score);
    }

    @Override
    public void updateTime(long sec) {
        view.updateTime(sec);
    }

    @Override
    public void newGame() {
        view.newGame();
    }

    @Override
    public void gameOver() {
        view.gameOver();
    }

    @Override
    public void victory() {
        view.victory();
    }

    @Override
    public void finishOpen() {
    }
}
