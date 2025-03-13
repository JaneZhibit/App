import javax.swing.*;
import java.awt.event.*;

public class Play {
    private static Play proxy;
    private JPanel panel;
    private JLabel player;
    private int w = App.getProxy().w, h = App.getProxy().h;
    private int step = 15; // Шаг движения
    private String moveUpKey, moveDownKey; // Бинды клавиш

    private Play() {
        initPanel();
        initButtons();
        initPlayer();
        initBackground();
        initKeyListener();
        updateKeyBindings(); // Загружаем бинды при создании
    }

    public static Play getProxy() {
        if (proxy == null) {
            proxy = new Play();
        }
        proxy.updateKeyBindings(); // Загружаем актуальные бинды при каждом вызове
        return proxy;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, w, h);
    }

    private void initButtons() {
        JButton backButton = new JButton("Назад");
        backButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        backButton.addActionListener(e -> App.getProxy().showMenu());
        panel.add(backButton);
    }

    private void initPlayer() {
        player = new JLabel(new ImageIcon("src/pics/player.png"));
        int playerX = w / 20;
        int playerY = h / 3;
        int playerWidth = 609;
        int playerHeight = 359;
        player.setBounds(playerX, playerY, playerWidth, playerHeight);
        panel.add(player);
    }

    private void initBackground() {
        JLabel bg = new JLabel(new ImageIcon("src/pics/play_bg1.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);
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
                    movePlayer(-step);
                } else if (keyName.equals(moveDownKey)) {
                    movePlayer(step);
                }
            }
        });
    }

    private void movePlayer(int deltaY) {
        int newY = player.getY() + deltaY;
        if (newY >= 0 && newY <= h - 360) {
            player.setLocation(player.getX(), newY);
        }
    }

    // понял, почему у тебя не двигался игрок
    // данная функция следит за фокусом на panel
    // вызывается из app, можно обойтись без функции

    public void requestFocus() {
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    public JPanel getPanel() {
        return panel;
    }
}
