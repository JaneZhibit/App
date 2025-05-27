package play.components;

import javax.swing.*;
import java.awt.*;

public class Boss extends JComponent {
    private Image image;
    private int targetX, y;
    private int currentX;
    private Timer enterTimer;
    private Timer exitTimer;

    public Boss(int screenW, int screenH) {
        ImageIcon icon = new ImageIcon("src/pics/boss.png");
        image = icon.getImage();

        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        targetX = screenW - width - 30;
        y = screenH / 2 - height / 2;
        currentX = screenW; // начинаем за правым краем

        setBounds(0, 0, screenW, screenH); // компонент занимает весь экран (или панель)
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, currentX, y, this);
        }
    }

    public void enter() {
        enterTimer = new Timer(10, e -> {
            currentX -= 10;
            if (currentX <= targetX) {
                currentX = targetX;
                enterTimer.stop();
            }
            repaint();
        });
        enterTimer.start();
    }

    public void exit() {
        exitTimer = new Timer(10, e -> {
            currentX += 10;
            if (currentX >= getWidth()) {
                exitTimer.stop();
            }
            repaint();
        });
        exitTimer.start();
    }

}

