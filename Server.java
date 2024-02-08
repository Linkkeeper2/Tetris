import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.Socket;

public class Server extends Thread {
	private int port;
	private ArrayList<ServerThread> threadList;

	public Server(int port)
	{
		this.port = port;
	}

	@Override
	public void run() {
		threadList = new ArrayList<>();

		try (ServerSocket serverSocket = new ServerSocket(port)) {
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
}