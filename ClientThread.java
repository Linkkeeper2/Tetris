import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

                int start = 0;

                for (int i = 0; i < response.length(); i++) {
                    String s = response.charAt(i) + "";
                    if (i < response.length() - 5 && s.equals(" ") && s.substring(i + 1, i + 5).equals("sent")) {
                        start = i + 1;
                        break;
                    }
                }

                MyGame.sendLines(Integer.parseInt(response.substring(start + 6, start + 7)));

                System.out.println(response);
            }
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