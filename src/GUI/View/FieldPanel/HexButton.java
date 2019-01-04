package GUI.View.FieldPanel;

import javax.swing.*;
import java.awt.*;

public class HexButton extends JButton {
    private final int posX;
    private final int posY;
    private static final int side_length = 10;

    public HexButton(int x, int y, ImageIcon icon) {
        super(icon);
        setContentAreaFilled(false);
        setFocusPainted(true);
        setBorderPainted(false);
        setPreferredSize(new Dimension(20, 20));
        posX = x;
        posY = y;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            hex.addPoint((int) (10 + side_length * Math.cos(i * 2 * Math.PI / 6)), (int) (10 + side_length * Math.sin(i * 2 * Math.PI / 6)));
        }
        g.drawPolygon(hex);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}