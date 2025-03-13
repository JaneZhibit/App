import javax.swing.*;

public class Settings {
    private static Settings proxy;
    private JPanel panel;
    private JComboBox<String> difficultyBox;
    private JButton moveDownButton;
    private JButton moveUpButton;
    private final int w = App.getProxy().w, h = App.getProxy().h;

    public Settings() {
        initPanel();
        initButtons();
        initLabels();
        initListeners();
        addBackground();
    }

    // Потокобезопасный синглтон
    public static Settings getProxy() {
        if (proxy == null) {
            proxy = new Settings();
        }
        return proxy;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, w, h);
    }

    private void initButtons() {
        moveDownButton = new JButton(App.getProxy().getConfig().getProperty("moveDownKey", "S"));
        moveUpButton = new JButton(App.getProxy().getConfig().getProperty("moveUpKey", "W"));

        moveUpButton.setBounds(w / 17 + w / 40, 4 * h / 8, w / 5, h / 16);
        moveDownButton.setBounds(w / 17 + w / 40, 5 * h / 8, w / 5, h / 16);

        JButton saveButton = new JButton("Сохранить и выйти");
        saveButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        saveButton.addActionListener(e -> saveSettings());

        panel.add(moveUpButton);
        panel.add(moveDownButton);
        panel.add(saveButton);
    }

    private void initLabels() {
        JLabel difficultyLabel = createLabel("Уровень сложности:", w / 17, 2 * h / 8 - h / 24);
        JLabel moveUpLabel = createLabel("Лететь вверх", w / 17, 4 * h / 8 - h / 24);
        JLabel moveDownLabel = createLabel("Лететь вниз", w / 17, 5 * h / 8 - h / 24);

        panel.add(difficultyLabel);
        panel.add(moveUpLabel);
        panel.add(moveDownLabel);

        String[] difficulties = {"Легкий", "Средний", "Сложный"};
        difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.setBounds(w / 17, 2 * h / 8, w / 4, h / 16);
        difficultyBox.setSelectedItem(App.getProxy().getConfig().getProperty("difficulty", "Средний"));
        panel.add(difficultyBox);
    }

    private void initListeners() {
        BindKeyListener bindUpKeyListener = new BindKeyListener(moveUpButton);
        BindKeyListener bindDownKeyListener = new BindKeyListener(moveDownButton);
        moveUpButton.addActionListener(bindUpKeyListener);
        moveDownButton.addActionListener(bindDownKeyListener);
    }

    private void addBackground() {
        JLabel bg = new JLabel(new ImageIcon("src/pics/menu_bg.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w / 4, h / 25);
        label.setOpaque(true);
        return label;
    }

    private void saveSettings() {
        App.getProxy().getConfig().setProperty("moveDownKey", moveDownButton.getText());
        App.getProxy().getConfig().setProperty("moveUpKey", moveUpButton.getText());
        App.getProxy().getConfig().setProperty("difficulty", (String) difficultyBox.getSelectedItem());
        App.getProxy().saveConfig();
        App.getProxy().showMenu();
    }

    public JPanel getPanel() {
        return panel;
    }
}
