package GUI.View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BackgroundPanel extends JPanel {
    private BufferedImage image;
    private BufferedImage extraImage;

    public BackgroundPanel() {
    }

    public void setBackground(BufferedImage i) {
        assert i != null;
        image = i;
        extraImage = null;
        repaint();
    }

    public void setExtraImage(BufferedImage i) {
        assert i != null;
        extraImage = i;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        assert image != null;
        g.drawImage(image, 0, 0, null);
        if (extraImage != null) {
            g.drawImage(extraImage, 0, 0, null);
        }
    }
}
