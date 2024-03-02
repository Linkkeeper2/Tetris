import java.awt.Color;

public class CheckerboardChallenge extends Challenge {
    public void start() {
        TetriminoNode[][] board = MyGame.board.board;

        for (int r = board.length / 2 + 1; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (c % 2 == r % 2) {
                    board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                    board[r][c].updateID();
                }
            }
        }
    }

    public void check() {
        boolean end = true;
        TetriminoNode[][] board = MyGame.board.board;

        for (int r = board.length / 2 + 1; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] != null && board[r][c].id < 0) end = false;
            }
        }

        if (end) {
            MyGame.status.addMessage("You Win!", 3000);
            MyGame.account.addExp((int)(Math.random() * 200) + 400);
            MyGame.exitToMenu();
        }
    }
}
