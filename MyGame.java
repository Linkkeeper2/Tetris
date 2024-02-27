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
    public static TetriminoNode[][] board;
    public static int offset;
    public static Timer timer;
    private static Tetriminos pieces;
    private static Tetrimino currentTetrimino, nextTetrimino, heldTetrimino;
    public static int tNum = 0; // Number of Tetriminos used (Keeps track of ID for each Tetrimino)
    private static boolean hardDropping = false; // Determines whether or not the regular drop interval should occur (prevents Tetrimino clipping while Hard Dropping)
    private static int speed = 1000; // The millisecond delay between automatic Tetrimino movement
    private static int lines = 0; // The number of lines cleared
    private static int score = 0; // The total score of the player
    public static short level = 0; // The level (speed) of the game
    public static boolean alive = false;
    private static boolean held = false; // Has the player held a piece on the current turn?
    public static Menus menus;
    public static Menu menu;
    private ArrayList<Message> messages; // Message for line clears
    private Menu.Text levelMessage; // Message for level ups
    public static ColorPalette palette;
    private int messageCol; // Column to display line clear messages
    private int messageRow; // Row to display line clear messages
    private int messageDirection; // Direction to move message when clearing lines
    private boolean[] arrows = new boolean[4]; // Determines whether or not to repeated left or right movement
    private int direction = 0; // -1 = Left, 1 = Right, 0 = None
    private int inputDelay = 0; // Delay for repeating directional inputs
    private int prevLinesCleared = 0; // Previous amount of lines cleared to score Back-to-Back Tetrises
    private static int pity = 0; // Pity to getting an IPiece
    private static int nextPity = 5; // Value that pity needs to surpass the get an IPiece
    public static boolean notMove = false; // Used to delay Tetrimino downwards movement when a key is pressed
    public static boolean tSpin = false; // Determines if a T-Spin should occur
    public static boolean doActions = true; // Prevents Tetrimino movement if an action is being processed
    public static int tileSize = 16; // Size of each tile on the board

    // Client data
    public static Client client;
    public static Server server;
    public static ServerStatus status; // Status messages to display in game
    public static Chat chat; // Chat between players
    public static TextBox prompt;
    public static Menu.Button disconnect;
    public static Menu.Button addBot;
    public static ArrayList<Bot> bots;
    private static int timesCleared = 0; // The amount of times the player has cleared lines in order to send lines to other clients
    public static int linesToSend; // Amount of lines to send to other clients when a threshold is reached
    private static int clock = 10; // Clock for multiplayer games
    public static boolean recieving = false;

    public static Save save; // Save file properties for the player

    private static ClearAnimation animation;

    public static Database database;
    public static Account account;

    public MyGame() {
        // initialize variables here
        database = new Database();
        account = new Account();
        chat = new Chat();
        status = new ServerStatus();
        menus = new Menus();
        menu = menus.new MainMenu();
        messages = new ArrayList<>();
        levelMessage = new Menu().new Text("", 0, 700, Color.WHITE);
        palette = new ColorPalette();
        timer = new Timer();
        pieces = new Tetriminos();
        bots = new ArrayList<>();
        save = new Save();
        animation = new ClearAnimation();
        animation.start();

        timer.schedule(new TimerTask() {
            public void run() {
                automaticMove();
            }
        }, (long)speed);

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
        }, (long)1000, 1000);

        account.login();
    }

    public static void startGame() {
        alive = true;
        lines = 0;
        score = 0;
        timesCleared = 0;
        try {
            if (client == null) {
                level = save.startLevel;
                tileSize = 16;
                palette.sheet = new SpriteSheetLoader(16, 16, 10, 3);
            }
            else {
                level = 0;
                tileSize = 25;
                palette.sheet = new SpriteSheetLoader(25, 25, 10, 3, "gfx/PaletteBattle.png");
            }
        } catch (IOException e) {}

        pity = 0;
        nextPity = 5;
        palette.currentPalette = 0;
        menu = null;
        board = new TetriminoNode[20][10];
        if (client != null) recieveLines(0);
        offset = 125;

        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();
        heldTetrimino = null;

        updateArray();
        move(1);
        if (client == null) {
            if (level < 9) SoundManager.playSound("sfx/MusicSolo.wav", true);
            else if (level < 19) SoundManager.playSound("sfx/Level9.wav", true);
            else SoundManager.playSound("sfx/Level19.wav", true);
            clock = 10;
        }
        else {
            SoundManager.playSound("sfx/Battle.wav", false);
            clock = 300;
            MyGame.client.deaths = 0;
        }
    }

    public static void reset() {
        alive = true;
        lines = 0;
        score = 0;
        level = 0;
        pity = 0;
        nextPity = 5;
        palette.currentPalette = 0;
        menu = null;
        board = new TetriminoNode[20][10];
        if (client != null) recieveLines(0);
        offset = 125;

        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();
        heldTetrimino = null;

        updateArray();
        move(1);

        if (client != null) {
            client.deaths++;
            client.queue.clear();
        }
    }
    
    public void update() {
        // updating logic
        if (currentTetrimino == null && alive) {
            if (animation.rowsToClear.size() == 0) clearRow();

            if (animation.rowsToClear.size() == 0) {
                held = false;
                swapTetriminos();
                move(1);
                notMove = false;
            }
        } else if (currentTetrimino != null) {
            messageCol = currentTetrimino.getNodes()[0].col;
            messageRow = currentTetrimino.getNodes()[0].row;

            if (direction != 0) {
                move(direction);
            }
        }

        updateArray();

        for (int i = 0; i < messages.size(); i++) {
            if (i < messages.size()) {
                Message m = messages.get(i);
                if (m != null) {
                    m.y--;
                    m.x += messageDirection == 0 ? 1 : -1;
                    m.updatePosition();
                }
            }
        }

        if (menu == null && !alive && client != null) {
            client.output.println(client.name + " has topped out.");
            reset();
            SoundManager.playSound("sfx/KO.wav", false);
        }

        if (clock <= 0 && server != null && menu == null) {
            client.output.println("... ... ... ... ... ... ... Game over.");
            endGame();
        }

        updateState();
    }
    
    public void updateState() {
        // Updates the speed of the game and color palette
        if (level <= 8) speed = (int)(((48 - level * 5) / 60f) * 1000);
        else if (level == 9) speed = (int)((6 / 60f) * 1000);
        else if (level >= 10 && level <= 12) speed = (int)((5 / 60f) * 1000);
        else if (level >= 13 && level <= 15) speed = (int)((4 / 60f) * 1000);
        else if (level >= 16 && level <= 18) speed = (int)((3 / 60f) * 1000);
        else if (level >= 19 && level <= 28) speed = (int)((2 / 60f) * 1000);
        else if (level >= 29) speed = (int)((1 / 60f) * 1000);

        palette.currentPalette = level % 10;
    }

    public void draw(Graphics pen) {
        if (menu == null) {
            // Draws the Board Tiles
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] == null) {
                        pen.setColor(Color.gray);
                        pen.fillRect(c * tileSize + offset, r * tileSize + offset, tileSize, tileSize);
                        pen.setColor(Color.DARK_GRAY);
                        pen.drawRect(c * tileSize + offset, r * tileSize + offset, tileSize, tileSize);
                    }
                }
            }

            // Draws the Tetrimino Nodes
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] != null) {
                        TetriminoNode curr = board[r][c];
                        
                        // Draws the current node to the board
                        curr.updateColor();
                        curr.draw(pen);

                        if (currentTetrimino != null) {
                            // Determines if the indicator for dropping the Tetrimino should be drawn for the current node
                            TetriminoNode[] nodes = currentTetrimino.getNodes();
                            boolean stop = true;

                            for (int i = 0; i < nodes.length; i++) {
                                if (nodes[i].equals(curr)) {
                                    stop = false;
                                    break;
                                }
                            }

                            if (!stop) {
                                // Draws the indicator for dropping the Tetrimino
                                for (int i = curr.row + 1; i < board.length; i++) {
                                    if (board[i][c] == null) {
                                        pen.setColor(Color.LIGHT_GRAY);
                                        pen.fillRect(c * tileSize + offset, i * tileSize + offset, tileSize, tileSize);
                                        pen.setColor(Color.GRAY);
                                        pen.drawRect(c * tileSize + offset, i * tileSize + offset, tileSize, tileSize);
                                    } else break;
                                }
                            }
                        }
                    }
                }
            }
            
            // Draws the Text to the screen
            pen.setFont(new Font("comicsansms", 0, 20));
            pen.setColor(Color.WHITE);
            pen.drawString("Lines: " + lines, offset + board[0].length * tileSize + 8, offset - 24);
            pen.drawString("Score: " + score, offset + board[0].length * tileSize + 8, offset);
            pen.drawString("Level: " + level, 32, 60);
            pen.drawString("Next", offset + board[0].length * tileSize + 48, offset + 40);
            pen.drawString("Hold", 32, offset + 40);
            
            if (client != null) drawClock(pen);
            
            for (int i = 0; i < messages.size(); i++) {
                messages.get(i).draw(pen);
            }

            levelMessage.draw(pen);
    
            if (alive) {
                if (nextTetrimino != null) {
                    TetriminoNode[] nodes = nextTetrimino.getNodes();
                
                    // Draws the next Tetrimino
                    for (int i = 0; i < nodes.length; i++) {
                        TetriminoNode node = nodes[i];
                        node.updateColor();
                        pen.drawImage(node.sprite, node.col * tileSize + (offset + board[0].length * tileSize - tileSize), node.row * tileSize + offset + 60, null);
                    }
                }

                if (heldTetrimino != null) {
                    TetriminoNode[] nodes = heldTetrimino.getNodes();

                    // Draws the held Tetrimino
                    for (int i = 0; i < nodes.length; i++) {
                        TetriminoNode node = nodes[i];
                        node.updateColor();
                        pen.drawImage(node.sprite, node.col * tileSize - 20, node.row * tileSize + 175, null);
                    }
                }
            }
        } else {
            menu.draw(pen);
        }

        status.draw(pen);
        if (prompt != null) prompt.draw(pen);
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
        
        if (c < 10) s += "0" + c;
        else s += c;

        pen.setColor(Color.WHITE);
        pen.drawString("Time Left " + s, offset + board[0].length * 25 + 8, offset - 48);
    }

    public static void moveTetriminos() {
        if (!alive || menu != null) return;

        if (currentTetrimino == null) return;

        TetriminoNode[] nodes = currentTetrimino.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].row >= board.length - 1) {
                if (!notMove) currentTetrimino = null;
                return;
            }
            else if (board[nodes[i].row + 1][nodes[i].col] != null && board[nodes[i].row + 1][nodes[i].col].parent == null) {
                if (!notMove) currentTetrimino = null;
                return;
            } 
            else if (board[nodes[i].row + 1][nodes[i].col] != null && !board[nodes[i].row + 1][nodes[i].col].parent.equals(nodes[i].parent)) {
                if (!notMove) currentTetrimino = null;
                return;
            }
        }

        tSpin = false;

        if (!hardDropping) {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].row++;
            }
    
            TetriminoNode[][] rotations = currentTetrimino.getRotations();
    
            if (rotations != null) {
                for (int r = 0; r < rotations.length; r++) {
                    for (int c = 0; c < rotations[r].length; c++) {
                        if (rotations[r][c] != null) {
                            if (nodes.equals(rotations[r])) break;
                            
                            rotations[r][c].row++;
                        }
                    }
                }
            }
    
            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                board[node.row][node.col] = node;
            }
    
            updateArray();
        }
    }
    
    public static void add(TetriminoNode t, int row, int col) {
        if (board[row][col] != null) alive = false;
        board[row][col] = t;
    }

    public static void updateArray() {
        // Updates the the board used to move Tetriminos
        if (menu == null) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] != null) {
                        TetriminoNode node = board[r][c];
                        if (node.row != r || node.col != c) board[r][c] = null;
                    }
                }
            }
        }
    }

    public static void move(int direction) {
        if (!alive || !doActions) return;

        if (currentTetrimino == null) return;

        TetriminoNode[] nodes = currentTetrimino.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            switch (direction) {
                case -1:
                    if (nodes[i].col <= 0) return;
                    break;

                case 1:
                    if (nodes[i].col >= board[0].length - 1) return;
                    break;
            }

            if (board[nodes[i].row][nodes[i].col + 1 * direction] != null && board[nodes[i].row][nodes[i].col + 1 * direction].id != nodes[i].id) return;
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i].col += 1 * direction;
        }

        TetriminoNode[][] rotations = currentTetrimino.getRotations();

        if (rotations != null) {
            for (int r = 0; r < rotations.length; r++) {
                for (int c = 0; c < rotations[r].length; c++) {
                    if (rotations[r][c] != null) {
                        boolean move = true;
                        if (nodes.equals(rotations[r])) move = false;
                        if (move) rotations[r][c].col += 1 * direction;
                    }
                }
            }
        }

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            board[node.row][node.col] = node;
        }

        updateArray();
        if (client != null) {
            SoundManager.playSound("sfx/Move.wav", false);
        }
        else {
            SoundManager.playSound("sfx/Action.wav", false);
        }
    }

    public static Tetrimino getTetrimino() {
        if (!alive) return null;

        int num = (int)(Math.random() * 7);
        Tetrimino t = null;

        switch (num) {
            case 0:
                t = pieces.new IPiece();
                break;

            case 1:
                t = pieces.new TPiece();
                break;

            case 2:
                t = pieces.new ZPiece();
                break;

            case 3:
                t = pieces.new SPiece();
                break;

            case 4:
                t = pieces.new OPiece();
                break;

            case 5:
                t = pieces.new LPiece();
                break;

            case 6:
                t = pieces.new JPiece();
                break;
        }

        t.setID(tNum);
        tNum++;
        held = false;

        return t;
    }

    public static Tetrimino getNextTetrimino() {
        if (!alive) return null;

        int num = (int)(Math.random() * 7);
        Tetrimino t = null;

        if (pity >= nextPity) {
            pity = 0;
            nextPity = (int)(Math.random() * 5) + 5;
            num = 0;
        }

        switch (num) {
            case 0:
                t = pieces.new IPiece(false);
                pity = 0;
                break;

            case 1:
                t = pieces.new TPiece(false);
                break;

            case 2:
                t = pieces.new ZPiece(false);
                pity++;
                break;

            case 3:
                t = pieces.new SPiece(false);
                pity++;
                break;

            case 4:
                t = pieces.new OPiece(false);
                pity++;
                break;

            case 5:
                t = pieces.new LPiece(false);
                pity++;
                break;

            case 6:
                t = pieces.new JPiece(false);
                pity++;
                break;
        }

        return t;
    }

    public void clearRow() {
        if (!alive || board == null) return;

        int linesCleared = 0;

        for (int r = 0; r < board.length; r++) {
            boolean clear = true;

            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    clear = false;
                    break;
                }
            }

            if (clear) {
                lines++;
                linesCleared++;

                messageRow = r;
                animation.rowsToClear.add(r);

                speedCalculation();
            }
        }

        if (prevLinesCleared == 4 && linesCleared == 4) {
            prevLinesCleared = linesCleared;
            linesCleared++;
        } else {
            if (linesCleared > 0) prevLinesCleared = linesCleared;
        }
        
        int scoreAdded = 0;
        switch (linesCleared) {
            case 1:
                scoreAdded += 40 * (level + 1);
                score += 40 * (level + 1);
                if (!tSpin) messages.add(new Message("+" + scoreAdded + " Single!"));
                else {
                    scoreAdded += 50 * (level + 1);
                    messages.add(new Message("+" + scoreAdded + " T-Spin Single!"));
                    score += 50 * (level + 1);
                }
                if (client != null) {
                    SoundManager.playSound("sfx/Single.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/LineClearSolo.wav", false);
                }
                break;

            case 2:
                score += 100 * (level + 1);
                scoreAdded += 100 * (level + 1);
                if (!tSpin) messages.add(new Message("+" + scoreAdded + " Double!"));
                else {
                    scoreAdded += 100 * (level + 1);
                    messages.add(new Message("+" + scoreAdded + " T-Spin Double!"));
                    score += 100 * (level + 1);
                }
                if (client != null) {
                    SoundManager.playSound("sfx/Double.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/LineClearSolo.wav", false);
                }
                break;

            case 3:
                scoreAdded += 300 * (level + 1);
                score += 300 * (level + 1);
                if (!tSpin) messages.add(new Message("+" + scoreAdded + " Triple!"));
                else {
                    scoreAdded += 300 * (level + 1);
                    messages.add(new Message("+" + scoreAdded + " T-Spin Triple!"));
                    score += 300 * (level + 1);
                }
                if (client != null) {
                    SoundManager.playSound("sfx/Triple.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/LineClearSolo.wav", false);
                }
                break;

            case 4:
                scoreAdded += 1200 * (level + 1);
                score += 1200 * (level + 1);
                messages.add(new Message("+" + scoreAdded + " Tetris!"));
                if (client != null) {
                    SoundManager.playSound("sfx/Tetris.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/TetrisSolo.wav", false);
                }
                break;

            case 5:
                scoreAdded += 2000 * (level + 1);
                score += 2000 * (level + 1);
                messages.add(new Message("+" + scoreAdded + " Back-to-Back Tetris!"));
                if (client != null) {
                    SoundManager.playSound("sfx/Tetris.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/TetrisSolo.wav", false);
                }
                break;
        }

        if (linesCleared > 0 && client != null) {
            if (client.queue.size() == 0) sendLines(linesCleared);
            else client.changeTimer(linesCleared);
        }
        
        tSpin = false;

        messageDirection = (int)(Math.random() * 2);

        if (messages.size() > 0) {
            Message msg = messages.get(messages.size() - 1);
            msg.x = messageCol * tileSize + offset;
            msg.y = messageRow * tileSize + offset;

            timer.schedule(new TimerTask() {
                public void run() {
                    messages.remove(msg);
                }
            }, (long)750);
        }
    }

    public void hardDrop() {
        tSpin = false;
        if (!alive) return;

        hardDropping = true;

        for (int k = 0; k < board.length; k++) {
            if (currentTetrimino == null) {
                hardDropping = false;
                return;
            }

            TetriminoNode[] nodes = currentTetrimino.getNodes();

            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].row >= board.length - 1) {
                    currentTetrimino = null;
                    hardDropping = false;
                    held = false;
                    return;
                } else if (board[nodes[i].row + 1][nodes[i].col] != null && board[nodes[i].row + 1][nodes[i].col].id != nodes[i].id) {
                    currentTetrimino = null;
                    hardDropping = false;
                    held = false;
                    return;
                }
            }

            for (int i = 0; i < nodes.length; i++) {
                nodes[i].row++;
            }
    
            TetriminoNode[][] rotations = currentTetrimino.getRotations();
    
            if (rotations != null) {
                for (int r = 0; r < rotations.length; r++) {
                    for (int c = 0; c < rotations[r].length; c++) {
                        if (rotations[r][c] != null) {
                            boolean move = true;
                            if (nodes.equals(rotations[r])) move = false;
                            
                            if (move) rotations[r][c].row++;
                        }
                    }
                }
            }
    
            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                board[node.row][node.col] = node;
            }
    
            updateArray();
        }

        hardDropping = false;
        held = false;
    }

    public static void automaticMove() {
        // Used for automatic Tetrimino movement to reschedule the task
        int time = speed;
        if (client != null) {
            if (clock <= 20) time /= 7;
            else if (clock <= 30) time /= 6;
            else if (clock <= 60) time /= 5;
            else if (clock <= 100) time /= 4;
            else if (clock <= 150) time /= 3;
            else if (clock <= 225) time /= 2;
        }

        if (!alive || !doActions) {
            timer.schedule(new TimerTask() {
                public void run() {
                    automaticMove();
                }
            }, (long)time);
            return;
        }

        moveTetriminos();
        timer.schedule(new TimerTask() {
            public void run() {
                automaticMove();
            }
        }, (long)time);
    }

    public void swapTetriminos() {
        if (board == null) return;

        if (nextTetrimino == null) return;

        switch (nextTetrimino.getType()) {
            case "IPiece":
                currentTetrimino = pieces.new IPiece();
                break;

            case "TPiece":
                currentTetrimino = pieces.new TPiece();
                break;

            case "ZPiece":
                currentTetrimino = pieces.new ZPiece();
                currentTetrimino.rotate(1);
                break;

            case "SPiece":
                currentTetrimino = pieces.new SPiece();
                currentTetrimino.rotate(1);
                break;

            case "OPiece":
                currentTetrimino = pieces.new OPiece();
                break;

            case "LPiece":
                currentTetrimino = pieces.new LPiece();
                break;

            case "JPiece":
                currentTetrimino = pieces.new JPiece();
                break;
        }

        if (currentTetrimino != null) currentTetrimino.setID(tNum);
        tNum++;

        nextTetrimino = getNextTetrimino();
    }

    public void speedCalculation() {
        if ((level + 1) * 10 - lines <= 0) {
            level++;
            levelMessage.contents = "Level Up!";

            if (client != null) {
                SoundManager.playSound("sfx/LUp.wav", false);
            }
            else {
                SoundManager.playSound("sfx/LevelUpSolo.wav", false);
            }

            if (level == 9) {
                SoundManager.stopAllSounds();
                SoundManager.playSound("sfx/Level9.wav", true);
            }
            else if (level == 19) {
                SoundManager.stopAllSounds();
                SoundManager.playSound("sfx/Level19.wav", true);
            }

            timer.schedule(new TimerTask() {
                public void run() {
                    levelMessage.contents = "";
                }
            }, (long)750);
        }
    }

    public static void hold() {
        if (menu != null || currentTetrimino == null) return;

        for (int i = 0; i < currentTetrimino.nodes.length; i++) {
            board[currentTetrimino.nodes[i].row][currentTetrimino.nodes[i].col] = null;
        }

        if (heldTetrimino == null) {
            switch (currentTetrimino.getType()) {
                case "IPiece":
                    heldTetrimino = pieces.new IPiece(false);
                    break;
    
                case "TPiece":
                    heldTetrimino = pieces.new TPiece(false);
                    break;
    
                case "ZPiece":
                    heldTetrimino = pieces.new ZPiece(false);
                    break;
    
                case "SPiece":
                    heldTetrimino = pieces.new SPiece(false);
                    break;
    
                case "OPiece":
                    heldTetrimino = pieces.new OPiece(false);
                    break;
    
                case "LPiece":
                    heldTetrimino = pieces.new LPiece(false);
                    break;
    
                case "JPiece":
                    heldTetrimino = pieces.new JPiece(false);
                    break;
            }
            
            currentTetrimino = null;
        } else {
            String type = currentTetrimino.getType();

            switch (heldTetrimino.getType()) {
                case "IPiece":
                    currentTetrimino = pieces.new IPiece();
                    break;
    
                case "TPiece":
                    currentTetrimino = pieces.new TPiece();
                    break;
    
                case "ZPiece":
                    currentTetrimino = pieces.new ZPiece();
                    currentTetrimino.rotate(1);
                    break;
    
                case "SPiece":
                    currentTetrimino = pieces.new SPiece();
                    currentTetrimino.rotate(1);
                    break;
    
                case "OPiece":
                    currentTetrimino = pieces.new OPiece();
                    break;
    
                case "LPiece":
                    currentTetrimino = pieces.new LPiece();
                    break;
    
                case "JPiece":
                    currentTetrimino = pieces.new JPiece();
                    break;
            }

            switch (type) {
                case "IPiece":
                    heldTetrimino = pieces.new IPiece(false);
                    break;
    
                case "TPiece":
                    heldTetrimino = pieces.new TPiece(false);
                    break;
    
                case "ZPiece":
                    heldTetrimino = pieces.new ZPiece(false);
                    break;
    
                case "SPiece":
                    heldTetrimino = pieces.new SPiece(false);
                    break;
    
                case "OPiece":
                    heldTetrimino = pieces.new OPiece(false);
                    break;
    
                case "LPiece":
                    heldTetrimino = pieces.new LPiece(false);
                    break;
    
                case "JPiece":
                    heldTetrimino = pieces.new JPiece(false);
                    break;
            }
        }

        held = true;
        if (client != null) {
            SoundManager.playSound("sfx/Hold.wav", false);
        }
        else {
            SoundManager.playSound("sfx/Action.wav", false);
        }
    }

    public static void recieveLines(int lines) {
        // Recieves lines in the queue
        if (animation.clearing) {
            timer.schedule(new TimerTask() {
                public void run() {
                    recieveLines(lines);
                }
            }, (long)100);
            return;
        } 

        if (menu == null && alive) {
            int row = board.length - 1;

            for (int i = 0; i < lines; i++) {
                for (int r = 2; r < board.length - 1; r++) {
                    for (int c = 0; c < board[r].length; c++) {
                        if (board[r + 1][c] != null) {
                            board[r][c] = board[r + 1][c];
                            board[r + 1][c] = null;

                            if (board[r][c] != null) {
                                row = --board[r][c].row;
                            }
                        }
                    }
                }
            }

            for (int r = row; r < board.length; r++) {
                int stop = (int)(Math.random() * board[r].length);
                for (int c = 0; c < board[r].length; c++) {
                    if (c != stop) {
                        board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                        board[r][c].updateID();
                        updateArray();
                    }
                }
            }

            SoundManager.playSound("sfx/Alert.wav", false);

            recieving = false;
        }
    }

    public static void sendLines(int linesCleared) {
        // Sends lines to the other players when a lines are cleared
        if (client != null && client.output != null) {
            if (linesToSend >= timesCleared) {
                if (linesCleared > 0) {
                    String recieve = client.lobby.get((int)(Math.random() * client.lobby.size())).contents;
                    while (recieve.equals(client.name) || recieve.equals("Lobby")) {
                        recieve = client.lobby.get((int)(Math.random() * client.lobby.size())).contents;
                    }
                    
                    client.output.println(client.name + " sent " + linesCleared + " lines to " + recieve);
                    status.addMessage("Sent " + linesCleared + " lines to " + recieve + ".");

                    if (recieve.startsWith("Bot")) {
                        for (int i = 0; i < bots.size(); i++) {
                            if (bots.get(i).name.equals(recieve)) {
                                bots.get(i).topRow -= linesCleared;
                            }
                        }
                    }

                    linesToSend = 0;
                    timesCleared = (int)(Math.random() * 4) + 1;
                }
            } else {
                if (linesToSend < 5) linesToSend += linesCleared;
            }
        }
    }

    public static void exitToMenu() {
        menu = menus.new MainMenu();
        SoundManager.stopAllSounds();
        if (client != null) client.queue.clear();
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
                } catch (UnknownHostException e) {}
            }

            server = null;
            prompt = null;
            menu.buttons.remove(disconnect);
            //menu.buttons.remove(addBot);
            bots.clear();
            disconnect = null;
        }

        SoundManager.stopAllSounds();
    }

    public static void endGame() {
        SoundManager.stopAllSounds();
        menu = new Menus().new ResultsMenu();
        clock = 10;
        client.output.println(client.name + " " + client.deaths + " ... ... ... ... deaths.");
        if (client != null) client.queue.clear();
    }

    public void repeatLeft() {
        if (arrows[0]) {
            if (inputDelay >= 150) {
                direction = -1;
                inputDelay = 0;
                return;
            }

            timer.schedule(new TimerTask() {
                public void run() {
                    inputDelay += 10;
                    repeatLeft();
                }
            }, (long)10);
        }
    }

    public void repeatRight() {
        if (arrows[1]) {
            if (inputDelay >= 150) {
                direction = 1;
                inputDelay = 0;
                return;
            }

            timer.schedule(new TimerTask() {
                public void run() {
                    inputDelay += 10;
                    repeatRight();
                }
            }, (long)10);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        if (prompt != null) prompt.type(ke);
        switch (ke.getKeyCode()) {
            case 27: // ESCAPE Key
                if (menu == null) {
                    if (server != null) {
                        exitToMenu();
                        client.output.println("Game Ended.");
                    } else {
                        if (client == null) exitToMenu();
                    }
                } else {
                    leaveGame();
                    
                    this.running = false;
                }
                break;

            case 32: // SPACE
                if (prompt == null) {
                    hardDrop();
                    if (client != null) {
                        SoundManager.playSound("sfx/Harddrop.wav", false);
                    }
                    else {
                        SoundManager.playSound("sfx/HarddropSolo.wav", false);
                    }
                }
                notMove = false;
                break;

            case 37: // Left Arrow Key
                arrows[0] = true;
                arrows[2] = false;

                if (timer != null) {
                    repeatLeft();
                }

                move(-1);
                notMove = true;
                break;

            case 38: // Up Arrow Key
                if (currentTetrimino != null && currentTetrimino.direction != -1) currentTetrimino.rotate(1);
                notMove = true;
                break;

            case 39: // Right Arrow Key
                arrows[1] = true;
                arrows[3] = false;

                if (timer != null) {
                    repeatRight();
                }
                
                move(1);
                notMove = true;
                break;

            case 40: // Down Arrow Key
                moveTetriminos();
                if (client != null) {
                    SoundManager.playSound("sfx/Softdrop.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/Action.wav", false);
                }
                break;

            case 47: // Slash key
                chat.openChat();
                break;

            case 67: // C Key (Holding)
                if (!held) hold();
                break;

            case 90: // Z Key
                if (currentTetrimino != null && currentTetrimino.direction != -1) currentTetrimino.rotate(-1);
                notMove = true;
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

        notMove = false;
    }

    @Override
    public void mouseClicked(MouseEvent ke) {
        if (menu != null) menu.buttonInteractions(ke);
    }

    @Override
    public void mousePressed(MouseEvent me) {}
    
    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}

    @Override
    public void mouseMoved(MouseEvent me) {
        if (menu != null) menu.buttonInteractions(me);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (menu != null) menu.buttonInteractions(me);
    }
        
    //Launches the Game
    public static void main(String[] args) { new MyGame().start(TITLE, SCREEN_WIDTH,SCREEN_HEIGHT); }
}