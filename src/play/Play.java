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

    // параметры зависят от уровня сложности
    private int enemySpeed;
    private int enemySpawnDelay;
    private int coinSpawnDelay;
    private int playerStartLives;
    private int bgSpeedCoef;
    private String difficulty;

    private ScoreCounter score = new ScoreCounter();
    private final SoundPlayer backgroundMusic = new SoundPlayer("src/audio/game_theme.wav");

    private List<Coin> coins = new ArrayList<>();
    private Timer coinMoveTimer, coinSpawnTimer;

    private List<Enemy> enemies = new ArrayList<>();
    private Timer enemyMoveTimer, enemySpawnTimer;

    private PlayersPhysics playersPhysics;

    private Timer bossWaitTimer, bossTransitionTimer;
    private Boss boss;
    private BossFightController bossFightController;

    private boolean background = false;

    public Play() {

        difficulty = App.getProxy().getConfig().getProperty("difficulty", "Средний");

        switch (difficulty) {
            case "Легкий":
                enemySpeed = 4;
                enemySpawnDelay = 3500;
                coinSpawnDelay = 1500;
                playerStartLives = 5;
                bgSpeedCoef = 1;
                break;
            case "Сложный":
                enemySpeed = 14;
                enemySpawnDelay = 1300;
                coinSpawnDelay = 3500;
                playerStartLives = 2;
                bgSpeedCoef = 3;
                break;
            default: // Средний
                enemySpeed = 7;
                enemySpawnDelay = 3000;
                coinSpawnDelay = 2500;
                playerStartLives = 3;
                bgSpeedCoef = 2;
                break;
        }

        playersPhysics = new PlayersPhysics(playerStartLives);
        initPanel();
        panel.add(score.getScoreLabel());
        initButtons();
        initPlayer();
        initBackgroundDynamic();
        initKeyListener();
        updateKeyBindings();


        startCoinLogic();
        startEnemyLogic();

        waitingBoss();
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
        int[] speedsBG = {bgSpeedCoef, 3*bgSpeedCoef, 7*bgSpeedCoef};
        parallaxBackground = new ParallaxBG(backgrounds, speedsBG, yPositions, layerWidths, layerHeights, w, h);
        panel.add(parallaxBackground);
    }

    private void initBackground2() {
        String[] backgrounds = {
                "src/pics/parallaxBG/b1.png",
                "src/pics/parallaxBG/b2.png",
                "src/pics/parallaxBG/b3.png",
                "src/pics/parallaxBG/b4.png",
                "src/pics/parallaxBG/b5.png"
        };

        int[] yPositions = {0, h - 743, h - 469, h - 399, h - 456};
        int[] layerWidths = {3840, 3840, 3840, 3840, 3840};
        int[] layerHeights = {1080, 743, 469, 399, 456};
        int[] speedsBG = {bgSpeedCoef, 2*bgSpeedCoef, 3*bgSpeedCoef, 4*bgSpeedCoef, 5*bgSpeedCoef};

        parallaxBackground = new ParallaxBG(backgrounds, speedsBG, yPositions, layerWidths, layerHeights, w, h);
        panel.add(parallaxBackground);
    }

    private void initBackgroundDynamic() {
        if (parallaxBackground != null) {
            panel.remove(parallaxBackground); // удаляем предыдущий фон
            parallaxBackground.stop();
        }
        if (background) {
            initBackground();
        } else {
            initBackground2();
        }
        background = !background;
        panel.repaint();
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
                            stopBossLogic();
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

    private void waitingBoss(){
        bossWaitTimer = new Timer(10_000, e -> bossTransition());
        bossWaitTimer.setRepeats(false);
        bossWaitTimer.start();
    }

    private void bossTransition(){
        bossTransitionTimer = new Timer(7_000, e -> startBossFight());
        bossTransitionTimer.start();
        bossWaitTimer.stop();
        if (enemySpawnTimer != null) enemySpawnTimer.stop();
        if (coinSpawnTimer != null) coinSpawnTimer.stop();
        boss = new Boss(w, h);
        new SoundPlayer("src/audio/boss_apear.wav").play();
        boss.enter();
        panel.add(boss, 0);

        JLabel warningLabel = new JLabel("ПРИГОТОВЬСЯ К БИТВЕ!", JLabel.CENTER);
        warningLabel.setFont(new Font("Arial", Font.BOLD, 48));
        warningLabel.setForeground(Color.WHITE);
        warningLabel.setBounds(0, h / 2 - 50, w, 100);
        warningLabel.setOpaque(false);

        panel.add(warningLabel, 0);
        panel.repaint();
        new Timer(3000, e -> {
            panel.remove(warningLabel);
            panel.repaint();
        }).start();
    }

    private void startBossFight(){
        if (enemyMoveTimer != null) enemyMoveTimer.stop();
        if (coinMoveTimer != null) coinMoveTimer.stop();
        bossTransitionTimer.stop();

        //экземпляр контроллера; передача функции, которая исполнится в конце боя с боссом
        bossFightController = new BossFightController(win -> { // функция для исполнения
            panel.remove(bossFightController); // контроллер удаляем
            panel.repaint(); // перерисовка
            boss.exit(); // босс уходит
            initBackgroundDynamic();
            panel.requestFocusInWindow(); // возвращаем фокус на панель

            enemySpawnTimer.start();
            enemyMoveTimer.start();
            coinSpawnTimer.start();
            coinMoveTimer.start();

            waitingBoss();

            if (win) { // победа
                score.add(1000);
            } else { // поражение
                new SoundPlayer("src/audio/damage.wav").play();
                if (playersPhysics.damage()){ // когда жизни заканчиваются
                    new SoundPlayer("src/audio/gameOver.wav").play();
                    showGameOverScreen(score.getScore());
                }
            }
        });

        panel.add(bossFightController, 0);
        bossFightController.requestFocusInWindow();
    }

    private void stopBossLogic() {
        if (bossWaitTimer != null) {
            bossWaitTimer.stop();
            bossWaitTimer = null;
        }

        if (bossTransitionTimer != null) {
            bossTransitionTimer.stop();
            bossTransitionTimer = null;
        }

        if (bossFightController != null) {
            panel.remove(bossFightController);
            bossFightController = null;
        }

        if (boss != null) {
            panel.remove(boss);
            boss = null;
        }
    }

    private void exit(){
        backgroundMusic.stop();
        parallaxBackground.stop();
        score.stopScoreUpdater();
        stopBossLogic();
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