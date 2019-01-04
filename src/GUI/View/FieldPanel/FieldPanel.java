package GUI.View.FieldPanel;

import Model.CellMark;
import Model.View.ViewObservable;
import Model.Model;

import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class FieldPanel extends JPanel {

    private final ViewObservable observable;
    private HexButton[][] buttons;
    private final Model model;
    private static final Map<CellMark, String> cellMarkIconsResources = new HashMap<>();

    static {
        cellMarkIconsResources.put(CellMark.NONE, "/Resources/notOpenedCell.png");
        cellMarkIconsResources.put(CellMark.FLAG, "/Resources/flaggedCell.png");
    }

    public FieldPanel(final ViewObservable o, final Model m) {
        assert o != null;
        assert m != null;
        observable = o;
        model = m;

        setOpaque(false);
        setBackground(Color.black);
    }

    public void disableButtons() {
        for (HexButton[] rows : buttons) {
            for (HexButton button : rows) {
                for (MouseListener listener : button.getMouseListeners())
                    button.removeMouseListener(listener);
            }
        }
    }

    public void init(int width, int height) {
        buttons = new HexButton[width][height];
        removeAll();

        setLayout(new MigLayout("gap 0px 0px"));

        BufferedImage notOpenedButtonImage;
        ImageIcon notOpenedButtonIcon = null;
        try {
            notOpenedButtonImage = ImageIO.read(this.getClass().getResource(cellMarkIconsResources.get(CellMark.NONE)));
            notOpenedButtonIcon = new ImageIcon(notOpenedButtonImage);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                HexButton button = new HexButton(x, y, notOpenedButtonIcon);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent event) {
                        int clickedCount = event.getClickCount();
                        HexButton sourceButton = (HexButton) event.getSource();

                        if (SwingUtilities.isLeftMouseButton(event)) {
                            switch (clickedCount) {
                                case 1:
                                    processLeftClick(sourceButton);
                                    break;
                                case 2:
                                    processLeftDoubleClick(sourceButton);
                                    break;
                            }
                        } else if (SwingUtilities.isRightMouseButton(event))
                            processRightClick(sourceButton);
                    }

                    private void processRightClick(HexButton sourceButton) {
                        int posX = sourceButton.getPosX();
                        int posY = sourceButton.getPosY();

                        if (!model.cellIsOpen(posX, posY)) {
                            observable.notifyCellMarkIterated(posX, posY);
                            CellMark cellMark = model.getCellMark(posX, posY);
                            BufferedImage buttonImage = null;
                            try {
                                buttonImage = ImageIO.read(this.getClass().getResource(cellMarkIconsResources.get(cellMark)));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.exit(0);
                            }

                            ImageIcon buttonIcon;
                            try {
                                buttonIcon = new ImageIcon(buttonImage);
                                sourceButton.setIcon(buttonIcon);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.exit(0);
                            }
                        }
                    }

                    private void processLeftClick(HexButton sourceButton) {
                        int posX = sourceButton.getPosX();
                        int posY = sourceButton.getPosY();

                        System.out.println("x " + posX + " y " + posY);

                        CellMark cellMark = model.getCellMark(posX, posY);
                        if (cellMark != CellMark.FLAG)
                            observable.notifyCellPressed(posX, posY);
                    }

                    private void processLeftDoubleClick(HexButton sourceButton) {
                        int posX = sourceButton.getPosX();
                        int posY = sourceButton.getPosY();

                        observable.notifyOpenCellDoubleClick(posX, posY);
                    }
                });
                buttons[x][y] = button;
                add(buttons[x][y], "width 20!, height 20!" + ((x % 2 == 0) ? ", gaptop 20px" : "") + (x == width - 1 ? ((y == height - 1) ? ", wrap" : ", wrap -20") : ""));
            }
        }
    }

    public void setButtonIcon(String image, int posX, int posY) {
        BufferedImage cellImage;
        try {
            cellImage = ImageIO.read(this.getClass().getResource(image));
            ImageIcon icon = new ImageIcon(cellImage);
            getButton(posX, posY).setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openEmptyCell(int posX, int posY) {
        setButtonIcon("/Resources/openedCell.png", posX, posY);
    }

    public void openNumberedCell(int posX, int posY, int number) {
        String image = null;
        switch (number) {
            case 1: {
                image = "/Resources/1.png";
                break;
            }
            case 2: {
                image = "/Resources/2.png";
                break;
            }
            case 3: {
                image = "/Resources/3.png";
                break;
            }
            case 4: {
                image = "/Resources/4.png";
                break;
            }
            case 5: {
                image = "/Resources/5.png";
                break;
            }
            case 6: {
                image = "/Resources/6.png";
                break;
            }
            default:
                break;
        }
        setButtonIcon(image, posX, posY);
    }

    public void openMine(int posX, int posY) {
        setButtonIcon("/Resources/minedCell.png", posX, posY);
    }

    private HexButton getButton(int posX, int posY) {
        return buttons[posX][posY];
    }
}
