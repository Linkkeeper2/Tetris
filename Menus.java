import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.TimerTask;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import java.awt.Graphics;

public class Menus {
        public class MainMenu extends Menu {
                public MainMenu() {
                        super();

                        text.add(new Text("Tetris", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Start Solo", new ButtonActions().new Start()));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Multiplayer", new ButtonActions().new Multiplayer()));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Settings",
                                        new ButtonActions().new Settings()));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Challenges", new ButtonActions().new Challenges()));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 100, 150, 50,
                                        Color.GRAY,
                                        Color.DARK_GRAY, "Account", new ButtonActions().new ViewAccount()));
                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Leaderboard",
                                        new ButtonActions().new Leaderboard()));
                        SoundManager.stopAllSounds();
                        MyGame.status.results.clear();
                        if (MyGame.database != null)
                                MyGame.database.updateAccount(MyGame.account.name);

                        MyGame.timer.schedule(new TimerTask() {
                                public void run() {
                                        MyGame.status.messages.clear();
                                }
                        }, 7500);

                        MyGame.board.spectating = false;
                }
        }

        public class ResultsMenu extends Menu {
                public ArrayList<Menu.Text> otherDeaths = new ArrayList<>();

                public ResultsMenu() {
                        super();

                        text.add(new Text("Results", MyGame.SCREEN_WIDTH / 2 - 75, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
                        text.add(new Text("Your Deaths: " + MyGame.client.deaths, MyGame.SCREEN_WIDTH / 2 - 75, 96,
                                        Color.WHITE));
                        text.add(new Text("All Deaths:", MyGame.SCREEN_WIDTH / 2 - 75, 144, Color.WHITE));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, MyGame.SCREEN_HEIGHT - 175, 150, 50,
                                        Color.GRAY,
                                        Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
                }

                public void draw(Graphics pen) {
                        super.draw(pen);

                        for (int i = 0; i < otherDeaths.size(); i++) {
                                otherDeaths.get(i).draw(pen);
                        }
                }
        }

        public class SettingsMenu extends Menu {
                public SettingsMenu() {
                        super();
                        text.add(new Text("Settings", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
                        text.add(new Text("Volume: " + (int) (SoundManager.volume * 100), MyGame.SCREEN_WIDTH / 2 - 75,
                                        275,
                                        Color.WHITE));
                        text.add(new Text("Starting Level: " + MyGame.save.startLevel, MyGame.SCREEN_WIDTH / 2 - 75,
                                        375,
                                        Color.WHITE));
                        text.add(new Text("Input Delay: " + MyGame.account.inputDelay, MyGame.SCREEN_WIDTH / 2 - 75,
                                        325, Color.WHITE));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 150, 350, 50, 50, Color.GRAY, Color.DARK_GRAY,
                                        "-",
                                        new ButtonActions().new ChangeLevel((short) -1)));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 100, 350, 50, 50, Color.GRAY, Color.DARK_GRAY,
                                        "+",
                                        new ButtonActions().new ChangeLevel((short) 1)));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 150, 300, 50, 50, Color.GRAY, Color.DARK_GRAY,
                                        "-",
                                        new ButtonActions().new ChangeDelay((short) -10)));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 100, 300, 50, 50, Color.GRAY, Color.DARK_GRAY,
                                        "+",
                                        new ButtonActions().new ChangeDelay((short) 10)));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 150, 250, 50, 50, Color.GRAY, Color.DARK_GRAY,
                                        "-",
                                        new ButtonActions().new ChangeVolume(-0.05f)));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 100, 250, 50, 50, Color.GRAY, Color.DARK_GRAY,
                                        "+",
                                        new ButtonActions().new ChangeVolume(0.05f)));

                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Controls", new ButtonActions().new GoToControls()));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 500, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Skins", new ButtonActions().new GoToSkins()));
                }
        }

        public class ServerMenu extends Menu {
                public ServerMenu() {
                        super();
                        text.add(new Text("Server List", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        ArrayList<Document> servers = new ArrayList<>();
                        FindIterable<Document> iterable = MyGame.database.getServers();
                        iterable.into(servers);

                        for (int i = 0; i < servers.size(); i++) {
                                Document doc = servers.get(i);
                                buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 150, 100 + (i * 75), 300, 50,
                                                Color.GRAY,
                                                Color.DARK_GRAY, doc.getString("name") + "'s Game",
                                                new ButtonActions().new JoinServer(doc.getString("address"))));
                        }

                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));
                }
        }

        public class AccountMenu extends Menu {
                public AccountMenu() {
                        super();
                        text.add(new Text("Account", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
                        text.add(new Text(MyGame.account.name, MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 125,
                                        Color.WHITE));
                        text.add(new Text("Level: " + MyGame.account.level, MyGame.SCREEN_WIDTH / 2 - 20, 100,
                                        Color.WHITE,
                                        new Rectangle(0, 100, MyGame.SCREEN_WIDTH, 48)));
                        text.add(new Text("Prestige: " + MyGame.account.prestige, MyGame.SCREEN_WIDTH / 2 - 20, 148,
                                        Color.WHITE,
                                        new Rectangle(0, 148, MyGame.SCREEN_WIDTH, 48)));
                        text.add(new Text("EXP: " + MyGame.account.exp + "/" + (50 + (MyGame.account.level * 50)),
                                        MyGame.SCREEN_WIDTH / 2 - 20, 196, Color.WHITE,
                                        new Rectangle(0, 196, MyGame.SCREEN_WIDTH, 48)));
                        text.add(new Text("Highest Level: " + MyGame.account.highestLevel, MyGame.SCREEN_WIDTH / 2 - 20,
                                        244,
                                        Color.WHITE, new Rectangle(0, 244, MyGame.SCREEN_WIDTH, 48)));

                        if (MyGame.account.name.startsWith("Guest"))
                                buttons.add(new Button(MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 100, 150, 50,
                                                Color.GRAY,
                                                Color.DARK_GRAY, "Link Account",
                                                new ButtonActions().new LinkAccount()));
                        else
                                buttons.add(new Button(MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 100, 150, 50,
                                                Color.GRAY,
                                                Color.DARK_GRAY, "Log Out", new ButtonActions().new LogOut()));
                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));
                }
        }

        public class LeaderboardMenu extends Menu {
                public LeaderboardMenu() {
                        super();
                        text.add(new Text("Leaderboard", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        ArrayList<Document> accounts = new ArrayList<>();
                        FindIterable<Document> iterable = MyGame.database.getAccounts();
                        iterable.into(accounts);
                        String[] names = new String[accounts.size()];
                        int[] levels = new int[accounts.size()];
                        int[] highestLvl = new int[accounts.size()];
                        int[] prestiges = new int[accounts.size()];

                        for (int i = 0; i < accounts.size(); i++) {
                                Document doc = accounts.get(i);
                                names[i] = doc.getString("name");
                                levels[i] = doc.getInteger("level");
                                highestLvl[i] = doc.getInteger("highestLvl");
                                prestiges[i] = doc.getInteger("prestige");
                        }

                        for (int k = 0; k < levels.length; k++) {
                                for (int i = k; i < levels.length - 1; i++) {
                                        if (levels[i] < levels[i + 1]) {
                                                int tmp = levels[i];
                                                levels[i] = levels[i + 1];
                                                levels[i + 1] = tmp;

                                                String temp = names[i];
                                                names[i] = names[i + 1];
                                                names[i + 1] = temp;

                                                tmp = highestLvl[i];
                                                highestLvl[i] = highestLvl[i + 1];
                                                highestLvl[i + 1] = tmp;

                                                tmp = prestiges[i];
                                                prestiges[i] = prestiges[i + 1];
                                                prestiges[i + 1] = tmp;
                                        }
                                }
                        }

                        for (int i = 0; i < names.length; i++) {
                                text.add(new Text(
                                                names[i] + ": LVL " + levels[i] + " (Prestige: " + prestiges[i]
                                                                + ") | Highest LVL: "
                                                                + highestLvl[i],
                                                MyGame.SCREEN_WIDTH / 2 - 20, 96 + (i * 48), Color.WHITE,
                                                new Rectangle(0, 96 + (i * 48), MyGame.SCREEN_WIDTH, 48)));
                        }

                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));
                }
        }

        public class MultiplayerMenu extends Menu {
                public MultiplayerMenu() {
                        super();
                        text.add(new Text("Mutliplayer", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Host Game",
                                        new ButtonActions().new Host()));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Servers",
                                        new ButtonActions().new ServerList()));
                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));
                }
        }

        public class LobbyMenu extends Menu {
                public LobbyMenu() {
                        super();

                        text.add(new Text("Lobby", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        if (MyGame.client != null && MyGame.client.gameHost) {
                                buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY,
                                                Color.DARK_GRAY,
                                                "Start Game", new ButtonActions().new Start()));

                                /*
                                 * buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50,
                                 * Color.GRAY,
                                 * Color.DARK_GRAY,
                                 * "Start Tournament", new ButtonActions().new StartTournament()));
                                 */

                                buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50, Color.GRAY,
                                                Color.DARK_GRAY,
                                                "Disconnect", new ButtonActions().new DisConnect()));
                        } else {
                                buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY,
                                                Color.DARK_GRAY,
                                                "Disconnect", new ButtonActions().new DisConnect()));
                        }

                        // buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50,
                        // Color.GRAY, Color.DARK_GRAY, "Add Bot", new ButtonActions().new AddBot()));
                }
        }

        public class ChallengeMenu extends Menu {
                public ChallengeMenu() {
                        super();

                        text.add(new Text("Challenges", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Checkerboard",
                                        new ButtonActions().new StartChallenge(new CheckerboardChallenge())));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Staircase",
                                        new ButtonActions().new StartChallenge(new StairCaseChallenge())));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Hard Checkboard",
                                        new ButtonActions().new StartChallenge(new HardCheckerboardChallenge())));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "X",
                                        new ButtonActions().new StartChallenge(new XChallenge())));
                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));
                }
        }

        public class ControlsMenu extends Menu {
                public ControlsMenu() {
                        super();

                        text.add(new Text("Controls", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        text.add(new Text("Hard Drop: " + KeyEvent.getKeyText(MyGame.account.controls.get(0)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 120, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 95, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(0)));

                        text.add(new Text("Move Left: " + KeyEvent.getKeyText(MyGame.account.controls.get(1)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 168, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 143, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(1)));

                        text.add(new Text("Rotate Clockwise: " + KeyEvent.getKeyText(MyGame.account.controls.get(2)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 216, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 192, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(2)));

                        text.add(new Text("Move Right: " + KeyEvent.getKeyText(MyGame.account.controls.get(3)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 264, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 239, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(3)));

                        text.add(new Text("Soft Drop: " + KeyEvent.getKeyText(MyGame.account.controls.get(4)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 312, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 287, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(4)));

                        text.add(new Text("Open Chat: " + KeyEvent.getKeyText(MyGame.account.controls.get(5)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 368, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 344, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(5)));

                        text.add(new Text("Hold Piece: " + KeyEvent.getKeyText(MyGame.account.controls.get(6)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 416, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 392, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(6)));

                        text.add(new Text(
                                        "Rotate CounterClockwise: "
                                                        + KeyEvent.getKeyText(MyGame.account.controls.get(7)),
                                        MyGame.SCREEN_WIDTH / 2 - 75, 464, Color.WHITE));
                        buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 200, 439, 40, 40, Color.GRAY, Color.DARK_GRAY,
                                        "Set",
                                        new ButtonActions().new SetControl(7)));

                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu",
                                        new ButtonActions().new BackToMenu()));
                }
        }

        public class SkinsMenu extends Menu {
                public SkinsMenu() {
                        super();

                        text.add(new Text("Skins", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE,
                                        new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

                        buttons.add(new Button(MyGame.SCREEN_HEIGHT / 2 + 25, 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Default", new ButtonActions().new SwitchSkin("gfx/PaletteBattle.png", 10, 0)));

                        buttons.add(new Button(MyGame.SCREEN_HEIGHT / 2 + 25, 175, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "NES", new ButtonActions().new SwitchSkin("gfx/Palette.png", 10, 1)));

                        buttons.add(new Button(MyGame.SCREEN_HEIGHT / 2 + 25, 250, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Glitched Colors",
                                        new ButtonActions().new SwitchSkin("gfx/Skins/Glitched.png", 54, 5)));

                        buttons.add(new Button(MyGame.SCREEN_HEIGHT / 2 + 25, 325, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Minecraft",
                                        new ButtonActions().new SwitchSkin("gfx/Skins/Minecraft.png", 10, 7)));

                        buttons.add(new Button(MyGame.SCREEN_HEIGHT / 2 + 25, 400, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Geometry Dash",
                                        new ButtonActions().new SwitchSkin("gfx/Skins/GD.png", 6, 10)));

                        buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY,
                                        "Back to Menu", new ButtonActions().new BackToMenu()));
                }
        }
}