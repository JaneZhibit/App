package play.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.function.Consumer;

public class BossFightController extends JComponent {
    private final String[] keys = {"u", "i", "j", "p", "m"}; // кнопки для игры
    private String currentKey;
    private int progress = 35;
    private final int maxProgress = 100;
    private Timer lowerProgressTimer;
    private final Consumer<Boolean> resultCallback;
    private final Random random = new Random();

    public BossFightController(Consumer<Boolean> resultCallback ) {
        this.resultCallback = resultCallback;

        // фокусировка
        setFocusable(true);
        setRequestFocusEnabled(true);
        setOpaque(false); // отключение непрозрачности
        setBounds(0, 0, 1280, 720);

        chooseNextKey(); // выбирает клавишу для нажатия

        // Уменьшение прогресса
        lowerProgressTimer = new Timer(100, e -> {
            if (progress > 0) {
                progress -= 2;
                repaint();
            } else {
                lowerProgressTimer.stop();
                resultCallback.accept(false);
            }
        });
        lowerProgressTimer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
                handleKeyPress(key);
            }
        });
    }

    private void chooseNextKey() {
        currentKey = keys[random.nextInt(keys.length)];
        repaint();
    }

    private void handleKeyPress(String key) {
        if (key.equalsIgnoreCase(currentKey)) {
            progress += 25;
            if (progress >= maxProgress) {
                progress = maxProgress;
                lowerProgressTimer.stop();
                resultCallback.accept(true);
            } else {
                chooseNextKey();
            }
            repaint();
        }
    }

    // Отрисовка
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Клавиша текст
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        g2.setColor(Color.WHITE);
        g2.drawString("Нажми: " + currentKey.toUpperCase(), getWidth() / 2 - 100, getHeight() / 2);

        // Прогресс-бар
        int barWidth = 400;
        int barHeight = 30;
        int barX = getWidth() / 2 - barWidth / 2;
        int barY = getHeight() / 2 + 50;

        g2.setColor(Color.GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        // заполняет зеленым по размеру progress
        g2.setColor(Color.GREEN);
        g2.fillRect(barX, barY, (int)((progress / (double) maxProgress) * barWidth), barHeight);
    }
}
