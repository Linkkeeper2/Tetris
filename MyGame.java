import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class MyGame extends Game  {
    public static final String TITLE = "Tetris";
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 800;

    // declare variables here
    public static Tetrimino[][] board;
    public static int offset;
    private Timer timer;
    private Pieces pieces;
    private Tetrimino currentTetrimino;

    public MyGame() {
        // initialize variables here
        board = new Tetrimino[16][8];
        offset = 100;
        pieces = new Pieces();
        createTetrimino();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                moveTetriminos();
            }
        }, (long)1000, (long)1000);
    }
    
    public void update() {
        // updating logic
    }
    
    public void draw(Graphics pen) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    pen.setColor(Color.gray);
                    pen.fillRect(c * 25 + offset, r * 25 + offset, 25, 25);
                } else {
                    Tetrimino curr = board[r][c];
                            
                    while (curr != null) {
                        pen.setColor(curr.getColor());
                        pen.fillRect(curr.col * 25 + offset, curr.row * 25 + offset, 25, 25);
                        curr = curr.getNext();
                    }
                }
            }
        }
    }

    public void createTetrimino() {
        currentTetrimino = pieces.new LongPiece();
    }

    public static void addTetriminoNode(Tetrimino t, int direction, int row, int col) {
        // 0 -> Left, 1 -> Right, 2 -> Up, 3 -> Down, 4 -> Down + Right
        switch (direction) {
            case 0:
                t.setNext(new Tetrimino(t.getColor(), row, col - 1));
                board[row][col - 1] = t.getNext();
                break;

            case 1:
                t.setNext(new Tetrimino(t.getColor(), row, col + 1));
                board[row][col + 1] = t.getNext();
                break;

            case 2:
                t.setNext(new Tetrimino(t.getColor(), row - 1, col));
                board[row - 1][col] = t.getNext();
                break;

            case 3:
                t.setNext(new Tetrimino(t.getColor(), row + 1, col));
                board[row + 1][col] = t.getNext();
                break;

            case 4:
                t.setNext(new Tetrimino(t.getColor(), row + 1, col + 1));
                board[row + 1][col + 1] = t.getNext();
                break;
        }
    }

    public void moveTetriminos() {
        ArrayList<Tetrimino> toMove = new ArrayList<Tetrimino>();

        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++) {
                if (i < board.length - 1 && board[i][k] != null && !toMove.contains(board[i][k])) {
                    Tetrimino curr = board[i][k];

                    while (curr != null) {
                        if (!toMove.contains(curr)) {
                            curr.row++;
                            toMove.add(curr);
                        }
                        curr = curr.getNext();
                    }
                }
            }
        }
        
        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++) {
                if (board[i][k] != null) {
                    for (int t = 0; t < toMove.size(); t++) {
                        if (toMove.get(t).canMove) {
                            board[toMove.get(t).row - 1][toMove.get(t).col] = null;
                            board[toMove.get(t).row][toMove.get(t).col] = toMove.get(t);
                        }
                    }
                }
            }
        }
    }
    
    public static void add(Tetrimino t, int row, int col) {
        board[row][col] = t;
    }

    public void moveLeft() {
        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++) {
                if (board[i][k] == currentTetrimino) {
                    Tetrimino t = currentTetrimino;

                    while (t != null) {
                        t.col--;
                        board[i][k - 1] = null;
                        board[t.row][t.col] = t;
                        t = t.getNext();
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == ke.VK_LEFT) {
            moveLeft();
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