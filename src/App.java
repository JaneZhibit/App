import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class App extends JFrame {

    private JPanel panel;
    private Properties config;
    private File configFile = new File("config.properties");

    int w = 1280, h = 720;

    App() {
        super("Harry Potter and the programmer`s pain");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(w, h);
        this.setLayout(null);
        setResizable(false); // запрет на изменение размера окна

        loadConfig();
        Menu();
    }

    private void Menu() {
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        panel.setBounds(0, 0, w, h);


        // создание кнопок меню
        JButton playButton = new JButton("Играть");
        JButton settingsButton = new JButton("Настройки");
        JButton upgradesButton = new JButton("Улучшения");
        JButton exitButton = new JButton("Выйти");

        // свойства кнопок меню, добавление в контейнер
        playButton.setBounds(w / 17, 3 * h / 8, w / 4, h / 10);
        playButton.setBackground(Color.LIGHT_GRAY);
        panel.add(playButton);

        exitButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        exitButton.setBackground(Color.LIGHT_GRAY);
        panel.add(exitButton);

        settingsButton.setBounds(w / 17, 5 * h / 8, w / 4, h / 10);
        panel.add(settingsButton);

        upgradesButton.setBounds(w / 17, 4 * h / 8, w / 4, h / 10);
        panel.add(upgradesButton);

        // добавление действий к кнопкам
        JLabel goodbyeLabel = new JLabel("До свидания!");
        goodbyeLabel.setBounds(600, 0, w, h); // Растягиваем на весь экран
        goodbyeLabel.setFont(new Font("Arial", Font.BOLD, 72)); // Большой шрифт
        goodbyeLabel.setForeground(Color.WHITE);
        goodbyeLabel.setOpaque(false); // Прозрачный фон
        goodbyeLabel.setVisible(false);
        panel.add(goodbyeLabel);

        ExitButtonListener exitListener = new ExitButtonListener(goodbyeLabel, this);
        exitButton.addActionListener(exitListener);


        playButton.addActionListener(e -> {
            panel.removeAll();
            Play();
        });

        UpgradesButtonListener upgradesListener = new UpgradesButtonListener();
        upgradesButton.addActionListener(upgradesListener);

        settingsButton.addActionListener(new ActionListener() { // можно заменить на lambda
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll(); // очистка виджетов меню
                Settings();
            }
        });

        JLabel bg = new JLabel(new ImageIcon("src/pics/menu_bg.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);

        this.setVisible(true);
    }

    private void Settings() {
        // создание контейнера
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        panel.setBounds(0, 0, w, h);

        // Выбор сложности надпись
        JLabel difficultyLabel = new JLabel("Уровень сложности:");
        difficultyLabel.setOpaque(true);
        difficultyLabel.setBounds(w / 17, 2*h/8 - h/24, w/4, h/25);
        panel.add(difficultyLabel);

        // Выбор сложности JComboBox
        String[] difficulties = {"Легкий", "Средний", "Сложный"};
        JComboBox<String> difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.setBounds(w / 17,  2*h/8, w/4, h/16);
        difficultyBox.setSelectedItem(config.getProperty("difficulty", "Средний"));
        panel.add(difficultyBox);

        // Бинд надпись
        JLabel moveUpLabel = new JLabel("Лететь вверх");
        moveUpLabel.setOpaque(true);
        moveUpLabel.setBounds(w / 17, 4*h/8 - h/24, w/4, h/25);
        panel.add(moveUpLabel);

        // кнопка бинда вверх
        JButton moveUpButton = new JButton(config.getProperty("moveUp", "W"));
        moveUpButton.setBounds(w / 17 + w/40, 4*h/8, w/5, h/16);
        panel.add(moveUpButton);

        // Бинд надпись
        JLabel moveDownLabel = new JLabel("Лететь вниз");
        moveDownLabel.setOpaque(true);
        moveDownLabel.setBounds(w / 17, 5*h/8 - h/24, w/4, h/25);
        panel.add(moveDownLabel);

        // кнопка бинда вниз
        JButton moveDownButton = new JButton(config.getProperty("moveDown", "S"));
        moveDownButton.setBounds(w / 17 + w/40, 5*h/8, w/5, h/16);
        panel.add(moveDownButton);

        // слушатели кнопок вверх и вниз
        BindKeyListener bindUpKeyListener = new BindKeyListener(moveUpButton);
        BindKeyListener bindDownKeyListener = new BindKeyListener(moveDownButton);

        moveUpButton.addActionListener(bindUpKeyListener);
        moveDownButton.addActionListener(bindDownKeyListener);

        // добавление кнопки сохранить и выйти и логика работы
        JButton saveButton = new JButton("Сохранить и выйти");
        saveButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        panel.add(saveButton);
        saveButton.addActionListener(e -> {
            config.setProperty("moveDownKey", moveDownButton.getText());
            config.setProperty("moveUpKey", moveUpButton.getText());
            config.setProperty("difficulty", (String) difficultyBox.getSelectedItem());
            saveConfig();
            panel.removeAll();
            Menu();
        });

        JLabel bg = new JLabel(new ImageIcon("src/pics/menu_bg.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);
        this.setVisible(true);

    }

    private void Play() {
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        panel.setBounds(0, 0, w, h);

        JButton backButton = new JButton("Назад (временный вариант)");
        backButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        panel.add(backButton);
        backButton.addActionListener(e -> {
            panel.removeAll();
            Menu();
        });

        JLabel player = new JLabel(new ImageIcon("src/pics/player.png"));
        // начальное положение игрока
        int playerX = w / 20;
        int playerY = h / 3;
        player.setBounds(playerX, playerY, 609, 359);
        panel.add(player);

        JLabel bg = new JLabel(new ImageIcon("src/pics/play_bg1.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);

        // Загружаем бинды клавиш из config.properties
        String moveUpKey = config.getProperty("moveUpKey", "W");
        String moveDownKey = config.getProperty("moveDownKey", "S");

        // Добавляем KeyListener
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(new KeyAdapter() {
            int step = 15; // Шаг движения

            @Override
            public void keyPressed(KeyEvent e) {
                String keyName = KeyEvent.getKeyText(e.getKeyCode());

                // забавно, но для сравнения строк в java нужно использовать .equals()
                if (keyName.equals(moveUpKey)) {
                    // Новая позиция после движения вверх
                    int newY = player.getY() - step;

                    // Проверка, чтобы игрок не вылетел за верхнюю границу
                    if (newY >= -120) {
                        player.setLocation(player.getX(), newY);
                    }
                } else if (keyName.equals(moveDownKey)) {
                    // Новая позиция после движения вниз
                    int newY = player.getY() + step;

                    // Проверка, чтобы игрок не вылетел за нижнюю границу
                    if (newY <= h - 180) {
                        player.setLocation(player.getX(), newY);
                    }
                }

            }
        });

        this.setVisible(true);
    }

    private void loadConfig() {
        config = new Properties();
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "Game settings file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new App();
    }
}
