import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;

public class MyGame extends Game {
    public static final String TITLE = "Tetris";
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 800;

    // declare variables here
    public static Board board;
    public static int offset;
    public static Timer timer;
    public static int tNum = 0; // Number of Tetriminos used (Keeps track of ID for each Tetrimino)
    public static boolean hardDropping = false; // Determines whether or not the regular drop interval should occur
                                                // (prevents Tetrimino clipping while Hard Dropping)
    public static int speed = 1000; // The millisecond delay between automatic Tetrimino movement
    public static int lines = 0; // The number of lines cleared
    public static int score = 0; // The total score of the player
    public static short level = 0; // The level (speed) of the game
    public static Menus menus;
    public static Menu menu;
    public static Menu.Text levelMessage; // Message for level ups
    public static ColorPalette palette;
    public static int messageCol; // Column to display line clear messages
    public static int messageRow; // Row to display line clear messages
    private boolean[] arrows = new boolean[4]; // Determines whether or not to repeated left or right movement
    public static int direction = 0; // -1 = Left, 1 = Right, 0 = None
    private int inputDelay = 0; // Delay for repeating directional inputs
    public static int prevLinesCleared = 0; // Previous amount of lines cleared to score Back-to-Back Tetrises
    public static int pity = 0; // Pity to getting an IPiece
    public static int nextPity = 5; // Value that pity needs to surpass the get an IPiece
    public static boolean tSpin = false; // Determines if a T-Spin should occur
    public static boolean doActions = true; // Prevents Tetrimino movement if an action is being processed
    public static int tileSize = 24; // Size of each tile on the board
    public static String prevType = ""; // Keeps track of the last piece to prevent two of the same piece in a row

    // Client data
    public static Client client;
    public static Server server;
    public static ServerStatus status; // Status messages to display in game
    public static Chat chat; // Chat between players
    public static TextBox prompt;
    public static ArrayList<Bot> bots;
    public static int timesCleared = 0; // The amount of times the player has cleared lines in order to send lines to
                                        // other clients
    public static int linesToSend; // Amount of lines to send to other clients when a threshold is reached
    public static int clock = 10; // Clock for multiplayer games
    public static boolean recieving = false;

    public static Save save; // Save file properties for the player

    public static ClearAnimation animation;

    public static Database database;
    public static Account account;

    public MyGame() {
        // initialize variables here
        board = new Board();
        database = new Database();
        account = new Account();
        chat = new Chat();
        status = new ServerStatus();
        menus = new Menus();
        menu = menus.new MainMenu();
        levelMessage = new Menu().new Text("", 0, 700, Color.WHITE);
        palette = new ColorPalette();
        timer = new Timer();
        bots = new ArrayList<>();
        save = new Save();
        animation = new ClearAnimation();
        animation.start();

        try {
            timer.schedule(new TimerTask() {
                public void run() {
                    board.automaticMove();
                }
            }, (long) speed);

            timer.schedule(new TimerTask() {
                public void run() {
                    if (clock > 0 && menu == null) {
                        clock--;
                    }

                    switch (clock) {
                        case 150:
                            SoundManager.stopAllSounds();
                            SoundManager.playSound("sfx/BattleHalf.wav", false);
                            break;

                        case 60:
                            SoundManager.stopAllSounds();
                            SoundManager.playSound("sfx/BattleMinute.wav", false);
                            break;
                    }
                }
            }, (long) 1000, 1000);
        } catch (IllegalStateException e) {
        }

        account.login();
    }

    public void update() {
        // updating logic
        board.update();

        if (menu == null && !board.alive && client != null) {
            client.output.println(client.name + " has topped out.");
            board.reset();
            SoundManager.playSound("sfx/KO.wav", false);
        }

        if (clock <= 0 && server != null && menu == null) {
            client.output.println("... ... ... ... ... ... ... Game over.");
            endGame();
        }

        updateState();

        if (client == null && tileSize == 25)
            exitToMenu();

        if (client == null && !board.alive && menu == null) {
            status.addMessage("Game Over", 3000);
            status.addMessage("Score: " + score, 3000);
            status.addMessage("Level: " + level, 3000);

            if (board.challenge == null) {
                account.addExp(score / 400);

                if (level > MyGame.account.highestLevel
                        && (MyGame.save.startLevel == 0 || level > MyGame.save.startLevel)) {
                    MyGame.account.highestLevel = level;
                    MyGame.database.updateAccount(MyGame.account.name);
                }
            }
            exitToMenu();
        }
    }

    public void updateState() {
        // Updates the speed of the game and color palette
        if (level <= 8)
            speed = (int) (((48 - level * 5) / 60f) * 1000);
        else if (level == 9)
            speed = (int) ((6 / 60f) * 1000);
        else if (level >= 10 && level <= 12)
            speed = (int) ((5 / 60f) * 1000);
        else if (level >= 13 && level <= 15)
            speed = (int) ((4 / 60f) * 1000);
        else if (level >= 16 && level <= 18)
            speed = (int) ((3 / 60f) * 1000);
        else if (level >= 19 && level <= 28)
            speed = (int) ((2 / 60f) * 1000);
        else if (level >= 29)
            speed = (int) ((1 / 60f) * 1000);

        palette.currentPalette = level % 10;
    }

