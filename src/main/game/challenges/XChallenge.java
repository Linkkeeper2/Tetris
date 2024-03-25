package src.main.game.challenges;

import java.awt.Color;

import src.main.MyGame;
import src.main.game.objects.TetriminoNode;

public class XChallenge extends Challenge {
    public void start() {
        TetriminoNode[][] board = MyGame.board.board;
        int node1 = 0;
        int node2 = board[0].length - 1;

        for (int r = board.length / 2; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (c == node1 || c == node2) {
                    board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                    board[r][c].updateID();
                }
            }

            node1++;
            node2--;
        }
    }

    public void end() {
        MyGame.status.addMessage("You Win!", 3000);
        MyGame.account.addExp((int) (Math.random() * 200) + 100);
        MyGame.exitToMenu();
    }
}
