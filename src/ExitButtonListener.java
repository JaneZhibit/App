import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonListener implements ActionListener {
    private JLabel goodbyeLabel;
    private static JFrame frame = App.getProxy().getFrame();

    public ExitButtonListener(JLabel goodbyeLabel) {
        this.goodbyeLabel = goodbyeLabel;
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
