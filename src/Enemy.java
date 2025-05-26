import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {
    private JLabel enemyLabel;
    private boolean hit = false;
    private int speed;

    private static final int PANEL_HEIGHT = App.getProxy().h;
    private static final int PANEL_WIDTH = App.getProxy().w;

    public Enemy(int speed) {
        this.speed = speed;
        ImageIcon icon = new ImageIcon("src/pics/play/enemy.png");
        enemyLabel = new JLabel(icon);

        Random rand = new Random();
        int x = PANEL_WIDTH + rand.nextInt(300);
        int y = rand.nextInt(PANEL_HEIGHT - icon.getIconHeight() - 50);
        enemyLabel.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
    }

    public void move() {
        if (!hit) {
            Rectangle bounds = enemyLabel.getBounds();
            bounds.x -= speed;
            enemyLabel.setBounds(bounds);
        }
    }

    public boolean isOutOfScreen() {
        return enemyLabel.getX() + enemyLabel.getWidth() < 0;
    }

    public JLabel getLabel() {
        return enemyLabel;
    }

    public boolean isHit() {
        return hit;
    }

    public void hit() {
        hit = true;
        enemyLabel.setVisible(false);
    }

    public Rectangle getBounds() {
        return enemyLabel.getBounds();
    }
}
