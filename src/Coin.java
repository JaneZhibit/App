import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Coin {
    private JLabel coinLabel;
    private boolean collected = false;
    private int speed = 6;


    private static final int PANEL_HEIGHT = App.getProxy().h;
    private static final int PANEL_WIDTH = App.getProxy().w;

    public Coin() {
        ImageIcon icon = new ImageIcon("src/pics/play/coin.png");
        coinLabel = new JLabel(icon);

        Random rand = new Random();
        int x = PANEL_WIDTH + rand.nextInt(300);
        int y = rand.nextInt(PANEL_HEIGHT - icon.getIconHeight() - 50);
        coinLabel.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
    }

    public void move() {
        if (!collected) {
            Rectangle bounds = coinLabel.getBounds();
            bounds.x -= speed;
            coinLabel.setBounds(bounds);
        }
    }

    public boolean isOutOfScreen() {
        return coinLabel.getX() + coinLabel.getWidth() < 0;
    }

    public JLabel getLabel() {
        return coinLabel;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        coinLabel.setVisible(false);
    }

    public Rectangle getBounds() {
        return coinLabel.getBounds();
    }
}