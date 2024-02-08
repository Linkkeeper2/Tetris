import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ButtonActions {
    public class Start implements ButtonAction {
        public void action() {
            MyGame.startGame();
        }
    }

    public class Host implements ButtonAction {
        public void action() {
            MyGame.server = new Server(2500);
            MyGame.server.start();

            try {
                MyGame.client = new Client(InetAddress.getLocalHost().getHostAddress(), 2500);
            } catch (UnknownHostException e) {
                System.out.println("Failed to connect to server");
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
            scanner.close();
        }
    }
}
