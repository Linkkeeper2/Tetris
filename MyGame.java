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
    private int tNum = 0; // Number of Tetriminos used (Keeps track of ID for each Tetrimino)

    public MyGame() {
        // initialize variables here
        board = new TetriminoNode[16][8];
        offset = 100;
        pieces = new Tetriminos();
        updateArray();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                moveTetriminos();
            }
        }, (long)1000, (long)1000);
    }
    
    public void update() {
        // updating logic
        if (currentTetrimino == null) currentTetrimino = getTetrimino();
    }
    
    public void draw(Graphics pen) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    pen.setColor(Color.gray);
                    pen.fillRect(c * 25 + offset, r * 25 + offset, 25, 25);
                } else {
                    TetriminoNode curr = board[r][c];
                    
                    pen.setColor(curr.getColor());
                    pen.fillRect(curr.col * 25 + offset, curr.row * 25 + offset, 25, 25);
                }
            }
        }
    }

    public void moveTetriminos() {
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

        for (int i = 0; i < nodes.length; i++) nodes[i].row++;

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            board[node.row][node.col] = node;
        }

        updateArray();
    }
    
    public static void add(TetriminoNode t, int row, int col) {
        board[row][col] = t;
    }

    public void updateArray() {
        // Updates the copy of the board used to move Tetriminos
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
        }

        for (int i = 0; i < nodes.length; i++) nodes[i].col += 1 * direction;

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            board[node.row][node.col] = node;
        }

        updateArray();
    }

    public Tetrimino getTetrimino() {
        int num = (int)(Math.random() * 7);
        Tetrimino t = pieces.new LPiece();

        switch (num) {
            case 0:
                t = pieces.new LongPiece();
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
                t = pieces.new BoxPiece();
                break;

            case 5:
                t = pieces.new LPiece();
                break;

            case 6:
                t = pieces.new BackLPiece();
                break;
        }

        t.setID(tNum);
        tNum++;

        return t;
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case 32: // SPACE
                for (int i = 0; i < board.length; i++) {
                    moveTetriminos();
                }
                break;

            case 37: // Left Arrow Key
                move(-1);
                break;

            case 39: // Right Arrow Key
                move(1);
                break;

            case 40: // Down Arrow Key
                moveTetriminos();
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