import javax.swing.*;

public class ScoreCounter {
    private JLabel scoreLabel; // Для отображения счета
    private int score; // Переменная для хранения счета
    private Thread scoreThread; // Поток для обновления счета
    private volatile boolean running; // Флаг для управления потоком

    public ScoreCounter(){
        // Инициализация JLabel для счета
        scoreLabel = new JLabel("Счёт: 0");
        scoreLabel.setBounds(10, 10, 100, 30); // Позиция и размер
        startScoreUpdater();
    }


    private void startScoreUpdater() {
        running = true;
        scoreThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(50); // Обновляем счет каждую секунду
                    score++; // Увеличиваем счет
                    scoreLabel.setText("Score: " + score);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
                    break; // Выход из цикла при прерывании потока
                }
            }
        });
        scoreThread.setDaemon(true); // Делаем поток демоном
        scoreThread.start(); // Запускаем поток
    }

    public void stopScoreUpdater() {
        // Устанавливаем флаг в false для остановки потока
        running = false;
        if (scoreThread != null) {
            // Прерываем поток, если он спит
            scoreThread.interrupt();
        }
    }

    public JLabel getScoreLabel(){
        return scoreLabel;
    }
}
