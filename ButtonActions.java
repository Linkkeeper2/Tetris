import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (client == null && MyGame.prompt == null) MyGame.board.startGame();
            else {
                if (MyGame.server != null && client.lobby.size() > 2) {
                    MyGame.board.startGame();
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
                MyGame.client.name = MyGame.account.name;

                MyGame.client.addPlayer(MyGame.client.name);

                try {
                    MyGame.status.addMessage("Hosted Game Successfully!", 5000);
                    MyGame.database.createServer(InetAddress.getLocalHost().getHostAddress(), MyGame.client.name);
                } catch (UnknownHostException e) {
                    MyGame.status.addMessage("Could not host game.");
                }

                MyGame.menu = new Menus().new LobbyMenu();
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Failed to connect to server");
                MyGame.server = null;
                MyGame.client = null;
            }
        }
    }

    public class DisConnect implements ButtonAction {
        public void action() {
            MyGame.leaveGame();
            MyGame.prompt = null;
        }
    }

    public class BackToMenu implements ButtonAction {
        public void action() {
            if (MyGame.client == null) MyGame.menu = new Menus().new MainMenu();
            else MyGame.menu = new Menus().new LobbyMenu();
            MyGame.prompt = null;
        }
    }

    public class AddBot implements ButtonAction {
        public void action() {
            MyGame.bots.add(new Bot());
        }
    }

    public class Settings implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new SettingsMenu();
        }
    }

    public class ChangeLevel implements ButtonAction {
        private short factor;

        public ChangeLevel(short factor) {
            this.factor = factor;
        }

        public void action() {
            if (MyGame.save.startLevel >= 0 && MyGame.save.startLevel < 18) MyGame.save.startLevel += factor;
            if (MyGame.save.startLevel < 0) MyGame.save.startLevel = 0;
            if (factor == -1 && MyGame.save.startLevel == 18) MyGame.save.startLevel += factor;

            MyGame.menu.text.remove(MyGame.menu.text.size() - 1);
            MyGame.menu.text.add(new Menu().new Text("Starting Level: " + MyGame.save.startLevel, MyGame.SCREEN_WIDTH / 2 - 75, 375, Color.WHITE));
        }
    }

    public class ServerList implements ButtonAction {
        public void action() {
            if (MyGame.client == null) MyGame.menu = new Menus().new ServerMenu();
        }
    }

    public class JoinServer implements ButtonAction {
        private String address;

        public JoinServer(String address) {
            this.address = address;
        } 

        public void action() {
            MyGame.client = new Client(address, 2500);

            MyGame.client.name = MyGame.account.name;

            if (MyGame.client.output != null) {
                MyGame.client.addPlayer(MyGame.client.name);

                MyGame.client.output.println(MyGame.client.name + " has connected.");
                MyGame.status.addMessage("Connected to host.");
                
                MyGame.menu = new Menus().new LobbyMenu();
            } else {
                MyGame.client.lobby.clear();
                MyGame.prompt = null;
                MyGame.client = null;
                return;
            }
        }
    }

    public class LinkAccount implements ButtonAction {
        public void action() {
            if (MyGame.client != null) return;
            MyGame.prompt = new TextBox("Enter the Account Name (If you have an account, enter it's name)");
            new TextActions().new AccountThread().start();
        }
    }

    public class LogOut implements ButtonAction {
        public void action() {
            MyGame.account = new Account();
            MyGame.menu = new Menus().new AccountMenu();
            MyGame.status.addMessage("Logged out Successfully!");
        }
    }

    public class ViewAccount implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new AccountMenu();
        }
    }

    public class StartChallenge implements ButtonAction {
        private Challenge challenge;

        public StartChallenge(Challenge challenge) {
            this.challenge = challenge;
        }

        public void action() {
            MyGame.board.startChallenge(this.challenge);
        }
    }

    public class Leaderboard implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new LeaderboardMenu();
        }
    }

    public class Multiplayer implements ButtonAction {
        public void action() {
            if (MyGame.client == null) MyGame.menu = new Menus().new MultiplayerMenu();
        }
    }

    public class Challenges implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new ChallengeMenu();
        }
    }
}