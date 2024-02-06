import java.awt.Color;

public class Menus {
    public class MainMenu extends Menu {
        public MainMenu() {
            this.text = new Text[1];
            text[0] = new Text("Tetris", MyGame.SCREEN_WIDTH / 2 - 20, 48);

            this.buttons = new Button[1];
            buttons[0] = new Button(MyGame.SCREEN_WIDTH / 2 - 75, 200, 150, 50, Color.GRAY, Color.DARK_GRAY, "Start");
        }
    }
}
