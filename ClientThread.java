import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader input;

    public ClientThread(Socket s) throws IOException {
        this.socket = s;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = input.readLine();
                String[] s = response.split(" ");

                if (!s[0].equals(MyGame.client.name)) {
                    if (!s[1].equals("Started")) {
                        if (response.endsWith("connected.")) {
                            MyGame.status.addMessage(response);
                        } else {
                            MyGame.recieveLines(Integer.parseInt(s[2]));
                            MyGame.status.addMessage(response);
                        }
                    } else {
                        MyGame.startGame();
                        MyGame.status.addMessage(response);
                    }
                }
            }
        } catch (SocketException e) {
            MyGame.status.addMessage("Disconnected from host.");
            System.out.println("Disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}