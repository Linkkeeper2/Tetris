import java.net.InetAddress;
import java.net.UnknownHostException;

public class TextActions {
    public class EnterName implements TextAction {
        public void action() {
            MyGame.client.name = MyGame.account.name;
            MyGame.client.addPlayer(MyGame.client.name);

            try {
                MyGame.status.addMessage("Hosting game on port '2500' -> " + InetAddress.getLocalHost().getHostAddress(), 5000);
                MyGame.database.createServer(InetAddress.getLocalHost().getHostAddress(), MyGame.client.name);
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Could not host game.");
            }
        }
    }

    public class EnterNameClient implements TextAction {
        private String address;

        public EnterNameClient(String address) {
            this.address = address;
        }

        public void action() {
            MyGame.client = new Client(address, 2500);

            MyGame.client.name = MyGame.account.name;

            if (MyGame.client.output != null) {
                MyGame.client.addPlayer(MyGame.client.name);

                MyGame.client.output.println(MyGame.client.name + " has connected.");
                MyGame.status.addMessage("Connected to host.");
                
                MyGame.menu = new Menus().new MainMenu();
                MyGame.menu.buttons.add(MyGame.disconnect);
            } else {
                MyGame.menu.buttons.remove(MyGame.disconnect);
                MyGame.client.lobby.clear();
                MyGame.prompt = null;
                MyGame.client = null;
                return;
            }
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

    public class ConnectClientThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            String host = MyGame.prompt.send;

            MyGame.client = new Client(host, 2500);

            if (MyGame.client.output != null) {
                MyGame.client.name = MyGame.account.name;
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

            MyGame.client.output.println(MyGame.client.name + ": " + MyGame.prompt.send + " chat.");
            
            MyGame.chat.bubble = null;
            MyGame.prompt = null;
        }
    }

    public class AccountThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            String name = MyGame.prompt.send.split(" ")[0];

            MyGame.prompt = new TextBox("Enter/Create Your Password");

            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            String password = MyGame.prompt.send;

            MyGame.database.linkAccount(name, password);
            MyGame.menu = new Menus().new AccountMenu();

            MyGame.prompt = null;
        }
    }
}