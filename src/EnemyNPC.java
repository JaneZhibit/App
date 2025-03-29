import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class EnemyNPC extends JLabel implements Runnable {
    private int speed;
    private int panelWidth;
    private int panelHeight;
    private PlayersPhysics player;
    private Play play;
    private boolean running = true;

    public EnemyNPC(String imagePath, int panelWidth, int panelHeight, PlayersPhysics player, Play play) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.player = player;
        this.play = play;

        setIcon(new ImageIcon(imagePath));
        setSize(100, 117); // Размер NPC (можно изменить)

        Random rand = new Random();
        int startY = rand.nextInt(panelHeight - getHeight()); // Случайная позиция по Y
        setLocation(panelWidth, startY); // Появляются справа

        this.speed = rand.nextInt(8) + 6; // Скорость от 3 до 7 пикселей за тик
    }

    private boolean checkCollision(EnemyNPC enemy) {
        int playerX = player.getPlayer().getX();
        int playerY = player.getPlayer().getY();
        int playerHeight = player.getPlayer().getHeight() / 3;

        int enemyX = enemy.getX();
        int enemyY = enemy.getY();
        int enemyHeight = enemy.getHeight();

        // Если X врага попадает в зону игрока, проверяем только Y
        if (enemyX <= playerX + player.getPlayer().getWidth() / 2 && enemyX + enemy.getWidth() >= playerX) {
            return (enemyY + enemyHeight >= playerY && enemyY <= playerY + playerHeight);
        }
        return false;
    }


    @Override
    public void run() {

        while (running) {
            int x = getX() - speed;
            if (x + getWidth() < 0) {
                running = false;
                play.removeEnemy(this);
                return;
            }

            setLocation(x, getY());

            // Проверка столкновения с игроком
            if (checkCollision(this)) {
                play.reducePlayerHealth();
                running = false;
                play.removeEnemy(this);
                return;
            }

            try {
                Thread.sleep(30); // Скорость обновления движения
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}
