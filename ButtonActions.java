import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            Client client = MyGame.client;
            if (client == null) MyGame.startGame();
            else {
                if (MyGame.server != null) {
                    MyGame.startGame();
                    MyGame.client.output.println(MyGame.client.name + " Started the Game!");
                    MyGame.status.addMessage(MyGame.client.name + " Started the Game!");
                }
            }
        }
    }

    public class Host implements ButtonAction {
        public void action() {
            MyGame.server = new Server(2500);
            MyGame.server.start();

            try {
                MyGame.client = new Client(InetAddress.getLocalHost().getHostAddress(), 2500);
                MyGame.status.addMessage("Hosting game on port '2500' -> " + InetAddress.getLocalHost().getHostAddress(), 3000);
            } catch (UnknownHostException e) {
                MyGame.status.addMessage("Failed to connect to server");
            }
        }
    }

    public class Connect implements ButtonAction {
        public void action() {
            if (MyGame.server != null) return;
            
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the host address: ");
            String host = scanner.nextLine();
            MyGame.client = new Client(host, 2500);
            MyGame.client.output.println(MyGame.client.name + " has connected.");
            MyGame.status.addMessage("Connected to host.");
            scanner.close();
        }
    }
}
