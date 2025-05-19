import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
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
    private double angleStep = 0.035; // Шаг изменения угла
    private double maxAngle = 0.4; // Максимальный угол

    private int baseSpeed = 0; // Базовая скорость (потом поиграемся)
    private double speedMultiplier = 40; // Чем круче угол, тем выше скорость

    private Timer rotationTimer; // Таймер для плавного изменения угла

    private volatile int currentFrameIndex = 0;
    private Thread animationThread;

    private int lives;
    private JLabel livesLabel;


    public PlayersPhysics(int startLives) {
        this.lives = startLives;

        String broomName = App.getProxy().getSelectedBroomName();
        Broom broom = Broom.valueOf(broomName);

        this.angleStep = broom.angleStep;
        this.maxAngle = broom.maxAngle;
        this.baseSpeed = broom.baseSpeed;
        this.speedMultiplier = broom.speedMultiplier;


        initPlayer();
        initAnimation();

        livesLabel = new JLabel("Жизни: " + lives);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        livesLabel.setForeground(Color.BLACK);
        livesLabel.setBounds(10, 40, 150, 30); // Положение на экране

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

    public boolean damage() {
        lives--;
        livesLabel.setText("Жизни: " + lives);
        return lives <= 0;
    }

    public JLabel getLivesLabel() {
        return livesLabel;
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

    public Rectangle getHitbox() {
        Rectangle bounds = player.getBounds();
        return new Rectangle(bounds.x, bounds.y,
                336, 180);
    }

    private void initAnimation() {
        ImageIcon[] animationFrames = new ImageIcon[]{
                new ImageIcon("src/pics/player/plr0.png"),
                new ImageIcon("src/pics/player/plr1.png"),
                new ImageIcon("src/pics/player/plr2.png"),
                new ImageIcon("src/pics/player/plr3.png"),
                new ImageIcon("src/pics/player/plr4.png"),
                new ImageIcon("src/pics/player/plr5.png"),
                new ImageIcon("src/pics/player/plr6.png"),
                new ImageIcon("src/pics/player/plr7.png"),
                new ImageIcon("src/pics/player/plr8.png"),
                new ImageIcon("src/pics/player/plr9.png"),
                new ImageIcon("src/pics/player/plr10.png"),
                new ImageIcon("src/pics/player/plr11.png"),
                new ImageIcon("src/pics/player/plr12.png")
        };


        originalIcon = animationFrames[0];

        animationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(200);
                    SwingUtilities.invokeLater(() -> {
                        currentFrameIndex = (currentFrameIndex + 1) % animationFrames.length;
                        originalIcon = animationFrames[currentFrameIndex];
                        updateRotation();
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        animationThread.setDaemon(true);
        animationThread.start();
    }

}