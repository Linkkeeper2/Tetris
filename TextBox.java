import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class TextBox {
    private String title;
    private String contents;

    public TextBox(String title) {
        this.title = title;
        this.contents = "";
    }

    public void draw(Graphics pen) {
        Graphics2D g2d = (Graphics2D) pen;
        FontMetrics fm = g2d.getFontMetrics();

        // Title
        int sum = 0;
        for (int i = 0; i < title.length(); i++)
            sum += fm.charWidth(title.charAt(i));

        int width = MyGame.SCREEN_WIDTH;
        int x = (width / 2 - 75) + ((int)150 - (int) 150 / 2) - sum / 2;
        int y = MyGame.SCREEN_HEIGHT - 150;
        g2d.drawString(title, x, y);
        
        // Contents
        sum = 0;
        for (int i = 0; i < title.length(); i++)
            sum += fm.charWidth(title.charAt(i));

        x = (width / 2 - 75) + ((int)150 - (int) 150 / 2) - sum / 2;

        g2d.drawString(contents, x, y + 30);
    }

    public void type(KeyEvent ke) {
        String charac = ke.getKeyChar() + "";
        if (charac.equals(" ")) {
            if (contents.length() > 0) contents = contents.substring(0, contents.length() - 1);
        } else {
            contents += charac + "";
        }
    }
}