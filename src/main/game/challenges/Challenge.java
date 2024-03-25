package src.main.game.challenges;

import src.main.MyGame;
import src.main.game.objects.TetriminoNode;

public abstract class Challenge {
    public abstract void start();

    public void check() {
        boolean end = true;
        TetriminoNode[][] board = MyGame.board.board;

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] != null && board[r][c].id < 0)
                    end = false;
            }
        }

        if (end)
            end();
    }

    public void end() {
        MyGame.status.addMessage("You Win!", 3000);
        MyGame.account.addExp((int) (Math.random() * 200) + 400);
        MyGame.exitToMenu();
    }
}
