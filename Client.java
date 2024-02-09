import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Client {
	public DataOutputStream out = null;
	public PrintWriter output;
	public BufferedReader input;
	public String name = "";
	public Socket socket = null;
	public ArrayList<Menu.Text> lobby;
	
	public Client(String host, int port)
	{
		lobby = new ArrayList<>();
		addPlayer("Lobby");

		try {
			socket = new Socket(host, port);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
			ClientThread clientThread = new ClientThread(socket);

			clientThread.start();
		} catch (Exception e) {
			MyGame.status.addMessage("Could not connect to server.");
		}
	}

	public void drawLobby(Graphics pen) {
		for (int i = 0; i < lobby.size(); i++) {
			if (MyGame.menu == null) {
				lobby.get(i).x = MyGame.offset + 10 * 25 + 140;
				lobby.get(i).y = MyGame.offset + i * 25;
			} else {
				lobby.get(i).x = 32;
				lobby.get(i).y = 200 + (i * 25);
			}
			lobby.get(i).draw(pen);
		}
	}

	public void addPlayer(String s) {
		lobby.add(new Menu().new Text(s, 0, 0, Color.WHITE));
	}

	public void removePlayer(Menu.Text t) {
		lobby.remove(t);
	}
}