import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class UpgradesButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        PrintStream var10000 = System.out;
        var10000.println("Нажата кнопка улучшения");
    }
}
