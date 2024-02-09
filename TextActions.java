import java.net.InetAddress;
import java.net.UnknownHostException;

public class TextActions {
    public class EnterName implements TextAction {
        public void action() {
            MyGame.prompt = new TextBox("Enter Your Name");
            Thread t = new NameHostThread();
            t.start();
        }
    }

    public class ConnectClient implements TextAction {
        public void action() {
            MyGame.prompt = new TextBox("Enter the host address");
            Thread t = new ConnectClientThread();
            t.start();
        }
    }

    public class NameHostThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }
            MyGame.client.name = MyGame.prompt.send;
            MyGame.client.addPlayer(MyGame.client.name);

            try {
                MyGame.status.addMessage("Hosting game on port '2500' -> " + InetAddress.getLocalHost().getHostAddress(), 3000);
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Could not host game.");
            }

            MyGame.prompt = null;
        }
    }

    public class ConnectClientThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            String host = MyGame.prompt.send;

            MyGame.prompt = new TextBox("Enter Your Name");

            while (MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            String name = MyGame.prompt.send;

            MyGame.client = new Client(host, 2500);
            MyGame.client.name = name;
            MyGame.client.addPlayer(MyGame.client.name);

            MyGame.client.output.println(MyGame.client.name + " has connected.");
            MyGame.status.addMessage("Connected to host.");
            
            MyGame.prompt = null;
        }
    }
}