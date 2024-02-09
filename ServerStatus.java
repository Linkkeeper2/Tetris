import java.util.ArrayList;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Graphics;

public class ServerStatus {
    private ArrayList<Menu.Text> messages;

    public ServerStatus() {
        messages = new ArrayList<Menu.Text>();
    }

    public void addMessage(String val) {
        Menu.Text t;
        if (MyGame.menu == null) {
            t = new Menu().new Text(val, MyGame.offset + 10 * 25 + 4, MyGame.offset + 180, Color.WHITE);
        } else {
            t = new Menu().new Text(val, MyGame.SCREEN_WIDTH / 2 + 80, 200, Color.WHITE);
        }
        
        messages.add(0, t);
    
        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                messages.remove(t);
            }
        }, (long)1500);
    }

    public void addMessage(String val, int timeAlive) {
        Menu.Text t;
        if (MyGame.menu == null) {
            t = new Menu().new Text(val, MyGame.offset + 10 * 25 + 4, MyGame.offset + 180, Color.WHITE);
        } else {
            t = new Menu().new Text(val, MyGame.SCREEN_WIDTH / 2 + 80, 200, Color.WHITE);
        }
        
        messages.add(0, t);
    
        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                messages.remove(t);
            }
        }, (long)timeAlive);
    }

    public void draw(Graphics pen) {
        for (int i = 0; i < messages.size(); i++) {
            if (MyGame.menu == null) {
                if (messages.get(i) == null) continue;
                messages.get(i).x = MyGame.offset + 10 * 25 + 25;
                messages.get(i).y = MyGame.offset + 180 + (i * 25);
            }
            else {
                if (messages.get(i) == null) continue;
                messages.get(i).x = MyGame.SCREEN_WIDTH / 2 + 88;
                messages.get(i).y = 200 + (i * 25);
            }
            messages.get(i).draw(pen);
        }
    }
}
