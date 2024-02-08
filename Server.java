import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public Server(int port)
	{
		ArrayList<ServerThread> threadList = new ArrayList<>();

		try (ServerSocket serverSocket = new ServerSocket(2500)) {
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread serverThread = new ServerThread(socket, threadList);
				threadList.add(serverThread);
				serverThread.start();
			}
		} catch (Exception e) {
			System.out.println("Client disconnected");
		}
	}

	public static void main(String[] args) {
		Server server = new Server(2500);
	}
}

