import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

public class Chat {
    private ArrayList<Menu.Text> messages;
    public TextActions.Chat bubble;

    public Chat() {
        messages = new ArrayList<Menu.Text>();
        Menu.Text t = new Menu().new Text("Chat (/ to chat)", 0, 0, Color.WHITE);
        messages.add(t);
    }

    public void addMessage(String s) {
        Menu.Text t = new Menu().new Text(s, 0, 0, Color.WHITE);
        messages.add(1, t);
    }

    public void draw(Graphics pen) {
        for (int i = 0; i < messages.size(); i++) {
            if (MyGame.menu == null) {
                if (messages.get(i) == null) continue;
                messages.get(i).x = MyGame.offset + 10 * 25 + 300;
                messages.get(i).y = MyGame.offset + i * 25;
                messages.get(i).draw(pen);
            }
        }
    }

    public void openChat() {
        if (MyGame.client == null) return;
        if (MyGame.menu != null) return;
        bubble = new TextActions().new Chat();
        bubble.action();
    }
}