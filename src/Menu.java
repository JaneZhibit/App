import javax.swing.*;
import java.awt.*;

public class Menu {

    private static Menu proxy;  // Singleton
    private JPanel panel;
    private final int w = App.getProxy().w, h = App.getProxy().h;

    // Кнопки меню
    private final JButton playButton = new JButton("Играть");
    private final JButton upgradesButton = new JButton("Улучшения");
    private final JButton settingsButton = new JButton("Настройки");
    private final JButton exitButton = new JButton("Выйти");

    private final SoundPlayer backgroundMusic = new SoundPlayer("src/audio/main_theme.wav");

    public Menu() {
        initPanel();
        initButtons();
        setupListeners();
        addBackground();
    }

    // Singleton
    public static Menu getProxy() {
        if (proxy == null) {
            proxy = new Menu();
        }
        return proxy;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, w, h);
    }

    private void initButtons() {
        playButton.setBounds(w / 17, 3 * h / 8, w / 4, h / 10);
        panel.add(playButton);

        upgradesButton.setBounds(w / 17, 4 * h / 8, w / 4, h / 10);
        panel.add(upgradesButton);

        settingsButton.setBounds(w / 17, 5 * h / 8, w / 4, h / 10);
        panel.add(settingsButton);

        exitButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        panel.add(exitButton);

    }

    private void setupListeners() {
        SoundPlayer gameClick = new SoundPlayer("src/audio/click_sound.wav");
        playButton.addActionListener(e -> {
                backgroundMusic.stop();
                gameClick.play();
                App.getProxy().showPlay();
        });
        upgradesButton.addActionListener(e -> {
            gameClick.play();
            App.getProxy().showUpgrades();
        });

        settingsButton.addActionListener(e -> {
            gameClick.play();
            App.getProxy().showSettings();
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    // для красоты конструктора решил вынести в отдельную функци
    private void addBackground() {
        JLabel bg = new JLabel(new ImageIcon("src/pics/menu_bg.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);
    }

    public void playMusic(){
        backgroundMusic.loop();
    }

    public JPanel getPanel() {
        return panel;
    }
}
