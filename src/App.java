//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class App extends JFrame {
    App() {
        super("My first App");
        this.setDefaultCloseOperation(3);
        this.setSize(800, 1000);
        this.setLayout((LayoutManager)null);
        JLabel label = new JLabel("Полный балл?");
        label.setBounds(250, 300, 160, 30);
        label.setFont(new Font("Arial", 1, 20));
        //label.setBackground(Color.BLUE);
        label.setForeground(Color.BLUE);
        //label.setOpaque(true);

        this.add(label);

        //JLabel pic = new JLabel(new ImageIcon("src/pics/pic1.png"));
        //pic.setBounds(200, 300, 500, 220);

        //this.add(pic);

        //JButton button = new JButton("click me");
        JButton yesbutton = new JButton("Да!!!");
        yesbutton.setBounds(200, 400, 200, 60);
        this.add(yesbutton);

        JButton nobutton = new JButton("Нет, есть недочёты");
        nobutton.setBounds(400, 400, 200, 60);
        this.add(nobutton);

        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(300, 500, 200, 30);
        messageLabel.setForeground(Color.BLUE);
        messageLabel.setFont(new Font("Arial", 1, 20));

        this.add(messageLabel);


        // Создание слушателя для yesbutton
        MyButtonListener listenerYes = new MyButtonListener(messageLabel, "Урааа!");
        yesbutton.addActionListener(listenerYes);

        // Создание слушателя для nobutton
        MyButtonListener listenerNo = new MyButtonListener(messageLabel, "Ладно, все плохо");
        nobutton.addActionListener(listenerNo);

        Mouse mouse = new Mouse(nobutton);
        this.addMouseListener(mouse);

        //JTextField inputField = new JTextField();
        //inputField.setBounds(200, 20, 160, 30);

        //MyButtonListener listener = new MyButtonListener(pic, 10, 10);


        //this.add(inputField);

//        MyTextListener textListener = new MyTextListener(inputField, label);
//        button.addActionListener(textListener);
//        inputField.addActionListener(textListener);
        //KeyBoard keyBoard = new KeyBoard(pic);
        //inputField.addKeyListener(keyBoard);
        //button.addKeyListener(keyBoard);
        //Mouse mouse = new Mouse(pic);
        //this.addMouseListener(mouse);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}
