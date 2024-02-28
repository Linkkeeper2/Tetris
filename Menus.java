import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;

public class Menus {
    public class MainMenu extends Menu {
        public MainMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Tetris", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE, new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY, "Start", new ButtonActions().new Start()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50, Color.GRAY, Color.DARK_GRAY, "Settings", new ButtonActions().new Settings()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50, Color.GRAY, Color.DARK_GRAY, "Host Game", new ButtonActions().new Host()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY, "Servers", new ButtonActions().new ServerList()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 500, 150, 50, Color.GRAY, Color.DARK_GRAY, "Challenge", new ButtonActions().new Challenge()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Account", new ButtonActions().new Account()));
            buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Leaderboard", new ButtonActions().new Leaderboard()));

            if (MyGame.client != null) {
                buttons.add(MyGame.disconnect);
            }
        }
    }

    public class ResultsMenu extends Menu {
        public ResultsMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Results", MyGame.SCREEN_WIDTH / 2 - 75, 48, Color.WHITE, new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
            text.add(new Text("Your Deaths: " + MyGame.client.deaths, MyGame.SCREEN_WIDTH / 2 - 75, 96, Color.WHITE));
            text.add(new Text("All Deaths:", MyGame.SCREEN_WIDTH / 2 - 75, 144, Color.WHITE));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, MyGame.SCREEN_HEIGHT - 175, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }

    public class SettingsMenu extends Menu {
        public SettingsMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Settings", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE, new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
            text.add(new Text("Starting Level: " + MyGame.save.startLevel, MyGame.SCREEN_WIDTH / 2 - 75, 375, Color.WHITE));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 150, 350, 50, 50, Color.GRAY, Color.DARK_GRAY, "-", new ButtonActions().new ChangeLevel((short)-1)));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 100, 350, 50, 50, Color.GRAY, Color.DARK_GRAY, "+", new ButtonActions().new ChangeLevel((short)1)));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }

    public class ServerMenu extends Menu {
        public ServerMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Server List", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE, new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));

            this.buttons = new ArrayList<>();

            ArrayList<Document> servers = new ArrayList<>();
            FindIterable<Document> iterable = MyGame.database.getServers();
            iterable.into(servers);

            for (int i = 0; i < servers.size(); i++) {
                Document doc = servers.get(i);
                buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 150, 100 + (i * 75), 300, 50, Color.GRAY, Color.DARK_GRAY, doc.getString("name") + "'s Game", new ButtonActions().new JoinServer(doc.getString("address"))));
            }
            
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }

    public class AccountMenu extends Menu {
        public AccountMenu()  {
            this.text = new ArrayList<>();
            text.add(new Text("Account", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE, new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
            text.add(new Text(MyGame.account.name, MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 125, Color.WHITE));
            text.add(new Text("Level: " + MyGame.account.level, MyGame.SCREEN_WIDTH / 2 - 20, 100, Color.WHITE, new Rectangle(0, 100, MyGame.SCREEN_WIDTH, 48)));
            text.add(new Text("EXP: " + MyGame.account.exp + "/" + (50 + (MyGame.account.level * 50)), MyGame.SCREEN_WIDTH / 2 - 20, 148, Color.WHITE, new Rectangle(0, 148, MyGame.SCREEN_WIDTH, 48)));

            this.buttons = new ArrayList<>();
            if (MyGame.account.name.startsWith("Guest")) buttons.add(new Button(MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Link Account", new ButtonActions().new LinkAccount()));
            else buttons.add(new Button(MyGame.SCREEN_WIDTH - 175, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Log Out", new ButtonActions().new LogOut()));
            buttons.add(new Button(25, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }

    public class LeaderboardMenu extends Menu {
        public LeaderboardMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Leaderboard", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE, new Rectangle(0, 48, MyGame.SCREEN_WIDTH, 48)));
            
            ArrayList<Document> accounts = new ArrayList<>();
            FindIterable<Document> iterable = MyGame.database.getAccounts();
            iterable.into(accounts);
            String[] names = new String[accounts.size()];
            int[] levels = new int[accounts.size()];

            for (int i = 0; i < accounts.size(); i++) {
                Document doc = accounts.get(i);
                names[i] = doc.getString("name");
                levels[i] = doc.getInteger("level");
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
                    }
                }
            }

            for (int i = 0; i < names.length; i++) {
                text.add(new Text(names[i] + ": LVL " + levels[i], MyGame.SCREEN_WIDTH / 2 - 20, 96 + (i * 48), Color.WHITE, new Rectangle(0, 96 + (i * 48), MyGame.SCREEN_WIDTH, 48)));
            }

            this.buttons = new ArrayList<>();
            buttons.add(new Button(10, MyGame.SCREEN_HEIGHT - 100, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }
}