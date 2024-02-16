import java.awt.Color;
import java.util.ArrayList;

public class Menus {
    public class MainMenu extends Menu {
        public MainMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Tetris", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY, "Start", new ButtonActions().new Start()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50, Color.GRAY, Color.DARK_GRAY, "Settings", new ButtonActions().new Settings()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50, Color.GRAY, Color.DARK_GRAY, "Host Game", new ButtonActions().new Host()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY, "Connect", new ButtonActions().new Connect()));
        
            if (MyGame.client != null) {
                buttons.add(MyGame.disconnect);
                //buttons.add(MyGame.addBot);
            }
        }
    }

    public class ResultsMenu extends Menu {
        public ResultsMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Results", MyGame.SCREEN_WIDTH / 2 - 75, 48, Color.WHITE));
            text.add(new Text("Your Deaths: " + MyGame.client.deaths, MyGame.SCREEN_WIDTH / 2 - 75, 96, Color.WHITE));
            text.add(new Text("All Deaths:", MyGame.SCREEN_WIDTH / 2 - 75, 144, Color.WHITE));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, MyGame.SCREEN_HEIGHT - 175, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }

    public class SettingsMenu extends Menu {
        public SettingsMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Settings", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE));
            text.add(new Text("Starting Level: " + MyGame.save.startLevel, MyGame.SCREEN_WIDTH / 2 - 75, 375, Color.WHITE));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 150, 350, 50, 50, Color.GRAY, Color.DARK_GRAY, "-", new ButtonActions().new ChangeLevel((short)-1)));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 + 100, 350, 50, 50, Color.GRAY, Color.DARK_GRAY, "+", new ButtonActions().new ChangeLevel((short)1)));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 425, 150, 50, Color.GRAY, Color.DARK_GRAY, "Back to Menu", new ButtonActions().new BackToMenu()));
        }
    }
}