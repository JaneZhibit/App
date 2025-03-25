import javax.swing.*;
import java.awt.event.*;

public class Play {
    private static Play proxy;
    private JPanel panel;
    private JLabel player;
    private int w = App.getProxy().w;
    private int h = App.getProxy().h;
    private String moveUpKey, moveDownKey; // Бинды клавиш
    private PlayersPhysics playersPhysics = PlayersPhysics.getProxy();

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
       panel.add(playersPhysics.getPlayer());
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
                    playersPhysics.moveUp();
                } else if (keyName.equals(moveDownKey)) {
                    playersPhysics.moveDown();
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                playersPhysics.keyReleased();
            }
        });
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
