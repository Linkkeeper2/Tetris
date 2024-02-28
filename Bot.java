import java.util.TimerTask;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Bot {
    private Client client;
    public int linesToSend = 0;
    public int topRow = 20;
    public String name;

    public Bot() {
        try {
            client = new Client(InetAddress.getLocalHost().getHostAddress(), 2500);
        } catch (UnknownHostException e) {
            MyGame.status.addMessage("Failed to add bot.");
        }

        name = "Bot" + MyGame.bots.size();
        client.name = name;
        client.output.println(name + " has connected.");

        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                determineLines();
            }
        }, (long)2000);
    }

    public void sendLines() {
        String recieve = MyGame.client.lobby.get((int)(Math.random() * client.lobby.size())).contents;
        while (recieve.equals(name) || recieve.equals("Lobby")) {
            recieve = MyGame.client.lobby.get((int)(Math.random() * MyGame.client.lobby.size())).contents;
        }
        
        client.output.println(client.name + " sent " + linesToSend + " lines to " + recieve);
    }

    public void determineLines() {
        if (MyGame.menu != null) {
            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    determineLines();
                }
            }, (long)(Math.random() * 1000) + 1500);
            return;
        }

        if ((int)(Math.random() * 10) >= 8) {
            linesToSend = (int)(Math.random() * 4) + 1;
            if (linesToSend == 0) linesToSend = 1;
            sendLines();
        }

        if (topRow <= 0) {
            client.output.println(client.name + " has topped out.");
            client.deaths++;
            topRow = 20;
        }

        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                determineLines();
            }
        }, (long)(Math.random() * 1000) + 1500);
    }
}