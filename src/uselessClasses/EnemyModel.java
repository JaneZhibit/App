import java.util.Random;

public class EnemyModel {
    private int x, y, speed;
    protected int height = 117, width = 100;
    private boolean alive = true;
    private PlayersPhysics player;

    public EnemyModel(int panelWidth, int panelHeight) {
        Random rand = new Random();
        this.x = panelWidth; // Спавн справа
        this.y = rand.nextInt(panelHeight - height); // Случайная позиция по Y
        this.speed = rand.nextInt(8) + 6; // Скорость от 6 до 13 пикселей за тик
    }

    // шар движется
    public void move() {
        Random rand = new Random();
        // хаотичное по y
        y += rand.nextInt(1) - 1;
        x -= speed;
        if (x + width < 0) { // Если враг ушел за левый край экрана
            alive = false;
        }
    }

    public boolean checkCollision() {
        int playerX = player.getPlayer().getX();
        int playerY = player.getPlayer().getY();
        int playerW = player.getPlayer().getWidth();
        int playerH = player.getPlayer().getHeight();

        int enemyX = getX();
        int enemyY = getY();

        return enemyX < playerX + playerW && enemyX + width > playerX &&
                enemyY < playerY + playerH && enemyY + height > playerY;
    }


    public boolean isAlive() { return alive; }
    public int getX() { return x; }
    public int getY() { return y; }
}
