import java.io.File;
import javax.sound.sampled.*;

import java.util.ArrayList;

public class SoundManager {
    private static ArrayList<Clip> clips = new ArrayList<>();
    public static float volume = 1.0f;

    public static void playSound(String path, boolean loop) {
        try {
            File file = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clips.add(clip);
            clip.open(audioStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));

            clip.start();

            if (loop)
                clip.loop(-1);
        } catch (Exception e) {
            // e.getStackTrace();
        }
    }

    public static void stopAllSounds() {
        for (int i = 0; i < clips.size(); i++) {
            if (i < clips.size() && clips.get(i) != null)
                clips.get(i).stop();
        }

        clips.clear();
    }
}