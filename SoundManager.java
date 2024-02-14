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
    public static long loopTime = (4 * 60 + 52) * 1000;
    private static ArrayList<Long> loopTimes = new ArrayList<>();

    public static void playSound(String path, boolean loop) {
        try {
            paths.add(path);
            music.add(new FileInputStream(new File(path)));
            audios.add(new AudioStream(music.get(music.size() - 1)));

            if (loop) {
                audioLoop = audios.get(audios.size() - 1);
                loopTimes.add(loopTime);

                MyGame.timer.schedule(new TimerTask() {
                    public void run() {
                        loopSound();
                    }
                }, loopTime);
            }

            AudioPlayer.player.start(audios.get(audios.size() - 1));
        } catch (Exception e) {
            //e.getStackTrace();
        }
    }

    public static void stopAllSounds() {
        try {
            for (int i = 0; i < audios.size(); i++) {
                if (i < audios.size()) AudioPlayer.player.stop(audios.get(i));
            }
    
            music.clear();
            audios.clear();
            paths.clear();
        } catch (Exception e) {}
    }

    public static void stopSound(String path) {
        try {
            AudioPlayer.player.stop(audios.get(paths.indexOf(path)));
        } catch (Exception e) {}
    }

    private static void loopSound() {
        if (MyGame.menu != null) return;

        try {
            if (loopTimes.get(0) != loopTime) {
                loopTimes.remove(0);
                return;
            }

            AudioPlayer.player.stop(audioLoop);
            music.add(new FileInputStream(new File(paths.get(audios.indexOf(audioLoop)))));
            audios.remove(audioLoop);
            audios.add(new AudioStream(music.get(music.size() - 1)));
            AudioPlayer.player.start(audios.get(audios.size() - 1));
            audioLoop = audios.get(audios.size() - 1);
            loopTimes.remove(0);

            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    loopSound();
                }
            }, loopTime);
        } catch (Exception e) {}
    }
}