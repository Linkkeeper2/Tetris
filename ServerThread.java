import java.net.Socket;
import java.util.ArrayList;
import java.io.*;

public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String outputString = input.readLine();

                if (outputString.equals("Exit")) break;

                printToAllClients(outputString);
                System.out.println(outputString);
            }

        } catch (Exception e) {
            System.out.println("Client Disconnected");
        }
    }

    private void printToAllClients(String outputString) {
        boolean sendLines = false;

        for (ServerThread sT: threadList) {
            sT.output.println(outputString);
            
            Client client = MyGame.client;
            
            if (sendLines) {
                client.recieveLines(Integer.parseInt(outputString.substring(client.name.length() + 6, client.name.length() + 7)));
            }

            if (outputString.substring(0, client.name.length()).equals(client.name)) {
                sendLines = true;
            }
        }
    }
}
