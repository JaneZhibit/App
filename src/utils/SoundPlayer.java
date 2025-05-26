package utils;
/*
Класс удобного музыкального плеера. Зацикленное воспроизведение и разовые звуковые эффекты
Использует многопоточность
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private Clip clip;

    public SoundPlayer(String path) {
        try {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Метод для воспроизведения звука
    public void play() {
        if (clip != null) {
            new Thread(() -> {
                clip.setFramePosition(0); // Перематываем на начало
                clip.start();
            }).start();
        }
    }

    // Метод для зацикленного воспроизведения (например, для фоновой музыки)
    public void loop() {
        if (clip != null) {
            new Thread(() -> {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }).start();
        }
    }

    // Метод для остановки звука
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
