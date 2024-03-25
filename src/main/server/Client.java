package src.main.server;

import java.util.ArrayList;
import java.util.TimerTask;

import src.main.MyGame;
import src.main.game.objects.TetriminoNode;
import src.main.screens.scaffolds.Menu;

import java.awt.Graphics;

public class Client {
	public String name = "";
	public ArrayList<Menu.Text> lobby;
	public int deaths = 0;
	public ArrayList<TetriminoNode> queue;
	public int queueTimer = 0;
	public boolean gameHost = false;
	public String host;

	public Client(String host, boolean gameHost) {
		lobby = new ArrayList<>();
		queue = new ArrayList<>();

		try {
			MyGame.timer.schedule(new TimerTask() {
				public void run() {
					increaseTimer();
				}
			}, (long) 1500);
		} catch (IllegalStateException e) {
		}

		this.gameHost = gameHost;
		this.host = host;
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

	public void drawQueue(Graphics pen) {
		for (int i = 0; i < queue.size(); i++) {
			queue.get(i).row = MyGame.board.board.length - i - 1;

			if (queueTimer > queue.size() + 3)
				queue.get(i).sprite = MyGame.palette.sheet.sprites[14];
			else if (queueTimer > queue.size())
				queue.get(i).sprite = MyGame.palette.sheet.sprites[20];
			else if (queueTimer > queue.size() / 2)
				queue.get(i).sprite = MyGame.palette.sheet.sprites[4];
			else
				queue.get(i).sprite = MyGame.palette.sheet.sprites[5];

			queue.get(i).updateColor();

			queue.get(i).draw(pen);
		}
	}

	public void updateLobby() {
		lobby = MyGame.database.readLobby();

		if (!gameHost)
			return;

		ArrayList<Menu.Text> status = MyGame.status.messages;

		for (int i = 0; i < status.size(); i++) {
			if (status.get(i).contents.contains("left")) {
				Menu.Text s = status.get(i);

				for (int k = 0; k < lobby.size(); k++) {
					if (lobby.get(k).contents.contains(s.contents.split(" ")[0])) {
						MyGame.database.removeLobby(lobby.get(k).contents);

						try {
							MyGame.timer.schedule(new TimerTask() {
								public void run() {
									MyGame.database.removeStatus(s.contents);
								}
							}, 1500);
						} catch (IllegalStateException e) {
						}
					}
				}
			}
		}
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
		if (MyGame.menu == null && queue.size() > 0)
			queueTimer++;
		else
			queueTimer = 0;

		if (queue.size() > 0 && queueTimer > queue.size() + 5) {
			MyGame.board.recieveLines(queue.size());
			queue.clear();
		}

		MyGame.timer.schedule(new TimerTask() {
			public void run() {
				if (MyGame.client != null)
					increaseTimer();
			}
		}, (long) 1500);
	}
}