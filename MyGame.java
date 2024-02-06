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
    private Timer timer;
    private Tetriminos pieces;
    private Tetrimino currentTetrimino;
    private Tetrimino nextTetrimino;
    private int tNum = 0; // Number of Tetriminos used (Keeps track of ID for each Tetrimino)
    private boolean hardDropping = false; // Determines whether or not the regular drop interval should occur (prevents Tetrimino clipping while Hard Dropping)
    private int speed = 1000; // The millisecond delay between automatic Tetrimino movement
    private int lines = 0; // The number of lines cleared
    private int score = 0;
    private boolean alive = true;

    public MyGame() {
        // initialize variables here
        board = new TetriminoNode[20][10];
        offset = 100;
        pieces = new Tetriminos();
        updateArray();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                automaticMove();
            }
        }, (long)speed);
        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();
    }
    
    public void update() {
        // updating logic
        if (currentTetrimino == null && alive) {
            clearRow();
            swapTetriminos();
        }

        updateArray();
    }
    
    public void draw(Graphics pen) {
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
        pen.drawString("Next", 0, 60);

        if (alive) {
            TetriminoNode[] nodes = nextTetrimino.getNodes();

            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                pen.setColor(node.getColor());
                pen.fillRect(node.row * 25 + 20, node.col * 25 + 15, 25, 25);
            }
        }
    }

    public void moveTetriminos() {
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
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] != null) {
                    TetriminoNode node = board[r][c];
                    if (node.row != r || node.col != c) board[r][c] = null;
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

    public Tetrimino getTetrimino() {
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

        return t;
    }

    public Tetrimino getNextTetrimino() {
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

                if (lines % 10 == 0) if (speed > 100) speed -= 100;

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
                break;

            case 2:
                score += 400;
                break;

            case 3:
                score += 800;
                break;

            case 4:
                score += 1600;
                break;
        }
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
                    return;
                } else if (board[nodes[i].row + 1][nodes[i].col] != null && board[nodes[i].row + 1][nodes[i].col].id != nodes[i].id) {
                    currentTetrimino = null;
                    hardDropping = false;
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
    }

    public void automaticMove() {
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

            case 90: // Z Key
                if (currentTetrimino != null && currentTetrimino.direction != -1) currentTetrimino.rotate(-1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {}

    @Override
    public void mouseClicked(MouseEvent ke) {}

    @Override
    public void mousePressed(MouseEvent me) {}
    
    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
        
    //Launches the Game
    public static void main(String[] args) { new MyGame().start(TITLE, SCREEN_WIDTH,SCREEN_HEIGHT); }
}