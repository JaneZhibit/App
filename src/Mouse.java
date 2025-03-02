import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintStream;
import javax.swing.*;
import java.util.Random;

public class Mouse implements MouseListener {
    JLabel pic;
    private JButton nobutton;

//    Mouse(JLabel pic) {
//        this.pic = pic;
//    }

    public Mouse(JButton nobutton) {
        this.nobutton = nobutton;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        PrintStream var10000 = System.out;
        int var10001 = e.getButton();
        var10000.println(var10001 + " " + e.getX() + " " + e.getXOnScreen());
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
            Random rand = new Random();
            int newX = rand.nextInt(300);
            int newY = rand.nextInt(300);
            nobutton.setLocation(newX, newY);

    }

    public void mouseExited(MouseEvent e) {
        //this.pic.setVisible(false);
        Random rand = new Random();
        int newX = rand.nextInt(300);
        int newY = rand.nextInt(300);
        nobutton.setLocation(newX, newY);
    }
}
