package src.main.server;

import src.main.MyGame;

public class Server extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                sleep(250);
                MyGame.database.readAll();
            } catch (InterruptedException e) {
                MyGame.status.addMessage("Could not read from servers.", 5000);
            }
        }
    }
}
