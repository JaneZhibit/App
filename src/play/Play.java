package play;

import app.App;
import utils.SoundPlayer;
import play.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Play {
    private JPanel panel;
    private int w = App.getProxy().w;
    private int h = App.getProxy().h;
    private String moveUpKey, moveDownKey;
    private ParallaxBG parallaxBackground;

    private int enemySpeed;
    private int enemySpawnDelay;
    private int coinSpawnDelay;
    private int playerStartLives;
    private int[] speedsBG;
    private String difficulty;

    private ScoreCounter score = new ScoreCounter();
    private final SoundPlayer backgroundMusic = new SoundPlayer("src/audio/game_theme.wav");

    private List<Coin> coins = new ArrayList<>();
    private Timer coinMoveTimer, coinSpawnTimer;

    private List<Enemy> enemies = new ArrayList<>();
    private Timer enemyMoveTimer, enemySpawnTimer;

    private PlayersPhysics playersPhysics;

    public Play() {

        difficulty = App.getProxy().getConfig().getProperty("difficulty", "Средний");

        switch (difficulty) {
            case "Легкий":
                enemySpeed = 4;
                enemySpawnDelay = 3500;
                coinSpawnDelay = 1500;
                playerStartLives = 5;
                speedsBG = new int[]{1, 3, 8};

                break;
            case "Сложный":
                enemySpeed = 14;
                enemySpawnDelay = 1300;
                coinSpawnDelay = 3500;
                playerStartLives = 2;
                speedsBG = new int[]{2, 7, 17};

                break;
            default: // Средний
                enemySpeed = 7;
                enemySpawnDelay = 3000;
                coinSpawnDelay = 2500;
                playerStartLives = 3;
                speedsBG = new int[]{2, 4, 11};

                break;
        }

        playersPhysics = new PlayersPhysics(playerStartLives);
        initPanel();
        panel.add(score.getScoreLabel());
        initButtons();
        initPlayer();
        initBackground();
        initKeyListener();
        updateKeyBindings();

        startCoinLogic();
        startEnemyLogic();
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
        backButton.addActionListener(e -> exit());
        panel.add(backButton);
    }

    private void initPlayer() {
        panel.add(playersPhysics.getPlayer());
        panel.add(playersPhysics.getLivesLabel());

    }

    private void initBackground() {
        String[] backgrounds = {
                "src/pics/parallaxBG/0.png",
                "src/pics/parallaxBG/1.png",
                "src/pics/parallaxBG/2.png"
        };

        int[] yPositions = {0, h - 573, h - 299};
        int[] layerWidths = {2024, 2024, 2024};
        int[] layerHeights = {768, 573, 299};
        parallaxBackground = new ParallaxBG(backgrounds, speedsBG, yPositions, layerWidths, layerHeights, w, h);
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

//                if (bossFightActive && bossFightController != null) {
//                    bossFightController.handleKeyPress(keyName);
//                }

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

    private void startEnemySpawner() {
        enemySpawnTimer = new Timer(enemySpawnDelay, e -> {
            Enemy enemy = new Enemy(enemySpeed);
            enemies.add(enemy);
            panel.add(enemy.getLabel(), 0);
            panel.repaint();
        });
        enemySpawnTimer.start();
    }

    private void startEnemyLogic() {
        startEnemySpawner();
        enemyMoveTimer = new Timer(30, e -> {
            Rectangle playerBounds = playersPhysics.getHitbox();

            List<Enemy> toRemove = new ArrayList<>();

            for (Enemy enemy : enemies) {
                if (!enemy.isHit()) {
                    enemy.move();

                    if (playerBounds.intersects(enemy.getBounds())) {
                        enemy.hit();
                        new SoundPlayer("src/audio/damage.wav").play();
                        if (playersPhysics.damage()){ // когда жизни заканчиваются
                            new SoundPlayer("src/audio/gameOver.wav").play();
                            showGameOverScreen(score.getScore());
                        }
                        toRemove.add(enemy);
                    } else if (enemy.isOutOfScreen()) {
                        toRemove.add(enemy);
                    }
                }
            }

            for (Enemy enemy : toRemove) {
                panel.remove(enemy.getLabel());
                enemies.remove(enemy);
            }

            panel.repaint();
        });
        enemyMoveTimer.start();
    }

    private void startCoinSpawner() {
        coinSpawnTimer = new Timer(coinSpawnDelay, e -> {
            Coin coin = new Coin();
            coins.add(coin);
            panel.add(coin.getLabel(), 0);
            panel.repaint();
        });
        coinSpawnTimer.start();
    }

    private void startCoinLogic() {
        startCoinSpawner();
        coinMoveTimer = new Timer(30, e -> {
            Rectangle playerBounds = playersPhysics.getHitbox();

            List<Coin> toRemove = new ArrayList<>();

            for (Coin coin : coins) {
                if (!coin.isCollected()) {
                    coin.move();

                    if (playerBounds.intersects(coin.getBounds())) {
                        coin.collect();
                        score.add(100);
                        new SoundPlayer("src/audio/coin.wav").play();
                        toRemove.add(coin);
                    } else if (coin.isOutOfScreen()) {
                        toRemove.add(coin);
                    }
                }
            }

            for (Coin coin : toRemove) {
                panel.remove(coin.getLabel());
                coins.remove(coin);
            }
            panel.repaint();
        });
        coinMoveTimer.start();
    }

    public int getTotalPoints() {
        String value = App.getProxy().getConfig().getProperty("totalPoints", "0");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void addPoints(int earned) {
        int current = getTotalPoints();
        App.getProxy().getConfig().setProperty("totalPoints", String.valueOf(current + earned));
        App.getProxy().saveConfig();
    }

    private void exit(){
        backgroundMusic.stop();
        parallaxBackground.stop();
        score.stopScoreUpdater();
        deleteObjects();
        App.getProxy().showMenu();
    }

    private void deleteObjects() {
        playersPhysics = null;
        score = null;
        if (coinMoveTimer != null) coinMoveTimer.stop();
        coins.clear();
        if (enemyMoveTimer != null) enemyMoveTimer.stop();
        if (enemySpawnTimer != null) enemySpawnTimer.stop();
        enemies.clear();
    }

    private void showGameOverScreen(int finalScore) {
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRect(w/4, h/4, w/2, h/2);
            }
        };

        overlay.setLayout(new GridBagLayout());
        overlay.setBounds(0, 0, w, h);
        addPoints(finalScore);


        if (coinMoveTimer != null) coinMoveTimer.stop();
        coins.clear();
        if (enemyMoveTimer != null) enemyMoveTimer.stop();

        JLabel gameOverLabel = new JLabel("Игра окончена! Счёт: " + finalScore);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setForeground(Color.WHITE);
        overlay.add(gameOverLabel);

        panel.add(overlay, 0);
        panel.repaint();

        new Timer(4000, e -> exit()).start();
    }

    public void playMusic() {
        backgroundMusic.loop();
    }

    public void requestFocus() {
        panel.setFocusable(true);
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    public JPanel getPanel() {
        return panel;
    }
}