package src.main.game.challenges;

import java.awt.Color;

import src.main.MyGame;
import src.main.game.objects.TetriminoNode;

public class StairCaseChallenge extends Challenge {
    public void start() {
        TetriminoNode[][] board = MyGame.board.board;

        int count = 1;
        int direction = 1;

        for (int r = 3; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (c < count) {
                    board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                    board[r][c].updateID();
                } else
                    break;
            }

            count += direction;
            if (count == board[r].length - 1 || count == 1)
                direction = -direction;
        }
    }

    public void end() {
        MyGame.status.addMessage("You Win!", 3000);
        MyGame.account.addExp((int) (Math.random() * 100) + 100);
        MyGame.exitToMenu();
    }
}
