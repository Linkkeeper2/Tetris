import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class MyGame extends Game  {
    public static final String TITLE = "Tetris";
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 800;

    // declare variables here
    public static TetriminoNode[][] board;
    public static int offset;
    public static Timer timer;
    private static Tetriminos pieces;
    private static Tetrimino currentTetrimino, nextTetrimino, heldTetrimino;
    private static int tNum = 0; // Number of Tetriminos used (Keeps track of ID for each Tetrimino)
    private static boolean hardDropping = false; // Determines whether or not the regular drop interval should occur (prevents Tetrimino clipping while Hard Dropping)
    private static int speed = 1000; // The millisecond delay between automatic Tetrimino movement
    private static int lines = 0; // The number of lines cleared
    private static int score = 0; // The total score of the player
    public static int level = 1; // The level (speed) of the game
    private static boolean alive = false;
    private static boolean held = false; // Has the player held a piece on the current turn?
    public static Menus menus;
    public static Menu menu;
    private Menu.Text message; // Message for line clears
    private Menu.Text levelMessage; // Message for level ups
    public static ColorPalette palette;
    private int messageCol; // Column to display line clear messages
    private int messageDirection; // Direction to move message when clearing lines
    private boolean[] arrows = new boolean[2]; // Determines whether or not to repeated left or right movement
    private int direction = 0; // -1 = Left, 1 = Right, 0 = None
    private long inputDelay = 400; // Delay for repeating directional inputs
    private int prevLinesCleared = 0; // Previous amount of lines cleared to score Back-to-Back Tetrises
    private static int pity = 0; // Pity to getting an IPiece
    private static int nextPity = 5; // Value that pity needs to surpass the get an IPiece

    // Client data
    public static Client client;
    public static Server server;
    public static ServerStatus status; // Status messages to display in game
    public static Chat chat; // Chat between players
    public static TextBox prompt;
    public static Menu.Button disconnect;
    public static Menu.Button addBot;
    public static ArrayList<Bot> bots;
    private int timesCleared = 3; // The amount of times the player has cleared lines in order to send lines to other clients
    private int linesToSend; // Amount of lines to send to other clients when a threshold is reached
    private static int clock = 10; // Clock for multiplayer games

    public MyGame() {
        // initialize variables here
        chat = new Chat();
        status = new ServerStatus();
        menus = new Menus();
        menu = menus.new MainMenu();
        message = new Menu().new Text("", 0, 0, Color.WHITE);
        levelMessage = new Menu().new Text("", 0, 700, Color.WHITE);
        palette = new ColorPalette();
        timer = new Timer();
        pieces = new Tetriminos();
        bots = new ArrayList<>();

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
                        //SoundManager.stopAllSounds();
                        //SoundManager.playSound("sfx/BattleHalf.wav", false);
                        break;

                    case 60:
                        //SoundManager.stopAllSounds();
                        //SoundManager.playSound("sfx/BattleMinute.wav", false);
                        break;
                }
            }
        }, (long)1000, 1000);
    }

    public static void startGame() {
        alive = true;
        lines = 0;
        score = 0;
        level = 1;
        speed = 1000;
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
            //SoundManager.playSound("sfx/Battle.wav", true);
            clock = 10;
        }
        else {
            //SoundManager.playSound("sfx/Battle.wav", false);
            clock = 300;
            MyGame.client.deaths = 0;
        }
    }

    public static void reset() {
        alive = true;
        lines = 0;
        score = 0;
        level = 1;
        speed = 1000;
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

        if (MyGame.client != null) MyGame.client.deaths++;
    }
    
    public void update() {
        // updating logic
        if (currentTetrimino == null && alive) {
            clearRow();
            held = false;
            swapTetriminos();
            move(1);
        } else if (currentTetrimino != null) {
            messageCol = currentTetrimino.getNodes()[0].col;

            if (direction != 0) {
                move(direction);
            }
        }

        updateArray();

        if (!message.contents.equals("")) {
            message.y--;
            message.x += messageDirection == 0 ? 1 : -1;
        }

        if (menu == null && !alive && client != null) {
            client.output.println(client.name + " has topped out.");
            reset();
            //SoundManager.playSound("sfx/KO.wav", false);
        }

        if (clock <= 0 && server != null && menu == null) {
            client.output.println("... ... ... ... ... ... ... Game over.");
            endGame();
        }
    }
    
    public void draw(Graphics pen) {
        if (menu == null) {
            // Draws the Board Tiles
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] == null) {
                        pen.setColor(Color.gray);
                        pen.fillRect(c * 25 + offset, r * 25 + offset, 25, 25);
                        pen.setColor(Color.DARK_GRAY);
                        pen.drawRect(c * 25 + offset, r * 25 + offset, 25, 25);
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
                        pen.setColor(curr.getColor());
                        pen.fillRect(curr.col * 25 + offset, curr.row * 25 + offset, 25, 25);
                        pen.setColor(curr.getDarkColor());
                        pen.drawRect(curr.col * 25 + offset, curr.row * 25 + offset, 25, 25);

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
                                        pen.fillRect(c * 25 + offset, i * 25 + offset, 25, 25);
                                        pen.setColor(Color.GRAY);
                                        pen.drawRect(c * 25 + offset, i * 25 + offset, 25, 25);
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
            pen.drawString("Lines: " + lines, offset + board[0].length * 25 + 8, offset - 24);
            pen.drawString("Score: " + score, offset + board[0].length * 25 + 8, offset);
            pen.drawString("Level: " + level, 32, 60);
            pen.drawString("Next", offset + board[0].length * 25 + 48, offset + 40);
            pen.drawString("Hold", 32, offset + 40);
            
            if (client != null) drawClock(pen);
            
            message.draw(pen);
            levelMessage.draw(pen);
    
            if (alive) {
                if (nextTetrimino != null) {
                    TetriminoNode[] nodes = nextTetrimino.getNodes();
                
                    // Draws the next Tetrimino
                    for (int i = 0; i < nodes.length; i++) {
                        TetriminoNode node = nodes[i];
                        node.updateColor();
                        pen.setColor(node.getColor());
                        pen.fillRect(node.col * 25 + (offset + board[0].length * 25 - 25), node.row * 25 + offset + 60, 25, 25);
                        pen.setColor(node.getDarkColor());
                        pen.drawRect(node.col * 25 + (offset + board[0].length * 25 - 25), node.row * 25 + offset + 60, 25, 25);
                    }
                }

                if (heldTetrimino != null) {
                    TetriminoNode[] nodes = heldTetrimino.getNodes();

                    // Draws the held Tetrimino
                    for (int i = 0; i < nodes.length; i++) {
                        TetriminoNode node = nodes[i];
                        node.updateColor();
                        pen.setColor(node.getColor());
                        pen.fillRect(node.col * 25 - 40, node.row * 25 + 175, 25, 25);
                        pen.setColor(node.getDarkColor());
                        pen.drawRect(node.col * 25 - 40, node.row * 25 + 175, 25, 25);
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
                currentTetrimino = null;
                return;
            } else if (board[nodes[i].row + 1][nodes[i].col] != null && board[nodes[i].row + 1][nodes[i].col].id != nodes[i].id) {
                currentTetrimino = null;
                return;
            }
        }

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
        if (!alive) return;

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
        //SoundManager.playSound("sfx/Move.wav", false);
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
        if (!alive) return;

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

                message.x = messageCol * 25 + offset;
                message.y = r * 25 + offset;

                speedCalculation();

                for (int i = r; i > 0; i--) {
                    board[i] = new TetriminoNode[board[0].length];
                    boolean stop = true;
                    
                    for (int k = 0; k < board[i].length; k++) {
                        if (board[i - 1][k] != null) stop = false;
                        board[i][k] = board[i - 1][k];
                        if (board[i][k] != null) board[i][k].row++;
                    }

                    if (stop) break;
                }
            }
        }

        if (prevLinesCleared == 4 && linesCleared == 4) {
            prevLinesCleared = linesCleared;
            linesCleared++;
        } else {
            if (linesCleared > 0) prevLinesCleared = linesCleared;
        }
        

        switch (linesCleared) {
            case 1:
                score += 100;
                message.contents = "+100 Single!";
                //SoundManager.playSound("sfx/Single.wav", false);
                break;

            case 2:
                score += 400;
                message.contents = "+400 Double!";
                //SoundManager.playSound("sfx/Double.wav", false);
                break;

            case 3:
                score += 800;
                message.contents = "+800 Triple!";
                //SoundManager.playSound("sfx/Triple.wav", false);
                break;

            case 4:
                score += 1600;
                message.contents = "+1600 Tetris!";
                //SoundManager.playSound("sfx/Tetris.wav", false);
                break;

            case 5:
                score += 2000;
                message.contents = "+2000 Back-to-Back Tetris!";
                //SoundManager.playSound("sfx/Tetris.wav", false);
                break;
        }

        sendLines(linesCleared);

        messageDirection = (int)(Math.random() * 2);

        timer.schedule(new TimerTask() {
            public void run() {
                message.contents = "";
            }
        }, (long)750);
    }

    public void hardDrop() {
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
        //SoundManager.playSound("sfx/Harddrop.wav", false);
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

        if (!alive) {
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
        // Swaps the current Tetrimino with the one in the next box
        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < board[i].length; k++) {
                if (board[i][k] != null) {
                    alive = false;
                }
            }
        }

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

        currentTetrimino.setID(tNum);
        tNum++;

        nextTetrimino = getNextTetrimino();
    }

    public void speedCalculation() {
        if (lines % 10 == 0) {
            level++;
            levelMessage.contents = "Level Up!";

            if (level == 7) {
                //SoundManager.stopAllSounds();
                //SoundManager.loopTime = (2 * 60 + 50) * 1000;
                //SoundManager.playSound("sfx/BattleHalf.wav", true);
            }

            //SoundManager.playSound("sfx/LUp.wav", false);

            timer.schedule(new TimerTask() {
                public void run() {
                    levelMessage.contents = "";
                }
            }, (long)750);

            if (palette.currentPalette < palette.getColors().length - 1) palette.currentPalette++;
            else palette.currentPalette = 0;

            if (speed > 100) speed -= 100;
            else {
                if (speed > 20) speed -= 5;
            }
        }
    }

    public static void hold() {
        if (menu != null) return;

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
                    break;
    
                case "SPiece":
                    currentTetrimino = pieces.new SPiece();
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
        //SoundManager.playSound("sfx/Hold.wav", false);
    }

    public static void recieveLines(int lines) {
        // Sends lines to the other players when a lines are cleared
        if (menu == null && alive) {
            int row = board.length - 1;

            for (int i = 0; i < lines; i++) {
                for (int r = 2; r < board.length - 1; r++) {
                    for (int c = 0; c < board[r].length; c++) {
                        if (board[r + 1][c] != null) {
                            boolean shift = true; // Prevents the falling Tetrimino from shifting

                            if (currentTetrimino != null) {
                                if (board[r + 1][c] != null && board[r + 1][c].parent != null && board[r + 1][c].parent.equals(currentTetrimino)) shift = false;
                            }

                            if (shift) {
                                board[r][c] = board[r + 1][c];
                                board[r + 1][c] = null;
                                board[r][c].row--;
                                row = board[r][c].row + 1;
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

            //SoundManager.playSound("sfx/Alert.wav", false);
        }
    }

    public void sendLines(int linesCleared) {
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
                if (linesToSend < 5) linesToSend++;
            }
        }
    }

    public static void exitToMenu() {
        menu = menus.new MainMenu();
        //SoundManager.stopAllSounds();
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
            server = null;
            prompt = null;
            menu.buttons.remove(disconnect);
            //menu.buttons.remove(addBot);
            bots.clear();
            disconnect = null;
        }

        //SoundManager.stopAllSounds();
    }

    public static void endGame() {
        //SoundManager.stopAllSounds();
        menu = new Menus().new ResultsMenu();
        clock = 10;
        client.output.println(client.name + " " + client.deaths + " ... ... ... ... deaths.");
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
                if (prompt == null) hardDrop();
                break;

            case 37: // Left Arrow Key
                arrows[0] = true;

                if (timer != null) {
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (arrows[0]) direction = -1;
                        }
                    }, inputDelay);
                }

                move(-1);
                break;

            case 38: // Up Arrow Key
                if (currentTetrimino != null && currentTetrimino.direction != -1) currentTetrimino.rotate(1);
                break;

            case 39: // Right Arrow Key
                arrows[1] = true;

                if (timer != null) {
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (arrows[1]) direction = 1;
                        }
                    }, inputDelay);
                }
                
                move(1);
                break;

            case 40: // Down Arrow Key
                moveTetriminos();
                //SoundManager.playSound("sfx/Softdrop.wav", false);
                break;

            case 47: // Slash key
                chat.openChat();
                break;

            case 67: // C Key (Holding)
                if (!held) hold();
                break;

            case 90: // Z Key
                if (currentTetrimino != null && currentTetrimino.direction != -1) currentTetrimino.rotate(-1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case 37: // Left Arrow Key
                arrows[0] = false;
                direction = 0;
                break;


            case 39: // Right Arrow Key
                arrows[1] = false;
                direction = 0;
                break;
        }
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