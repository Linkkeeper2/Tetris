import java.awt.Color;
import java.awt.Graphics;

public class Message {
    private Menu.Text text;
    public int x, y;

    public Message(String contents) {
        this.text = new Menu().new Text(contents, x, y, Color.WHITE);
    }

    public void draw(Graphics pen) {
        text.draw(pen);
    }

    public void updatePosition() {
        this.text.x = this.x;
        this.text.y = this.y;
    }
}
