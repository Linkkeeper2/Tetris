import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class MyGame extends Game  {
    public static final String TITLE = "Tetris";
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 800;

    // declare variables here
    public static TetriminoNode[][] board;
    public static int offset;
    private static Timer timer;
    private static Tetriminos pieces;
    private static Tetrimino currentTetrimino, nextTetrimino, heldTetrimino;
    private static int tNum = 0; // Number of Tetriminos used (Keeps track of ID for each Tetrimino)
    private static boolean hardDropping = false; // Determines whether or not the regular drop interval should occur (prevents Tetrimino clipping while Hard Dropping)
    private static int speed = 1000; // The millisecond delay between automatic Tetrimino movement
    private int lines = 0; // The number of lines cleared
    private int score = 0; // The total score of the player
    private int level = 1; // The level (speed) of the game
    private static boolean alive = false;
    private static boolean held = false; // Has the player held a piece on the current turn?
    private Menus menus;
    private static Menu menu;
    private String lineString; // Displays, Single, Double, Triple, or Tetris

    public MyGame() {
        // initialize variables here
        menus = new Menus();
        menu = menus.new MainMenu();
        lineString = "";
    }

    public static void startGame() {
        alive = true;
        menu = null;
        board = new TetriminoNode[20][10];
        offset = 100;
        pieces = new Tetriminos();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                automaticMove();
            }
        }, (long)speed);
        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();

        updateArray();
    }
    
    public void update() {
        // updating logic
        if (currentTetrimino == null && alive) {
            clearRow();
            held = false;
            swapTetriminos();
        }

        updateArray();
    }
    
    public void draw(Graphics pen) {
        if (menu == null) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] == null) {
                        pen.setColor(Color.gray);
                        pen.fillRect(c * 25 + offset, r * 25 + offset, 25, 25);
                        pen.setColor(Color.DARK_GRAY);
                        pen.drawRect(c * 25 + offset, r * 25 + offset, 25, 25);
                    } else {
                        TetriminoNode curr = board[r][c];
                        
                        pen.setColor(curr.getColor());
                        pen.fillRect(curr.col * 25 + offset, curr.row * 25 + offset, 25, 25);
                        pen.setColor(curr.getDarkColor());
                        pen.drawRect(curr.col * 25 + offset, curr.row * 25 + offset, 25, 25);
                    }
                }
            }
            
            pen.setFont(new Font("comicsansms", 0, 20));
            pen.setColor(Color.BLACK);
            pen.drawString("Lines: " + lines, 0, 20);
            pen.drawString("Score: " + score, 0, 40);
            pen.drawString("Level: " + level, 0, 60);
            pen.drawString("Next", 0, 80);
            pen.drawString("Hold", 0, 220);
            pen.drawString(lineString, 0, 700);
    
            if (alive) {
                TetriminoNode[] nodes = nextTetrimino.getNodes();
    
                for (int i = 0; i < nodes.length; i++) {
                    TetriminoNode node = nodes[i];
                    pen.setColor(node.getColor());
                    pen.fillRect(node.row * 25 + 20, node.col * 25 + 35, 25, 25);
                }

                if (heldTetrimino != null) {
                    nodes = heldTetrimino.getNodes();

                    for (int i = 0; i < nodes.length; i++) {
                        TetriminoNode node = nodes[i];
                        pen.setColor(node.getColor());
                        pen.fillRect(node.row * 25 + 20, node.col * 25 + 175, 25, 25);
                    }
                }
            }
        } else {
            menu.draw(pen);
        }
    }

    public static void moveTetriminos() {
        if (!alive) return;

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

    public void move(int direction) {
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

        switch (num) {
            case 0:
                t = pieces.new IPiece(false);
                break;

            case 1:
                t = pieces.new TPiece(false);
                break;

            case 2:
                t = pieces.new ZPiece(false);
                break;

            case 3:
                t = pieces.new SPiece(false);
                break;

            case 4:
                t = pieces.new OPiece(false);
                break;

            case 5:
                t = pieces.new LPiece(false);
                break;

            case 6:
                t = pieces.new JPiece(false);
                break;
        }

        t.setID(tNum);
        tNum++;

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

        switch (linesCleared) {
            case 1:
                score += 100;
                lineString = "+100 Single!";
                break;

            case 2:
                score += 400;
                lineString = "+400 Double!";
                break;

            case 3:
                score += 800;
                lineString = "+800 Triple!";
                break;

            case 4:
                score += 1600;
                lineString = "+1600 Tetris!";
                break;
        }

        timer.schedule(new TimerTask() {
            public void run() {
                lineString = "";
            }
        }, (long)500);
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
    }

    public static void automaticMove() {
        // Used for automatic Tetrimino movement to reschedule the task
        if (!alive) return;

        moveTetriminos();
        timer.schedule(new TimerTask() {
            public void run() {
                automaticMove();
            }
        }, (long)speed);
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

        switch (nextTetrimino.getType()) {
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

        currentTetrimino.setID(tNum);
        tNum++;

        nextTetrimino = getNextTetrimino();
    }

    public void speedCalculation() {
        if (lines % 10 == 0) {
            level++;

            if (speed > 100) speed -= 100;
            else {
                if (speed > 20) speed -= 5;
            }
        }
    }

    public static void hold() {
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
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case 32: // SPACE
                hardDrop();
                break;

            case 37: // Left Arrow Key
                move(-1);
                break;

            case 38: // Up Arrow Key
                if (currentTetrimino != null && currentTetrimino.direction != -1) currentTetrimino.rotate(1);
                break;

            case 39: // Right Arrow Key
                move(1);
                break;

            case 40: // Down Arrow Key
                moveTetriminos();
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
    public void keyReleased(KeyEvent ke) {}

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