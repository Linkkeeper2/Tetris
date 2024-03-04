import java.awt.Color;

public class HardCheckerboardChallenge extends Challenge {
   public void start() {
    TetriminoNode[][] board = MyGame.board.board;

        for (int r = 6; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (c % 2 == r % 2) {
                    board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                    board[r][c].updateID();
                }
            }
        }
   }

   public void end() {
        MyGame.status.addMessage("You Win!", 3000);
        MyGame.account.addExp((int)(Math.random() * 300) + 700);
        MyGame.exitToMenu();
   }
}
