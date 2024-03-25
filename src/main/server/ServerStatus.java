package src.main.server;

import java.util.ArrayList;
import java.util.TimerTask;

import src.main.MyGame;
import src.main.screens.Menus;
import src.main.screens.scaffolds.Menu;

import java.awt.Color;
import java.awt.Graphics;

public class ServerStatus {
    public ArrayList<Menu.Text> messages;
    public ArrayList<Menu.Text> results;

    public ServerStatus() {
        messages = new ArrayList<Menu.Text>();
        results = new ArrayList<Menu.Text>();
    }

    public void addMessage(String val) {
        Menu.Text t;

        if (MyGame.menu == null) {
            t = new Menu().new Text(val, MyGame.offset + 10 * 25 + 4, MyGame.offset + 180, Color.WHITE);
        } else {
            t = new Menu().new Text(val, MyGame.SCREEN_WIDTH / 2 + 80, 200, Color.WHITE);
        }

        messages.add(0, t);

        try {
            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    if (t != null)
                        messages.remove(t);
                }
            }, (long) 1500);
        } catch (IllegalStateException e) {
        }
    }

    public void addMessage(String val, int timeAlive) {
        Menu.Text t;
        if (MyGame.menu == null) {
            t = new Menu().new Text(val, MyGame.offset + 10 * 25 + 4, MyGame.offset + 180, Color.WHITE);
        } else {
            t = new Menu().new Text(val, MyGame.SCREEN_WIDTH / 2 + 80, 200, Color.WHITE);
        }

        if (messages != null)
            messages.add(0, t);

        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                if (t != null)
                    messages.remove(t);
            }
        }, (long) timeAlive);
    }

    public void draw(Graphics pen) {
        if (messages == null)
            messages = new ArrayList<>();
        if (results == null)
            results = new ArrayList<>();

        for (int i = 0; i < messages.size(); i++) {
            if (MyGame.menu == null) {
                if (messages.get(i) == null)
                    continue;
                messages.get(i).x = MyGame.offset + 10 * 25 + 25;
                messages.get(i).y = MyGame.offset + 180 + (i * 25);
            } else {
                if (messages.get(i) == null)
                    continue;

                Menu.Text msg = messages.get(i);

                if (MyGame.client != null)
                    msg.x = MyGame.SCREEN_WIDTH / 2 + 125;
                else
                    msg.x = 0;

                msg.y = 200 + (i * 25);
            }
            messages.get(i).draw(pen);
        }

        for (int i = 0; i < results.size(); i++) {
            Menu.Text result = results.get(i);

            if (MyGame.menu instanceof Menus.ResultsMenu) {
                result.x = MyGame.SCREEN_WIDTH / 2 - 75;
                result.y = 48 + (i * 48);
                result.draw(pen);
            }
        }
    }

    public void updateStatus(ArrayList<Menu.Text> msgs) {
        messages = msgs;
    }
}
