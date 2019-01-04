package GUI;

import javax.swing.*;

import GUI.Controller.Controller;
import Model.Model;
import GUI.View.View;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Model model = new Model();
            View view = new View(model);
            new Controller(model, view);
        });
    }
}