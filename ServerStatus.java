import java.util.ArrayList;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Graphics;

public class ServerStatus {
    public ArrayList<Menu.Text> messages;

    public ServerStatus() {
        messages = new ArrayList<Menu.Text>();
    }

    public void addMessage(String val) {
        Menu.Text t = new Menu().new Text(val, MyGame.offset + MyGame.board[0].length * 25 + 48, MyGame.offset + 180, Color.WHITE);
        messages.add(0, t);
    
        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                messages.remove(t);
            }
        }, (long)1500);
    }

    public void draw(Graphics pen) {
        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).y = MyGame.offset + 180 + (i * 25);
            messages.get(i).draw(pen);
        }
    }
}
