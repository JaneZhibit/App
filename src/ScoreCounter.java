import javax.swing.*;
import java.awt.*;

public class ScoreCounter {
    private JLabel scoreLabel; // Для отображения счета
    private int score; // Переменная для хранения счета
    private Thread scoreThread; // Поток для обновления счета
    private volatile boolean running; // Флаг для управления потоком

    public ScoreCounter(){
        // Инициализация JLabel для счета
        scoreLabel = new JLabel("Счёт: 0");
        scoreLabel.setBounds(10, 10, 150, 30); // Позиция и размер
        startScoreUpdater();
    }

    public int getScore(){
        return score;
    }

    private void startScoreUpdater() {
        running = true;
        scoreThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(50);
                    score++;
                    scoreLabel.setText("Score: " + score);
                    scoreLabel.setFont(new Font("Arial",1, 20));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        scoreThread.setDaemon(true);
        scoreThread.start();
    }

    public void stopScoreUpdater() {
        running = false;
        if (scoreThread != null) {
            scoreThread.interrupt();
        }
    }

    public JLabel getScoreLabel(){
        return scoreLabel;
    }

    public void add(int amount) {
        score += amount;
    }
}
