import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (client == null && MyGame.prompt == null)
                MyGame.board.startGame();
            else {
                if (MyGame.client.gameHost && client.lobby.size() > 2) {
                    MyGame.database.addStatus(MyGame.client.name + " Started the Game!");
                }
            }
        }
    }

    public class Host implements ButtonAction {
        public void action() {
            if (MyGame.client != null)
                return;

            try {
                MyGame.client = new Client(InetAddress.getLocalHost().getHostAddress(), true);
                MyGame.client.name = MyGame.account.name;

                try {
                    MyGame.database.createServer(InetAddress.getLocalHost().getHostAddress(), MyGame.client.name);
                    MyGame.database.addLobby(MyGame.client.name);
                    MyGame.database.addStatus("Hosted Game Successfully!");
                } catch (UnknownHostException e) {
                    MyGame.status.addMessage("Could not host game.");
                }

                MyGame.menu = new Menus().new LobbyMenu();
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Failed to connect to server");
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
            if (MyGame.client == null)
                MyGame.menu = new Menus().new MainMenu();
            else
                MyGame.menu = new Menus().new LobbyMenu();
            MyGame.prompt = null;
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
            if (MyGame.save.startLevel >= 0 && MyGame.save.startLevel < 18)
                MyGame.save.startLevel += factor;
            if (MyGame.save.startLevel < 0)
                MyGame.save.startLevel = 0;
            if (factor == -1 && MyGame.save.startLevel == 18)
                MyGame.save.startLevel += factor;

            MyGame.menu.text.remove(MyGame.menu.text.size() - 1);
            MyGame.menu.text.add(new Menu().new Text("Starting Level: " + MyGame.save.startLevel,
                    MyGame.SCREEN_WIDTH / 2 - 75, 375, Color.WHITE));
        }
    }

    public class ServerList implements ButtonAction {
        public void action() {
            if (MyGame.client == null)
                MyGame.menu = new Menus().new ServerMenu();
        }
    }

    public class JoinServer implements ButtonAction {
        private String address;

        public JoinServer(String address) {
            this.address = address;
        }

        public void action() {
            MyGame.client = new Client(address, false);

            MyGame.client.name = MyGame.account.name;

            MyGame.database.addLobby(MyGame.client.name);
            MyGame.status.addMessage("Connected to host.");
            MyGame.database.addStatus(MyGame.client.name + " has connected.");

            MyGame.menu = new Menus().new LobbyMenu();
        }
    }

    public class LinkAccount implements ButtonAction {
        public void action() {
            if (MyGame.database == null) {
                MyGame.status.addMessage("Not connected to servers.", 3000);
                return;
            }

            if (MyGame.client != null)
                return;
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
            if (MyGame.database == null) {
                MyGame.status.addMessage("Not connected to servers.", 3000);
                return;
            }

            MyGame.menu = new Menus().new LeaderboardMenu();
        }
    }

    public class Multiplayer implements ButtonAction {
        public void action() {
            if (MyGame.database == null) {
                MyGame.status.addMessage("Not connected to servers.", 3000);
                return;
            }

            if (MyGame.client == null)
                MyGame.menu = new Menus().new MultiplayerMenu();
        }
    }

    public class Challenges implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new ChallengeMenu();
        }
    }
}