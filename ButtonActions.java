import java.net.InetAddress;
import java.net.UnknownHostException;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (client == null) MyGame.startGame();
            else {
                if (MyGame.server != null) {
                    MyGame.startGame();
                    MyGame.client.output.println(MyGame.client.name + " Started the Game!");
                    MyGame.status.addMessage(MyGame.client.name + " Started the Game!");
                }
            }
        }
    }

    public class Host implements ButtonAction {
        public void action() {
            if (MyGame.client != null) return;

            MyGame.server = new Server(2500);
            MyGame.server.start();

            try {
                MyGame.client = new Client(InetAddress.getLocalHost().getHostAddress(), 2500);
                new TextActions().new EnterName().action();
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Failed to connect to server");
            }
        }
    }

    public class Connect implements ButtonAction {
        public void action() {
            if (MyGame.server != null || MyGame.client != null) return;
            new TextActions().new ConnectClient().action();
        }
    }
}
