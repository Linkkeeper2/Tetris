import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

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

    public void buttonInteractions(MouseEvent me) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].hover(me);
            buttons[i].click(me);
        }
    }

    public class Button {
        private Rectangle rect;
        private Color color;
        private Color defaultColor;
        private Color hoverColor;
        private String contents;

        public Button(int x, int y, int width, int height, Color color, Color hoverColor, String contents) {
            rect = new Rectangle(x, y, width, height);
            this.color = color;
            this.defaultColor = color;
            this.hoverColor = hoverColor;
            this.contents = contents;
        }

        public void draw(Graphics pen) {
            pen.setColor(color);
            pen.fillRect(rect.x, rect.y, rect.width, rect.height);
            pen.setColor(Color.WHITE);
            pen.setFont(new Font("comicsansms", 0, 20));
            pen.drawString(contents, rect.x + rect.width / 3, rect.y + rect.height / 2 + 5);
        }
    
        public void hover(MouseEvent me) {
            if (rect.intersects(new Rectangle(me.getX() - 8, me.getY() - 32, 1, 1))) color = hoverColor;
            else color = defaultColor;
        }
    
        public void click(MouseEvent me) {
            if (rect.intersects(new Rectangle(me.getX() - 8, me.getY() - 32, 1, 1))) {
                color = hoverColor;
                MyGame.startGame();
            }
            else color = defaultColor;
        }
    } 

    public class Text {
        public String contents;
        public int x;
        public int y;
        private Color color;

        public Text(String contents, int x, int y, Color color) {
            this.contents = contents;
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void draw(Graphics pen) {
            pen.setFont(new Font("comicsansms", 0, 20));
            pen.setColor(color);
            pen.drawString(contents, x, y);
        }
    }
}
