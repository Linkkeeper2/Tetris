import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (client == null && MyGame.prompt == null) MyGame.startGame();
            else {
                if (MyGame.server != null && client.lobby.size() > 2) {
                    MyGame.startGame();
                    MyGame.client.output.println(MyGame.client.name + " Started the Game!");
                    MyGame.status.addMessage(MyGame.client.name + " Started the Game!");
                } else {
                    if (MyGame.server != null) MyGame.status.addMessage("Game cannot start with 1 player.");
                    else MyGame.status.addMessage("Only the host can start the game..");
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
                MyGame.disconnect = MyGame.menu.new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY, "Disconnect", new ButtonActions().new DisConnect());
                MyGame.menu.buttons.add(MyGame.disconnect);
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Failed to connect to server");
                MyGame.menu.buttons.remove(MyGame.disconnect);
            }
        }
    }

    public class Connect implements ButtonAction {
        public void action() {
            if (MyGame.server != null || MyGame.client != null) return;
            new TextActions().new ConnectClient().action();
            MyGame.disconnect = MyGame.menu.new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY, "Disconnect", new ButtonActions().new DisConnect());
            MyGame.menu.buttons.add(MyGame.disconnect);
        }
    }

    public class DisConnect implements ButtonAction {
        public void action() {
            MyGame.leaveGame();
        }
    }

    public class BackToMenu implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new MainMenu();
        }
    }
}