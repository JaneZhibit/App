import javax.swing.*;

public class EnemyView extends JLabel {
    private EnemyModel model;
    public EnemyView() {
        setIcon(new ImageIcon("src/pics/enemy.png"));
        setSize(model.width, model.height);
    }

    public void updatePosition(int x, int y) {
        setLocation(x, y);
    }
}