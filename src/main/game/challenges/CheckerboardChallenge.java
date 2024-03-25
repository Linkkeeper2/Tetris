package src.main.game.challenges;

import java.awt.Color;

import src.main.MyGame;
import src.main.game.objects.TetriminoNode;

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
}
