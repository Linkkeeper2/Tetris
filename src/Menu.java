import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Menu {
    protected ArrayList<Button> buttons;
    protected ArrayList<Text> text;

    public Menu() {}

    public void draw(Graphics pen) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).draw(pen);
        }

        for (int i = 0; i < text.size(); i++) {
            text.get(i).draw(pen);
        }
    }

    public void buttonInteractions(MouseEvent me) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).hover(me);
            buttons.get(i).click(me);
        }
    }

    public class Button {
        private Rectangle rect;
        private Color color;
        private Color defaultColor;
        private Color hoverColor;
        private String contents;
        private ButtonAction action;

        public Button(int x, int y, int width, int height, Color color, Color hoverColor, String contents, ButtonAction action) {
            rect = new Rectangle(x, y, width, height);
            this.color = color;
            this.defaultColor = color;
            this.hoverColor = hoverColor;
            this.contents = contents;
            this.action = action;
        }

        public void draw(Graphics pen) {
            pen.setColor(color);
            pen.fillRect(rect.x, rect.y, rect.width, rect.height);
            pen.setColor(Color.WHITE);
            pen.setFont(new Font("comicsansms", 0, 20));

            Graphics2D g2d = (Graphics2D) pen;
            FontMetrics fm = g2d.getFontMetrics();

            int sum = 0;

            for (int i = 0; i < contents.length(); i++) {
                sum += fm.charWidth(contents.charAt(i));
            }

            int x = rect.x + ((int)rect.getWidth() - (int) rect.getWidth() / 2) - sum / 2;
            int y = rect.y + ((int)rect.getHeight() - (int) rect.getHeight() / 2) + fm.getAscent() / 2 - 4;
            g2d.drawString(contents, x, y);
        }
    
        public void hover(MouseEvent me) {
            if (rect.intersects(new Rectangle(me.getX() - 8, me.getY() - 32, 1, 1))) color = hoverColor;
            else color = defaultColor;
        }
    
        public void click(MouseEvent me) {
            if (rect.intersects(new Rectangle(me.getX() - 8, me.getY() - 32, 1, 1))) {
                color = hoverColor;
                if (this.action != null) this.action.action();
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
