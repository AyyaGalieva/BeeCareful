package GUI.Controller;

import Model.View.ViewObserver;
import Model.Model;
import Model.EventType;

public class GraphicsViewObserver implements ViewObserver {
    private final Model model;
    private final Controller controller;

    public GraphicsViewObserver(final Controller c) {
        assert c != null;
        controller = c;
        model = controller.getModel();
    }

    @Override
    public void newGame() {
        model.newGame();
        controller.stopTimer();
    }

    @Override
    public void cellPressed(int posX, int posY) {
        EventType result = model.clickCell(posX, posY);
        setVerdict(result);
    }

    private void setVerdict(EventType result) {
        switch (result) {
            case GAME_OVER:
                gameOver();
                break;
            case OPEN_CELL:
                updateScore();
                break;
            case PRESS_OPENED_CELL:
            case PRESS_FLAGGED_CELL:
                break;
            case FIRST_MOVE:
                updateScore();
                controller.startTimer();
                break;
            case VICTORY:
                updateScore();
                controller.victory();
                break;
            default:
                break;
        }
    }

    @Override
    public void cellMarkIterated(int posX, int posY) {
        model.iterateCellMark(posX, posY);
    }

    @Override
    public void doubleClickCell(int posX, int posY) {
        EventType result = model.doubleClickCell(posX, posY);
        setVerdict(result);
    }

    private void updateScore() {
        model.updateScore();
    }

    private void gameOver() {
        controller.stopTimer();
        model.gameOver();
    }
}
