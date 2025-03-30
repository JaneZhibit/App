import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ImageUtils {
    public static ImageIcon rotateImage(ImageIcon icon, double angle) {
        Image img = icon.getImage();
        // для корректных манипуляций с поворотом изображение увеличивается до 4/3 от полного
        int w = img.getWidth(null) + img.getWidth(null)/3;
        int h = img.getHeight(null) + img.getHeight(null)/3;

        BufferedImage rotatedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        AffineTransform at = new AffineTransform();
        at.rotate(angle, w / 2.0, h / 2.0);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(rotatedImage);
    }

    // возможно будут ещё функции для работы с изображениями
}