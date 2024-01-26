import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;

public class MyGame extends Game  {
    public static final String TITLE = "Tetris";
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 800;

    // declare variables here
    private int[][] board;
    private int offset;

    public MyGame() {
        // initialize variables here
        board = new int[8][8];
        offset = 100;
    }
    
    public void update() {
        // updating logic
    }
    
    public void draw(Graphics pen) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                switch (board[r][c]) {
                    case 0:
                        pen.setColor(Color.gray);
                        pen.fillRect(c * 25 + offset, r * 25 + offset, 25, 25);
                        break;
                }
            }
        }
    }
        
    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {}

    @Override
    public void keyReleased(KeyEvent ke) {}

    @Override
    public void mouseClicked(MouseEvent ke) {
        
    }

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