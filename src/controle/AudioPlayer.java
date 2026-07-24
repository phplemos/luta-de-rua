package controle;

import java.net.URL;
import javax.sound.sampled.*;

public class AudioPlayer {
    
    private static Clip bgmClip;

    public static void playSound(String filename) {
        try {
            URL url = AudioPlayer.class.getResource("/assets/audio/" + filename);
            if (url == null) {
                System.err.println("Sound file not found: " + filename);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + filename);
        }
    }

    public static void playBGM(String filename) {
        try {
            stopBGM(); // Para a música anterior antes de tocar a nova
            URL url = AudioPlayer.class.getResource("/assets/audio/" + filename);
            if (url == null) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // Toca em loop infinito
        } catch (Exception e) {
            System.err.println("Error playing BGM: " + filename);
        }
    }

    public static void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }
}
