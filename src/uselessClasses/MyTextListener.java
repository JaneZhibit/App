package uselessClasses;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MyTextListener implements ActionListener {
    private JTextField input;
    private JLabel output;

    public MyTextListener(JTextField in, JLabel out) {
        this.input = in;
        this.output = out;
    }

    public void actionPerformed(ActionEvent e) {
        this.output.setText(this.input.getText());
        this.input.setText("");
    }
}
