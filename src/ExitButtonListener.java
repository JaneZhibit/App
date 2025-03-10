import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonListener implements ActionListener {private JLabel goodbyeLabel;
    private JFrame frame;

    public ExitButtonListener(JLabel goodbyeLabel, JFrame frame) {
        this.goodbyeLabel = goodbyeLabel;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        goodbyeLabel.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    }
