public class Tetrimino {
    protected TetriminoNode[] nodes;
    protected int id; // The current Tetrimino ID (First Tetrimino in game = 0, next one is 1, etc.)
    protected int direction;

    public TetriminoNode[] getNodes() {
        return nodes;
    }

    public void setID(int val) {
        this.id = val;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].id = this.id;
        }
    }

    public void rotateRight() {}

    public void rotateLeft() {}

    protected boolean canRotate(int nextRow, int nextCol) {
        if (!(nextRow >= 0 && nextRow < MyGame.board.length && nextCol >= 0 && nextCol < MyGame.board[0].length)) return false;

        return true;
    }
}
