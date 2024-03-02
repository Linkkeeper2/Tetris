public class TextActions {
    public class Chat implements TextAction {
        public void action() {
            MyGame.prompt = new TextBox("Enter your chat message");
            Thread t = new ChatThread();
            t.start();
        }
    }

    public class ChatThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            MyGame.client.output.println(MyGame.client.name + ": " + MyGame.prompt.send + " chat.");
            
            MyGame.chat.bubble = null;
            MyGame.prompt = null;
        }
    }

    public class AccountThread extends Thread {
        @Override
        public void run() {
            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            if (MyGame.prompt == null) return;

            String name = MyGame.prompt.send.split(" ")[0];

            MyGame.prompt = new TextBox("Enter/Create Your Password");

            while (MyGame.prompt != null && MyGame.prompt.send.length() == 0) {
                System.out.print("");
            }

            String password = MyGame.prompt.send;

            MyGame.database.linkAccount(name, password);
            MyGame.menu = new Menus().new AccountMenu();

            MyGame.prompt = null;
        }
    }
}