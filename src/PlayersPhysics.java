import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PlayersPhysics {
    private static PlayersPhysics proxy;
    private JLabel player;
    private int h = App.getProxy().h;
    private int w = App.getProxy().w;

    private PlayersPhysics(){
        initPlayer();
    }

    private void initPlayer() {
        player = new JLabel(new ImageIcon("src/pics/player.png"));
        int playerX = w / 30;
        int playerY = h / 3;
        int playerWidth = 420;
        int playerHeight = 225;
        player.setBounds(playerX, playerY, playerWidth, playerHeight);
    }

    public JLabel getPlayer(){
        return player;
    }

    public void moveUp(){
        movePlayer(-15);
    }

    public void moveDown(){
        movePlayer(15);
    }


    public void movePlayer(int deltaY) {
        int newY = player.getY() + deltaY;
        if (newY >= 10 && newY <= h - 260) {
            player.setLocation(player.getX(), newY);
        }
    }

    public static PlayersPhysics getProxy() {
        if (proxy == null) {
            proxy = new PlayersPhysics();
        }
        return proxy;
    }


}