    public void draw(Graphics pen) {
        if (menu == null) {
            board.draw(pen);

            // Draws the Text to the screen
            pen.setFont(new Font("comicsansms", 0, 20));
            pen.setColor(Color.WHITE);
            pen.drawString("Lines: " + lines, offset + board.board[0].length * tileSize + 8, offset - 24);
            pen.drawString("Score: " + score, offset + board.board[0].length * tileSize + 8, offset);
            pen.drawString("Level: " + level, 32, 60);
            pen.drawString("Next", offset + board.board[0].length * tileSize + 48, offset + 40);
            pen.drawString("Hold", 32, offset + 40);

            if (client != null)
                drawClock(pen);

            levelMessage.draw(pen);
        } else {
            menu.draw(pen);
        }

        status.draw(pen);
        if (prompt != null)
            prompt.draw(pen);
        if (client != null) {
            client.drawLobby(pen);
            client.drawQueue(pen);
            chat.draw(pen);
        }
    }

    public void drawClock(Graphics pen) {
        int c = clock;
        String s = clock / 60 + ":";
        c -= clock / 60 * 60;

        if (c < 10)
            s += "0" + c;
        else
            s += c;

        pen.setColor(Color.WHITE);
        pen.drawString("Time Left " + s, offset + board.board[0].length * 25 + 8, offset - 48);
    }

    public static void exitToMenu() {
        SoundManager.stopAllSounds();

        if (client != null) {
            client.queue.clear();
            menu = menus.new LobbyMenu();
        } else {
            menu = menus.new MainMenu();
        }

        board.challenge = null;
    }

    public static void leaveGame() {
        if (client != null && client.output != null && client.input != null) {
            client.output.println(client.name + " has left.");

            try {
                client.output.close();
                client.input.close();
            } catch (IOException e) {
                status.addMessage(e.toString());
            }

            client = null;

            if (server != null) {
                try {
                    database.closeServer();
                } catch (UnknownHostException e) {
                }
            }

            server = null;
            prompt = null;
            menu = new Menus().new MainMenu();
            bots.clear();
        }

        SoundManager.stopAllSounds();
    }

    public static void endGame() {
        SoundManager.stopAllSounds();
        menu = new Menus().new ResultsMenu();
        clock = 10;

        switch (client.deaths) {
            case 0:
                account.addExp((int) ((Math.random() * 24) + 125));
                break;

            case 1:
                account.addExp((int) ((Math.random() * 49) + 75));
                break;

            case 2:
                account.addExp((int) ((Math.random() * 24) + 50));
                break;

            case 3:
                account.addExp((int) ((Math.random() * 24) + 25));
                break;

            default:
                account.addExp((int) ((Math.random() * 24) + 1));
                break;
        }

        client.output.println(client.name + " " + client.deaths + " ... ... ... ... deaths.");
        if (client != null)
            client.queue.clear();
    }

    public void repeatLeft() {
        if (arrows[0]) {
            if (inputDelay >= 150) {
                direction = -1;
                inputDelay = 0;
                return;
            }

            try {
                timer.schedule(new TimerTask() {
                    public void run() {
                        inputDelay += 10;
                        repeatLeft();
                    }
                }, (long) 10);
            } catch (IllegalStateException e) {
            }
        }
    }

    public void repeatRight() {
        if (arrows[1]) {
            if (inputDelay >= 150) {
                direction = 1;
                inputDelay = 0;
                return;
            }

            try {
                timer.schedule(new TimerTask() {
                    public void run() {
                        inputDelay += 10;
                        repeatRight();
                    }
                }, (long) 10);
            } catch (IllegalStateException e) {
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (prompt != null)
            prompt.type(ke);
        switch (ke.getKeyCode()) {
            case 27: // ESCAPE Key
                if (menu == null) {
                    if (server != null) {
                        exitToMenu();
                        client.output.println("Game Ended.");
                    } else {
                        if (client == null)
                            exitToMenu();
                    }
                } else {
                    leaveGame();

                    this.running = false;
                }
                break;

            case 32: // SPACE
                if (prompt == null) {
                    board.hardDrop();
                    if (client != null) {
                        SoundManager.playSound("sfx/Harddrop.wav", false);
                    } else {
                        SoundManager.playSound("sfx/HarddropSolo.wav", false);
                    }
                }
                break;

            case 37: // Left Arrow Key
                arrows[0] = true;
                arrows[2] = false;

                if (timer != null) {
                    repeatLeft();
                }

                board.move(-1);
                break;

            case 38: // Up Arrow Key
                if (board.currentTetrimino != null && board.currentTetrimino.direction != -1)
                    board.currentTetrimino.rotate(1);
                break;

            case 39: // Right Arrow Key
                arrows[1] = true;
                arrows[3] = false;

                if (timer != null) {
                    repeatRight();
                }

                board.move(1);
                break;

            case 40: // Down Arrow Key
                board.moveTetriminos();
                if (client != null) {
                    SoundManager.playSound("sfx/Softdrop.wav", false);
                } else {
                    SoundManager.playSound("sfx/Action.wav", false);
                }
                break;

            case 47: // Slash key
                if (prompt == null)
                    chat.openChat();
                break;

            case 67: // C Key (Holding)
                if (!board.held)
                    board.hold();
                break;

            case 90: // Z Key
                if (board.currentTetrimino != null && board.currentTetrimino.direction != -1)
                    board.currentTetrimino.rotate(-1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case 37: // Left Arrow Key
                arrows[0] = false;
                arrows[2] = true;
                direction = 0;
                inputDelay = 0;
                break;

            case 39: // Right Arrow Key
                arrows[1] = false;
                arrows[3] = true;
                direction = 0;
                inputDelay = 0;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent ke) {
        if (menu != null)
            menu.buttonInteractions(ke);
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (menu != null)
            menu.buttonInteractions(me);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (menu != null)
            menu.buttonInteractions(me);
    }

    // Launches the Game
    public static void main(String[] args) {
        new MyGame().start(TITLE, SCREEN_WIDTH, SCREEN_HEIGHT);
    }
}