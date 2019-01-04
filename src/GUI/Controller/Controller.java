package GUI.Controller;

import GUI.View.View;
import Model.Clock;
import Model.Model;

import javax.swing.*;

public class Controller {
    private final Model model;
    private final View view;

    private Clock timer;

    public Controller(final Model m, final View v) {
        assert m != null;
        assert v != null;
        model = m;
        view = v;

        timer = new Clock(model.getObservable());

        GraphicsModelObserver modelObserver = new GraphicsModelObserver(this);
        GraphicsViewObserver viewObserver = new GraphicsViewObserver(this);

        m.registerObserver(modelObserver);
        v.registerObserver(viewObserver);
    }

    Model getModel() {
        return model;
    }

    View getView() {
        return view;
    }

    void victory() {
        timer.stop();

        SwingUtilities.invokeLater(model::victory);
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }
}
