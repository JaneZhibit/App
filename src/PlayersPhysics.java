import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayersPhysics {
    private static PlayersPhysics proxy;
    private JLabel player;
    private ImageIcon originalIcon;
    private int h = App.getProxy().h;
    private int w = App.getProxy().w;

    private double angle = 0; // Текущий угол
    private double targetAngle = 0; // Желаемый угол
    private final double angleStep = 0.035; // Шаг изменения угла
    private final double maxAngle = 0.4; // Максимальный угол

    private int baseSpeed = 0; // Базовая скорость (потом поиграемся)
    private final double speedMultiplier = 40; // Чем круче угол, тем выше скорость

    private Timer rotationTimer; // Таймер для плавного изменения угла

    private PlayersPhysics() {
        initPlayer();

        // Таймер для плавного изменения угла
        rotationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRotation();
            }
        });
        rotationTimer.start();
    }

    private void initPlayer() {
        originalIcon = new ImageIcon("src/pics/player_min.png");
        player = new JLabel(originalIcon);
        int playerX = w / 30;
        int playerY = h / 3;
        int playerWidth = 336 + 336/3;
        int playerHeight = 180 + 180/3;
        player.setBounds(playerX, playerY, playerWidth, playerHeight);
    }

    public JLabel getPlayer() {
        return player;
    }

    public void moveUp() {
        targetAngle = -maxAngle; // Устанавливаем целевой угол
    }

    public void moveDown() {
        targetAngle = maxAngle; // Устанавливаем целевой угол
    }

    public void keyReleased() {
        targetAngle = 0; // Возвращаем в горизонтальное положение
    }

    private void movePlayer(int deltaY) {
        int newY = player.getY() + deltaY;

        // Проверяем границы
        if (newY <= 5) {
            newY = 5;
        }
        if (newY >= h - 230) {
            newY = h - 230;
        }

        player.setLocation(player.getX(), newY);
    }

    private void updateRotation() {
        if (angle < targetAngle) {
            angle = Math.min(angle + angleStep, targetAngle);
        } else if (angle > targetAngle) {
            angle = Math.max(angle - angleStep, targetAngle);
        }

        // Обновляем изображение
        player.setIcon(ImageUtils.rotateImage(originalIcon, angle));

        // Рассчитываем скорость и перемещаем игрока
        int speed = (int) (baseSpeed + Math.abs(angle) * speedMultiplier);
        if (angle < 0) {
            movePlayer(-speed);
        } else if (angle > 0) {
            movePlayer(speed);
        }
    }

    public static PlayersPhysics getProxy() {
        if (proxy == null) {
            proxy = new PlayersPhysics();
        }
        return proxy;
    }
}