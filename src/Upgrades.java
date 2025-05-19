import javax.swing.*;
import java.awt.*;

public class Upgrades {
    private static Upgrades proxy;
    private JPanel panel;
    private final int w = App.getProxy().w, h = App.getProxy().h;
    private int totalPoints;
    private JLabel pointsLabel;
    private java.util.Map<Broom, JButton> broomButtons = new java.util.HashMap<>();


    public Upgrades() {
        initPanel();
        initBackButton();
        initPointsLabel();
        initBroomChoices();
        addBackground();
    }

    public void refresh() {
        totalPoints = getTotalPoints();
        pointsLabel.setText("Всего очков: " + totalPoints);
    }

    public static Upgrades getProxy() {
        if (proxy == null) {
            proxy = new Upgrades();
        }
        return proxy;
    }

    private void initPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, w, h);
    }

    private void initBackButton() {
        JButton backButton = new JButton("Вернуться в меню");
        backButton.setBounds(w / 17, 6 * h / 8, w / 4, h / 10);
        backButton.addActionListener(e -> App.getProxy().showMenu());
        panel.add(backButton);
    }

    public int getTotalPoints() {
        String value = App.getProxy().getConfig().getProperty("totalPoints", "0");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void initPointsLabel() {
        totalPoints = getTotalPoints();
        pointsLabel = new JLabel("Всего очков: " + totalPoints);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 28));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setBounds(w / 2 - 150, h / 8, 300, 40);
        panel.add(pointsLabel);
    }

    private void addBackground() {
        JLabel bg = new JLabel(new ImageIcon("src/pics/upgrades_bg.png"));
        bg.setBounds(0, 0, w, h);
        panel.add(bg);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void initBroomChoices() {
        String selected = App.getProxy().getSelectedBroomName();

        int y = h / 4;

        for (Broom broom : Broom.values()) {
            JPanel broomPanel = new JPanel(null);
            broomPanel.setBounds(w / 2 - 200, y, 400, 120);
            broomPanel.setOpaque(false);

            // Картинка метлы
            ImageIcon broomIcon = new ImageIcon("src/pics/brooms/" + broom.name().toLowerCase() + ".png");
            JLabel iconLabel = new JLabel(broomIcon);
            iconLabel.setBounds(0, 0, 100, 100);
            broomPanel.add(iconLabel);

            // Название и цена
            JLabel label = new JLabel(broom.name + " — " + broom.cost + " очков");
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(Color.BLACK);
            label.setBounds(110, 10, 250, 30);
            broomPanel.add(label);

            // Кнопка выбора
            JButton button = new JButton();
            button.setBounds(110, 50, 200, 30);

            broomButtons.put(broom, button);


            if (broom.name().equals(selected)) {
                button.setText("Выбрана");
                button.setEnabled(false);
            } else {
                button.setText("Выбрать");
            }

            button.addActionListener(e -> {
                if (totalPoints >= broom.cost) {
                    App.getProxy().setSelectedBroom(broom.name());
                    updateBroomButtons(broom.name()); //
                } else {
                    button.setText("Недостаточно очков");
                    button.setEnabled(false);
                }
            });



            broomPanel.add(button);
            panel.add(broomPanel);
            y += 140; // расстояние между блоками
        }
    }

    private void updateBroomButtons(String selectedBroom) {
        for (Broom broom : broomButtons.keySet()) {
            JButton btn = broomButtons.get(broom);
            if (broom.name().equals(selectedBroom)) {
                btn.setText("Выбрана");
                btn.setEnabled(false);
            } else {
                btn.setText("Выбрать");
                btn.setEnabled(true);
            }
        }
    }

}
