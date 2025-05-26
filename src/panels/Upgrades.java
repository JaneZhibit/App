package panels;

import javax.swing.*;
import java.awt.*;

import java.util.HashSet;
import java.util.Set;

import app.App;
import panels.upgrData.Broom;


public class Upgrades {
    private static Upgrades proxy;
    private JPanel panel;
    private final int w = App.getProxy().w, h = App.getProxy().h;
    private int totalPoints;
    private JLabel pointsLabel;
    private java.util.Map<Broom, JButton> broomButtons = new java.util.HashMap<>();
    private Set<String> purchasedBrooms = new HashSet<>(); // для защиты от повторного добавления


    public Upgrades() {
        initPanel();
        initBackButton();
        initPointsLabel();
        initBroomChoices();
        loadPurchasedBrooms();
        addBackground();
    }

    // Загружаем список купленных метел из конфига
    private void loadPurchasedBrooms() {
        String purchased = App.getProxy().getConfig().getProperty("purchasedBrooms", "FIREBOLT");
        String[] broomNames = purchased.split(",");
        for (String name : broomNames) {
            if (!name.isEmpty()) {
                purchasedBrooms.add(name);
            }
        }
    }
    // Сохраняем список купленных метел в конфиг
    private void savePurchasedBrooms() {
        StringBuilder sb = new StringBuilder();
        for (String broom : purchasedBrooms) {
            sb.append(broom).append(",");
        }
        App.getProxy().getConfig().setProperty("purchasedBrooms", sb.toString());
        App.getProxy().saveConfig();
    }


    public void refresh() {
        totalPoints = getTotalPoints();
        updateBroomButtons(App.getProxy().getSelectedBroomName());
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
                boolean isPurchased = purchasedBrooms.contains(broom.name());
                button.setText(isPurchased ? "Выбрать" : "Купить за " + broom.cost);
            }

            button.addActionListener(e -> {
                boolean isPurchased = purchasedBrooms.contains(broom.name());

                if (isPurchased || totalPoints >= broom.cost) {
                    if (!isPurchased) {
                        // Если метла не куплена, списываем очки
                        totalPoints -= broom.cost;
                        purchasedBrooms.add(broom.name());
                        App.getProxy().getConfig().setProperty("totalPoints", String.valueOf(totalPoints));
                        savePurchasedBrooms();
                    }

                    App.getProxy().setSelectedBroom(broom.name());
                    updateBroomButtons(broom.name());
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
        totalPoints = getTotalPoints();
        pointsLabel.setText("Всего очков: " + totalPoints);

        for (Broom broom : broomButtons.keySet()) {
            JButton btn = broomButtons.get(broom);
            boolean isPurchased = purchasedBrooms.contains(broom.name());

            if (broom.name().equals(selectedBroom)) {
                btn.setText("Выбрана");
                btn.setEnabled(false);
                btn.setBackground(new Color(200, 255, 200));
            } else {
                if (isPurchased) {
                    btn.setText("Выбрать");
                    btn.setEnabled(true);
                    btn.setBackground(new Color(220, 220, 255));
                } else {
                    boolean canAfford = totalPoints >= broom.cost;
                    btn.setText(canAfford ? "Купить за " + broom.cost : "Недостаточно очков");
                    btn.setEnabled(canAfford);
                    btn.setBackground(canAfford ? new Color(220, 220, 255) : new Color(255, 200, 200));
                }
            }
        }
    }

}
