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
                    if (!response.equals("Starting Game!")) {
                        MyGame.recieveLines(Integer.parseInt(s[2]));
                        System.out.println(response);
                    } else {
                        MyGame.startGame();
                    }
                }
            }
        } catch (SocketException e) {
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