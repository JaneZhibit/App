package play.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.function.Consumer;

public class BossFightController extends JComponent {
    private final String[] keys = {"u", "i", "j", "p", "m"}; // кнопки для игры
    private String currentKey; // которую надо нажать, выбирается случайно
    private int progress = 35; // начальный прогресс
    private final int maxProgress = 100; // таргет
    private Timer lowerProgressTimer; // таймер, который понижает progress
    private final Consumer<Boolean> resultCallback; // функциональный интерфейс (почитай, что это)
    private final Random random = new Random();

    public BossFightController(Consumer<Boolean> resultCallback ) { // при вызове accept вызовется функция, которую я сюда передам из Play
        this.resultCallback = resultCallback; // сохраняется переданная функция

        // делает фокусируемым
        setFocusable(true);
        setRequestFocusEnabled(true);
        setOpaque(false); // отключение непрозрачности
        setBounds(0, 0, 1280, 720); // задает размеры на весь экран

        chooseNextKey(); // выбирает клавишу для нажатия

        // Уменьшение прогресса
        lowerProgressTimer = new Timer(100, e -> { // каждую 0,1 секунду убавляем прогресс на 1
            if (progress > 0) {
                progress--;
                repaint();
            } else {
                lowerProgressTimer.stop();
                resultCallback.accept(false); // если проиграли, то вызываем функцию с win = false
            }
        });
        lowerProgressTimer.start();

        // свой KeyListener у BossFightController. Решил не передавать из play, собственно поэтому и передаю фокусировку.
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
                if (key.equals("space")) key = "space";
                handleKeyPress(key); // функция проверки нажатия и работы с прогрессом
            }
        });
    }

    // выбирает клавишу для нажатия
    private void chooseNextKey() {
        currentKey = keys[random.nextInt(keys.length)];
        repaint();
    }

    // функция проверки нажатия и работы с прогрессом
    private void handleKeyPress(String key) {
        if (key.equalsIgnoreCase(currentKey)) {
            progress += 10;
            if (progress >= maxProgress) {
                progress = maxProgress;
                lowerProgressTimer.stop();
                resultCallback.accept(true); // если прогресса достаточно, то мы вызываем функцию с win=true
            } else {
                chooseNextKey();
            }
            repaint();
        }
    }

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
