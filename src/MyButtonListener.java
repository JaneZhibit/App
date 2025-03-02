import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

public class MyButtonListener implements ActionListener {
    private JLabel messageLabel;
    private String message;

    public MyButtonListener(JLabel messageLabel, String message) {
        this.messageLabel = messageLabel;
        this.message = message;
    }

    public void actionPerformed(ActionEvent e) {
        this.messageLabel.setText(this.message);
    }
}
