import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;
import java.awt.Graphics;
import java.awt.Color;

public class Client {
	public DataOutputStream out = null;
	public PrintWriter output;
	public BufferedReader input;
	public String name = "";
	public Socket socket = null;
	public ArrayList<Menu.Text> lobby;
	public int deaths = 0;
	public ArrayList<TetriminoNode> queue;
	public int queueTimer = 0;
	
	public Client(String host, int port)
	{
		lobby = new ArrayList<>();
		queue = new ArrayList<>();
		addPlayer("Lobby");
		MyGame.timer.schedule(new TimerTask() {
			public void run() {
				increaseTimer();
			}
		}, (long)1500);

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

		cleanLobby();
	}

	public void drawQueue(Graphics pen) {
		for (int i = 0; i < queue.size(); i++) {
			queue.get(i).row = MyGame.board.board.length - i - 1;
			
			if (queueTimer > queue.size() + 3) queue.get(i).sprite = MyGame.palette.sheet.sprites[14];
			else if (queueTimer > queue.size()) queue.get(i).sprite = MyGame.palette.sheet.sprites[20];
			else if (queueTimer > queue.size() / 2) queue.get(i).sprite = MyGame.palette.sheet.sprites[4];
			else queue.get(i).sprite = MyGame.palette.sheet.sprites[5];

			queue.get(i).updateColor();

			queue.get(i).draw(pen);
		}
	}

	public void cleanLobby() {
		for (int i = 0; i < lobby.size(); i++) {
			for (int k = i + 1; k < lobby.size(); k++) {
				if (lobby.get(i).contents.equals(lobby.get(k).contents)) {
					lobby.remove(k);
				}
			}
		}

		/*
		if (lobby.size() > 2) {
            if (MyGame.server != null && !MyGame.menu.buttons.contains(MyGame.addBot)) MyGame.menu.buttons.add(MyGame.addBot);
		} else {
			MyGame.menu.buttons.remove(MyGame.addBot);
		}
		*/
	}

	public void addPlayer(String s) {
		lobby.add(new Menu().new Text(s, 0, 0, Color.WHITE));
	}

	public void removePlayer(Menu.Text t) {
		lobby.remove(t);
	}

	public void changeTimer(int value) {
		queueTimer -= value;

		if (queue.size() > 2 && queueTimer < queue.size() / 2) {
			MyGame.linesToSend = 5;
			MyGame.board.sendLines(queue.size() / 2);

			for (int i = 0; i < queue.size() / 2; i++) {
				queue.remove(i);
			}

			queueTimer = 0;
		} else if (queue.size() > 0 && queueTimer < 0) {
			MyGame.linesToSend = 5;
			MyGame.board.sendLines(queue.size());
			queue.clear();
			queueTimer = 0;
		}
	}

	public void increaseTimer() {
		// Will increase the queue timer to fully recieve lines
		if (MyGame.menu == null && queue.size() > 0) queueTimer++;
		else queueTimer = 0;

		if (queue.size() > 0 && queueTimer > queue.size() + 5) {
			MyGame.board.recieveLines(queue.size());
			queue.clear();
		}

		MyGame.timer.schedule(new TimerTask() {
			public void run() {
				if (MyGame.client != null) increaseTimer();
			}
		}, (long)1500);
	}
}