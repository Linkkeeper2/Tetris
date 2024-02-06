import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Menu {
    protected Button[] buttons;
    protected Text[] text;

    public Menu() {}

    public void draw(Graphics pen) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].draw(pen);
        }

        for (int i = 0; i < text.length; i++) {
            text[i].draw(pen);
        }
    }

    public class Button {
        private Rectangle rect;
        private Color color;

        public Button(int x, int y, int width, int height, Color color) {
            rect = new Rectangle(x, y, width, height);
            this.color = color;
        }

        public void draw(Graphics pen) {
            pen.setColor(color);
            pen.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    public class Text {
        private String contents;
        private int x;
        private int y;

        public Text(String contents, int x, int y) {
            this.contents = contents;
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics pen) {
            pen.setFont(new Font("comicsansms", 0, 20));
            pen.setColor(Color.BLACK);
            pen.drawString(contents, x, y);
        }
    }
}
