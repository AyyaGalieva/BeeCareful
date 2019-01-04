package GUI.View;

import GUI.View.FieldPanel.FieldPanel;
import Model.Model;
import Model.View.ViewObservable;
import Model.View.ViewObserver;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class View extends JFrame {

    public static final int BUTTON_WIDTH = 20;
    public static final int BUTTON_HEIGHT = 20;

    private final Model model;
    private final ViewObservable observable;
    private FieldPanel fieldPanel;
    private final BackgroundPanel backgroundPanel;
    private JLabel scoreLabel;
    private JLabel timeLabel;

    private JMenu menu;

    public View(final Model m) {
        super("Bee Careful!");
        model = m;
        observable = new ViewObservable();
        registerWindowListener();

        backgroundPanel = new BackgroundPanel();

        initBackground();

        menu = new JMenu("Game");
        initMenu();

        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        bar.add(menu);


        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void registerObserver(ViewObserver o) {
        assert (o != null);
        observable.registerObserver(o);
    }

    private void initBackground() {
        backgroundPanel.setLayout(new MigLayout());
        setContentPane(backgroundPanel);
        BufferedImage mainMenuImage = null;
        try {
            mainMenuImage = ImageIO.read(this.getClass().getResource("/Resources/mainMenu1.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        backgroundPanel.setBackground(mainMenuImage);
    }

    private void initMenu() {
        menu.setMnemonic('M');

        JMenuItem newGame = new JMenuItem("New game");
        menu.add(newGame);
        newGame.addActionListener(e -> View.this.pressNewGame());

        JMenuItem exit = new JMenuItem("Exit");
        menu.add(exit);
        exit.addActionListener(e -> System.exit(0));
    }

    public void newGame() {
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(this.getClass().getResource("/Resources/gameBackground.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fieldPanel = new FieldPanel(observable, model);
        backgroundPanel.setBackground(backgroundImage);
        fieldPanel.init(16, 16);
        fieldPanel.setVisible(true);
        Container contentPanel = getContentPane();

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new MigLayout());

        Dimension panelSize = new Dimension(16 * BUTTON_WIDTH, 16 * BUTTON_HEIGHT);
        int x = 300 - panelSize.width / 2;
        int y = 200 - panelSize.height / 2;
        mainPanel.add(fieldPanel, "pos " + x + " " + y);

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("StudioScriptCTT", Font.BOLD, 50));
        scoreLabel.setText("0");
        scoreLabel.setForeground(new Color(250, 234, 124));

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("StudioScriptCTT", Font.BOLD, 40));
        timeLabel.setText("0:00");
        timeLabel.setForeground(new Color(250, 196, 13));

        contentPanel.removeAll();
        contentPanel.add(mainPanel);
        contentPanel.add(scoreLabel, "pos 28 10");
        contentPanel.add(timeLabel, "pos 28 70");

        validate();
        invalidate();
    }

    public void gameOver() {
        SwingUtilities.invokeLater(() -> {
            showGameOverPanel();
            makeSound("/Resources/Buzz.wav");
        });
    }

    private void makeSound(String soundFile) {
        InputStream is = getClass().getResourceAsStream(soundFile);
        AudioInputStream ais;
        Clip clip;
        try {
            ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateScore(int score) {
        SwingUtilities.invokeLater(() -> {
            assert (null != scoreLabel);
            scoreLabel.setText("" + score);
            makeSound("/Resources/Drop.wav");
        });
    }

    public void updateTime(long seconds) {
        SwingUtilities.invokeLater(() -> {
            assert (timeLabel != null);
            long minutes = seconds / 60;
            String str = String.format("%d:%02d", minutes, seconds % 60);
            timeLabel.setText(str);
        });
    }

    private void showGameOverPanel() {
        BufferedImage gameOverPanelImage = null;
        try {
            gameOverPanelImage = ImageIO.read(this.getClass().getResource("/Resources/gameOver.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fieldPanel.disableButtons();
        timeLabel = null;
        scoreLabel = null;
        backgroundPanel.setExtraImage(gameOverPanelImage);
    }

    private void showVictoryPanel() {
        BufferedImage victoryPanelImage = null;
        try {
            victoryPanelImage = ImageIO.read(this.getClass().getResource("/Resources/victory_background1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fieldPanel.setVisible(false);
        backgroundPanel.remove(fieldPanel);
        fieldPanel = null;

        backgroundPanel.setBackground(victoryPanelImage);

        getContentPane().remove(timeLabel);
        timeLabel.setForeground(new Color(250, 129, 0));
        timeLabel.setText("<html>Time:<br/>" + timeLabel.getText() + "</html>");
        getContentPane().add(timeLabel, "pos 650 170");

        getContentPane().remove(scoreLabel);
        scoreLabel.setForeground(new Color(250, 64, 6));
        scoreLabel.setText("<html>Your score:<br/>" + scoreLabel.getText() + "</html>");
        getContentPane().add(scoreLabel, "pos 10 170");
    }

    public void victory() {
        SwingUtilities.invokeLater(() -> {
            showVictoryPanel();
            makeSound("/Resources/win.wav");
        });
    }

    public void openMine(int posX, int posY) {
        fieldPanel.openMine(posX, posY);
    }

    public void openEmptyCell(int posX, int posY) {
        fieldPanel.openEmptyCell(posX, posY);
    }

    public void openNumberedCell(int posX, int posY, int number) {
        fieldPanel.openNumberedCell(posX, posY, number);
    }

    public void pressNewGame() {
        observable.notifyNewGame();
    }
}
