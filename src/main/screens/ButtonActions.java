package src.main.screens;

import java.net.InetAddress;
import java.net.UnknownHostException;

import src.main.MyGame;
import src.main.game.SoundManager;
import src.main.game.challenges.Challenge;
import src.main.game.graphics.ColorPalette;
import src.main.screens.scaffolds.ButtonAction;
import src.main.server.Account;
import src.main.server.Client;
import src.main.server.Tournament;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (client == null && MyGame.prompt == null)
                MyGame.board.startGame();
            else {
                if (MyGame.client.gameHost && client.lobby.size() > 2) {
                    MyGame.database.addStatus(MyGame.client.name + " Started the Game!");
                    MyGame.database.updateGameStatus(true);
                }
            }
        }
    }

    public class StartTournament implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (MyGame.client.gameHost && client.lobby.size() > 2 && (client.lobby.size() - 1) % 2 == 0) {
                MyGame.database.addStatus(MyGame.client.name + " Begun a Tournament!");
                MyGame.database.updateGameStatus(true);
                MyGame.board.tournament = new Tournament(MyGame.database.getServerPlayers());
                MyGame.database.updateTournament();
            } else {
                if (!MyGame.client.gameHost)
                    MyGame.status.addMessage("Only the host can start a Tournament.");
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

            for (int i = 0; i < MyGame.menu.text.size(); i++) {
                if (MyGame.menu.text.get(i).contents.contains("Starting"))
                    MyGame.menu.text.get(i).contents = "Starting Level: " + MyGame.save.startLevel;
            }
        }
    }

    public class ChangeDelay implements ButtonAction {
        private short factor;

        public ChangeDelay(short factor) {
            this.factor = factor;
        }

        public void action() {
            if (MyGame.account.inputDelay >= 0 && MyGame.account.inputDelay < 1000)
                MyGame.account.inputDelay += factor;
            if (MyGame.account.inputDelay < 0)
                MyGame.account.inputDelay = 0;
            if (factor < 0 && MyGame.account.inputDelay == 1000)
                MyGame.account.inputDelay += factor;

            for (int i = 0; i < MyGame.menu.text.size(); i++) {
                if (MyGame.menu.text.get(i).contents.contains("Input"))
                    MyGame.menu.text.get(i).contents = "Input Delay: " + MyGame.account.inputDelay;
            }
        }
    }

    public class ChangeVolume implements ButtonAction {
        private float factor;

        public ChangeVolume(float factor) {
            this.factor = factor;
        }

        public void action() {
            if (SoundManager.volume >= 0 && SoundManager.volume < 1)
                SoundManager.volume += factor;
            if (SoundManager.volume < 0)
                SoundManager.volume = 0;
            if (factor < 0 && SoundManager.volume >= 1)
                SoundManager.volume += factor;

            for (int i = 0; i < MyGame.menu.text.size(); i++) {
                if (MyGame.menu.text.get(i).contents.contains("Volume"))
                    MyGame.menu.text.get(i).contents = "Volume: " + (int) (SoundManager.volume * 100);
            }
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
            if (MyGame.database.serverStarted(address)) {
                MyGame.status.addMessage("Server is currently in a game.", 5000);
                return;
            }

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
            MyGame.palette = new ColorPalette();
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

    public class GoToControls implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new ControlsMenu();
        }
    }

    public class SetControl implements ButtonAction {
        private int controlNum;

        public SetControl(int num) {
            controlNum = num;
        }

        public void action() {
            MyGame.controlToSet = controlNum;
        }
    }

    public class GoToSkins implements ButtonAction {
        public void action() {
            MyGame.menu = new Menus().new SkinsMenu();
        }
    }

    public class SwitchSkin implements ButtonAction {
        private String skinPath;
        private int skinRows;
        private int skinLevel;

        public SwitchSkin(String path, int rows, int level) {
            skinPath = path;
            skinRows = rows;
            skinLevel = level;
        }

        public void action() {
            if (MyGame.account.level < skinLevel) {
                MyGame.status.addMessage("You must be level " + skinLevel + " for this skin.", 5000);
                return;
            }

            MyGame.account.skin = skinPath;
            MyGame.account.skinRows = skinRows;
            MyGame.palette = new ColorPalette(skinRows);

            if (MyGame.database != null)
                MyGame.database.updateAccount(MyGame.account.name);

            MyGame.status.addMessage("Skin Applied!", 3000);
        }
    }
}