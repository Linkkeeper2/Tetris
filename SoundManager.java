import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

import java.util.ArrayList;
import java.util.TimerTask;

import sun.audio.AudioStream;
import sun.audio.AudioPlayer;

public class SoundManager {
    private static ArrayList<InputStream> music = new ArrayList<>();
    private static ArrayList<AudioStream> audios = new ArrayList<>();
    private static ArrayList<String> paths = new ArrayList<>();
    private static AudioStream audioLoop;

    public static void playSound(String path, boolean loop) {
        try {
            paths.add(path);
            music.add(new FileInputStream(new File(path)));
            audios.add(new AudioStream(music.get(music.size() - 1)));

            if (loop) {
                long loopTime = 83 * 1000;
                audioLoop = audios.get(audios.size() - 1);

                MyGame.timer.schedule(new TimerTask() {
                    public void run() {
                        loopSound();
                    }
                }, loopTime);
            }

            AudioPlayer.player.start(audios.get(audios.size() - 1));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void stopAllSounds() {
        for (int i = 0; i < audios.size(); i++) {
            if (i < audios.size()) AudioPlayer.player.stop(audios.get(i));
        }

        music.clear();
        audios.clear();
        paths.clear();
    }

    public static void stopSound(String path) {
        try {
            AudioPlayer.player.stop(audios.get(paths.indexOf(path)));
        } catch (Exception e) {}
    }

    private static void loopSound() {
        if (MyGame.menu != null) return;
        AudioPlayer.player.stop(audioLoop);

        try {
            music.add(new FileInputStream(new File(paths.get(audios.indexOf(audioLoop)))));
            audios.remove(audioLoop);
            audios.add(new AudioStream(music.get(music.size() - 1)));
        } catch (Exception e) {}

        AudioPlayer.player.start(audios.get(audios.size() - 1));
    }
}