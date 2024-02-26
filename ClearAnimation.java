import java.util.ArrayList;

public class ClearAnimation extends Thread {
    public ArrayList<Integer> rowsToClear = new ArrayList<>();
    private int col1 = 4;
    private int col2 = 5;

    @Override
    public void run() {
        while (true) {
            clearAnimation();
            try {
                Thread.sleep(100);
            } catch (Exception e) {}
        }
    }

    public void clearAnimation() {
        // Clears all the necessary rows as an animation
        if (rowsToClear.size() == 0 || MyGame.board == null) return;

        TetriminoNode[][] board = MyGame.board;

        if (col1 >= 0 && col2 <= board[0].length) {
            for (int i = 0; i < rowsToClear.size(); i++) {
                board[rowsToClear.get(i)][col1] = null;
                board[rowsToClear.get(i)][col2] = null;
            }
        }
        
        col1--;
        col2++;

        if (col1 < -1 || col2 > 10) {
            for (int r = 0; r < rowsToClear.size(); r++) {
                for (int i = rowsToClear.get(r); i > 0; i--) {
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

            col1 = 4;
            col2 = 5;
            rowsToClear.clear();
        }
    }
}
