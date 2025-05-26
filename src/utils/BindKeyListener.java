package utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BindKeyListener implements ActionListener {

    private JButton button;
    public BindKeyListener(JButton button){
        this.button = button;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        button.setText("...");
        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyCode = KeyEvent.getKeyText(e.getKeyCode());
                button.setText(keyCode);
            }
        });
    }
}
