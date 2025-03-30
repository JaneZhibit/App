import javax.swing.*;
import java.awt.*;

public class ParallaxBG extends JPanel implements Runnable {
    private Image[] layers;
    private int[] xPositions;
    private int[] yPositions;
    private int[] layerWidths;
    private int[] layerHeights;
    private int[] speeds;

    private Thread animationThread;
    private volatile boolean running = true;

    public ParallaxBG(String[] imagePaths, int[] speeds, int[] yPositions,
                      int[] layerWidths, int[] layerHeights, int panelWidth, int panelHeight) {
        int layerCount = imagePaths.length;
        layers = new Image[layerCount];
        xPositions = new int[layerCount];
        this.yPositions = yPositions;
        this.layerWidths = layerWidths;
        this.layerHeights = layerHeights;
        this.speeds = speeds;

        for (int i = 0; i < layerCount; i++) {
            layers[i] = new ImageIcon(imagePaths[i]).getImage();
            xPositions[i] = 0; // Начальная позиция X всегда 0
        }

        setOpaque(false);
        setBounds(0, 0, panelWidth, panelHeight);

        animationThread = new Thread(this);
        animationThread.start();
    }

    @Override
    public void run() {
        while (running) {
            moveBackground();
            repaint();
            try {
                Thread.sleep(30); // Регулировка скорости анимации
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void moveBackground() {
        for (int i = 0; i < layers.length; i++) {
            xPositions[i] -= speeds[i];

            // Зацикливание фона
            if (xPositions[i] <= -layerWidths[i]) {
                xPositions[i] += layerWidths[i];
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < layers.length; i++) {
            g.drawImage(layers[i], xPositions[i], yPositions[i], layerWidths[i], layerHeights[i], null);
            g.drawImage(layers[i], xPositions[i] + layerWidths[i], yPositions[i], layerWidths[i], layerHeights[i], null);
        }
    }

    public void stop() {
        running = false;
        try {
            if (animationThread != null) {
                animationThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
