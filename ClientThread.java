import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.awt.Color;

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

                if (MyGame.client != null) {
                    if (!s[0].equals(MyGame.client.name)) {
                        if (!s[1].equals("Started")) {
                            if (response.endsWith("connected.")) {
                                MyGame.status.addMessage(response);
                                MyGame.client.addPlayer(s[0]);
                                if (!response.startsWith("Bot")) MyGame.client.output.println(MyGame.client.name + " has added.");
                            } 
                            else if (response.endsWith("added.")) {
                                boolean addPlayer = true;
    
                                ArrayList<Menu.Text> lobby = MyGame.client.lobby;
    
                                for (int i = 0; i < lobby.size(); i++) {
                                    if (lobby.get(i).contents.equals(s[0])) {
                                        addPlayer = false;
                                    }
                                }
    
                                if (addPlayer) MyGame.client.addPlayer(s[0]);
                            } 
                            else if (response.endsWith("left.")) {
                                Menu.Text t = null;
                                ArrayList<Menu.Text> lobby = MyGame.client.lobby;
    
                                for (int i = 0; i < lobby.size(); i++) {
                                    if (lobby.get(i).contents.equals(s[0])) {
                                        t = lobby.get(i);
                                    }
                                }
    
                                if (t != null) {
                                    MyGame.client.removePlayer(t);
                                    MyGame.status.addMessage(s[0] + " has left.");
                                    
                                    if (MyGame.server == null && MyGame.client.lobby.size() == 2) {
                                        MyGame.client = null;
                                        if (MyGame.menu != null) MyGame.menu.buttons.remove(MyGame.disconnect);
                                    }
                                }
                            } 
                            else if (response.endsWith("out."))  {
                                MyGame.status.addMessage(response);
                                SoundManager.playSound("sfx/KO.wav", false);
                            } 
                            else if (response.endsWith("over.")) {
                                if (MyGame.server == null) MyGame.endGame();
                                MyGame.status.addMessage("Game Over.");
                            }
                            else if (response.endsWith("deaths.")) {
                                MyGame.client.queue.clear();
                                ArrayList<Menu.Text> text = MyGame.menu.text;
                                boolean add = true;

                                for (int i = 0; i < text.size(); i++) {
                                    if (text.get(i).contents.startsWith(s[0])) add = false;
                                }

                                if (add) MyGame.menu.text.add(new Menu().new Text(s[0] + ": " + s[1] + " deaths.", MyGame.SCREEN_WIDTH / 2 - 75, 144 + ((MyGame.menu.text.size() - 2) * 48), Color.WHITE));
                            }
                            else if (response.equals("Game Ended.")) {
                                MyGame.exitToMenu();
                                MyGame.status.addMessage("Game Ended by Host.");
                            } 
                            else {
                                if (response.endsWith("chat.")) {
                                    if (!s[0].equals(MyGame.client.name)) MyGame.chat.addMessage(response.substring(0, response.length() - 6));
                                    SoundManager.playSound("sfx/Msg.wav", false);
                                } else {
                                    if (s[5].equals(MyGame.client.name)) {
                                        for (int i = 0; i < Integer.parseInt(s[2]); i++) {
                                            if (MyGame.client.queue.size() < 10) MyGame.client.queue.add(new TetriminoNode(Color.BLUE, 0, -2, -1));
                                        }

                                        MyGame.status.addMessage("Recieved " + s[2] + " lines from " + s[0]);
                                    }
                                }
                            }
                        } else {
                            MyGame.board.startGame();
                            MyGame.status.addMessage(response);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            MyGame.status.addMessage("Disconnected from host.");
            MyGame.client = null;
            MyGame.menu = new Menus().new MainMenu();
        } catch (IOException e) {} 
        finally {
            try {
                input.close();
            } catch (Exception e) {}
        }
    }
}