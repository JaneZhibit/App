//import javax.swing.*;
//import java.util.Random;
//
//public class EnemyNPC extends JLabel implements Runnable {
//    private int speed;
//    private PlayersPhysics player;
//    private Play play;
//    private boolean running = true;
//
//    public EnemyNPC(String imagePath, int panelWidth, int panelHeight, PlayersPhysics player, Play play) {
//        this.player = player;
//        this.play = play;
//
//        setIcon(new ImageIcon(imagePath));
//        setSize(100, 117); // Размер NPC (можно изменить)
//
//        Random rand = new Random();
//        int startY = rand.nextInt(panelHeight - getHeight()); // Случайная позиция по Y
//        setLocation(panelWidth, startY); // Появляются справа
//
//        this.speed = rand.nextInt(8) + 6;
//    }
//
//    private boolean checkCollision() {
//        int playerX = player.getPlayer().getX();
//        int playerY = player.getPlayer().getY();
//        int playerW = player.getPlayer().getWidth() / 3;
//        int playerH = player.getPlayer().getHeight();
//
//        int enemyX = getX();
//        int enemyY = getY();
//        int enemyW = getWidth();
//        int enemyH = getHeight();
//
//        return enemyX < playerX + playerW && enemyX + enemyW > playerX &&
//                enemyY < playerY + playerH && enemyY + enemyH > playerY;
//    }
//
//    @Override
//    public void run() {
//
//        while (running) {
//            int x = getX() - speed;
//            if (x + getWidth() < 0) {
//                running = false;
//                play.removeEnemy(this);
//                return;
//            }
//
//            setLocation(x, getY());
//
//            // Проверка столкновения с игроком
//            if (checkCollision()) {
//                play.reducePlayerHealth();
//                running = false;
//                play.removeEnemy(this);
//                return;
//            }
//
//            try {
//                Thread.sleep(30); // Скорость обновления движения
//            } catch (InterruptedException e) {
//                running = false;
//            }
//        }
//    }
//}
