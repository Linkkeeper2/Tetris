public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            MyGame.startGame();
        }
    }

    public class Connect implements ButtonAction {
        public void action() {
            MyGame.client = new Client("localhost", 2500);
            System.out.println("Connected to game on port '2500' -> 'localhost'.");
        }
    }
}
