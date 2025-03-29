import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParallaxBG extends JPanel {
    private Image[] layers;
    private int[] xPositions;
    private int[] yPositions;
    private int[] layerWidths;
    private int[] layerHeights;
    private int[] speeds;
    private int panelWidth, panelHeight;

    private Timer timer;

    public ParallaxBG(String[] imagePaths, int[] speeds, int[] yPositions, int[] layerWidths, int[] layerHeights, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

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

        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBackground();
            }
        });
        timer.start();
    }

    private void moveBackground() {
        for (int i = 0; i < layers.length; i++) {
            xPositions[i] -= speeds[i];

            // Зацикливание фона
            if (xPositions[i] <= -layerWidths[i]) {
                xPositions[i] += layerWidths[i];
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < layers.length; i++) {
            g.drawImage(layers[i], xPositions[i], yPositions[i], layerWidths[i], layerHeights[i], null);
            g.drawImage(layers[i], xPositions[i] + layerWidths[i], yPositions[i], layerWidths[i], layerHeights[i], null);
        }
    }
}
