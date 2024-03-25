package src.main.screens;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import src.main.MyGame;

import java.awt.Color;

public class TextBox {
    private String title;
    private String contents;
    public String send; // The value sent when the user is done typing

    public TextBox(String title) {
        this.title = title;
        this.contents = "";
        this.send = "";
    }

    public void draw(Graphics pen) {
        Graphics2D g2d = (Graphics2D) pen;
        FontMetrics fm = g2d.getFontMetrics();

        // Title
        int sum = 0;
        for (int i = 0; i < title.length(); i++)
            sum += fm.charWidth(title.charAt(i));

        int width = MyGame.SCREEN_WIDTH;
        int x = (width / 2 - 75) + ((int) 150 - (int) 150 / 2) - sum / 2;
        int y = MyGame.SCREEN_HEIGHT - 150;
        pen.setColor(Color.WHITE);
        g2d.drawString(title, x, y);

        // Contents
        sum = 0;
        for (int i = 0; i < title.length(); i++)
            sum += fm.charWidth(title.charAt(i));

        x = (width / 2 - 75) + ((int) 150 - (int) 150 / 2) - sum / 2;

        g2d.drawString(contents, x, y + 30);
        pen.setColor(Color.GRAY);
        pen.fillRect(x, y + 30, sum, fm.getAscent());
    }

    public void type(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case 8: // BACKSPACE
                if (contents.length() > 0)
                    contents = contents.substring(0, contents.length() - 1);
                break;

            case 10: // Enter
                send = contents;
                break;

            case 32:
                contents += " ";
                break;
        }

        // Alphanumeric Characters
        if (ke.getKeyCode() >= 44 && ke.getKeyCode() <= 111) {
            contents += ke.getKeyChar() + "";
        }
    }
}