import javax.swing.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Play {
    private static Play proxy;
    private JPanel panel;
    private int w = App.getProxy().w;
    private int h = App.getProxy().h;
    private String moveUpKey, moveDownKey;
    private PlayersPhysics playersPhysics = PlayersPhysics.getProxy();
    private ParallaxBG parallaxBackground;

    private ArrayList<EnemyNPC> enemies = new ArrayList<>();
    private int playerHealth = 3; // Количество жизней игрока

    private final SoundPlayer backgroundMusic = new SoundPlayer("src/audio/game_theme.wav");

    private Play() {
        initPanel();
        initButtons();
        initPlayer();
        initBackground();
        initKeyListener();
        startEnemySpawner();
        updateKeyBindings();
    }

    public static Play getProxy() {
        if (proxy == null) {
            proxy = new Play();
        }
        proxy.updateKeyBindings();
        return proxy;
    }

    private void initPanel() {
        panel = new JPanel(null);
        panel.setBounds(0, 0, w, h);
        panel.setOpaque(false);
    }

    private void initButtons() {
        JButton backButton = new JButton("Назад");
        backButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        backButton.addActionListener(e -> {
            backgroundMusic.stop();
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

    private void startEnemySpawner() {
        Thread enemySpawner = new Thread(() -> {
            Random rand = new Random();
            while (true) {
                try {
                    Thread.sleep(rand.nextInt(2000) + 1000); // Пауза 1-3 сек между спавном
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spawnEnemy();
            }
        });
        enemySpawner.setDaemon(true);
        enemySpawner.start();
    }

    private void spawnEnemy() {
        EnemyNPC enemy = new EnemyNPC("src/pics/enemy.png", w, h, playersPhysics, this);
        enemies.add(enemy);
        panel.add(enemy);
        panel.setComponentZOrder(enemy, 0); // Убедимся, что враги поверх фона

        Thread enemyThread = new Thread(enemy);
        enemyThread.start();
    }

    public void removeEnemy(EnemyNPC enemy) {
        SwingUtilities.invokeLater(() -> {
            panel.remove(enemy);
            enemies.remove(enemy);
            panel.repaint();
        });
    }

    public void reducePlayerHealth() {
        playerHealth--;
        System.out.println("Игрок получил урон! Оставшиеся жизни: " + playerHealth);
        if (playerHealth <= 0) {
            System.out.println("Игра окончена!");
            backgroundMusic.stop();
            App.getProxy().showMenu(); // Можно сделать экран поражения
        }
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