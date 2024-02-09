import java.awt.Color;
import java.util.ArrayList;

public class Menus {
    public class MainMenu extends Menu {
        public MainMenu() {
            this.text = new ArrayList<>();
            text.add(new Text("Tetris", MyGame.SCREEN_WIDTH / 2 - 20, 48, Color.WHITE));

            this.buttons = new ArrayList<>();
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY, "Start", new ButtonActions().new Start()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 275, 150, 50, Color.GRAY, Color.DARK_GRAY, "Host Game", new ButtonActions().new Host()));
            buttons.add(new Button(MyGame.SCREEN_WIDTH / 2 - 75, 350, 150, 50, Color.GRAY, Color.DARK_GRAY, "Connect", new ButtonActions().new Connect()));
        }
    }
}
