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
                System.out.println("Server received: " + outputString);
            }

        } catch (Exception e) {
            System.out.println("Client Disconnected");
        }
    }

    private void printToAllClients(String outputString) {
        for (ServerThread sT: threadList) {
            sT.output.println(outputString);
        }
    }
}
