import javax.swing.*;
import java.awt.event.*;

public class Play {
    private static Play proxy;
    private JPanel panel;
    private int w = App.getProxy().w;
    private int h = App.getProxy().h;
    private String moveUpKey, moveDownKey;
    private PlayersPhysics playersPhysics = new PlayersPhysics();
    private ParallaxBG parallaxBackground;
    private ScoreCounter score = new ScoreCounter();

    private final SoundPlayer backgroundMusic = new SoundPlayer("src/audio/game_theme.wav");

    public Play() {
        initPanel();
        panel.add(score.getScoreLabel());
        initButtons();
        initPlayer();
        initBackground();
        initKeyListener();
        updateKeyBindings();

    }

    private void initPanel() {
        panel = new JPanel(null);
        panel.setBounds(0, 0, w, h);
        panel.setOpaque(false);
    }



    private void initButtons() {
        JButton backButton = new JButton();
        ImageIcon buttonIcon = new ImageIcon("src/pics/backButton.png");
        int iconH = buttonIcon.getIconHeight(), iconW = buttonIcon.getIconWidth();
        backButton.setIcon(buttonIcon);
        backButton.setBounds(w - iconW - 30, 20, iconW, iconH);
        backButton.addActionListener(e -> {
            backgroundMusic.stop();
            parallaxBackground.stop();
            score.stopScoreUpdater();
            deleteObjects();
            App.getProxy().showMenu();
        });
        panel.add(backButton);
    }

    private void initPlayer() {
        panel.add(playersPhysics.getPlayer());
    }

    private void initBackground() {
        String[] backgrounds = {
                "src/pics/parallaxBG/0.png",
                "src/pics/parallaxBG/1.png",
                "src/pics/parallaxBG/2.png"
        };

        int[] speeds = {1, 3, 10};
        int[] yPositions = {0, h - 573, h - 299};
        int[] layerWidths = {2024, 2024, 2024};
        int[] layerHeights = {768, 573, 299};
        parallaxBackground = new ParallaxBG(backgrounds, speeds, yPositions, layerWidths, layerHeights, w, h);
        panel.add(parallaxBackground);
    }

    private void updateKeyBindings() {
        moveUpKey = App.getProxy().getConfig().getProperty("moveUpKey", "W");
        moveDownKey = App.getProxy().getConfig().getProperty("moveDownKey", "S");
    }

    private void initKeyListener() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyName = KeyEvent.getKeyText(e.getKeyCode());

                if (keyName.equals(moveUpKey)) {
                    playersPhysics.moveUp();
                } else if (keyName.equals(moveDownKey)) {
                    playersPhysics.moveDown();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                playersPhysics.keyReleased();
            }
        });
    }

    private void deleteObjects(){
        playersPhysics = null;
    }

    public void playMusic() {
        backgroundMusic.loop();
    }

    public void requestFocus() {
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    public JPanel getPanel() {
        return panel;
    }
}
