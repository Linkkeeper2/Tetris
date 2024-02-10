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

    public class Chat implements TextAction {
        public void action() {
            MyGame.prompt = new TextBox("Enter your chat message");
            Thread t = new ChatThread();
            t.start();
        }
    }

    public class NameHostThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            MyGame.client.name = MyGame.prompt.send.split(" ")[0];
            MyGame.client.addPlayer(MyGame.client.name);

            try {
                MyGame.status.addMessage("Hosting game on port '2500' -> " + InetAddress.getLocalHost().getHostAddress(), 5000);
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Could not host game.");
            }

            MyGame.prompt = null;
        }
    }

    public class ConnectClientThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            String host = MyGame.prompt.send;

            MyGame.prompt = new TextBox("Enter Your Name");

            while (MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            String name = MyGame.prompt.send;

            MyGame.client = new Client(host, 2500);

            if (MyGame.client.output != null) {
                MyGame.client.name = name;
                MyGame.client.addPlayer(MyGame.client.name);

                MyGame.client.output.println(MyGame.client.name + " has connected.");
                MyGame.status.addMessage("Connected to host.");
            } else {
                MyGame.menu.buttons.remove(MyGame.disconnect);
                MyGame.client.lobby.clear();
                MyGame.prompt = null;
                MyGame.client = null;
                return;
            }
            
            MyGame.prompt = null;
        }
    }

    public class ChatThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            MyGame.client.output.println(MyGame.client.name + ": " + MyGame.prompt.send);
            
            MyGame.chat.bubble = null;
            MyGame.prompt = null;
        }
    }
}