public class Tetrimino {
    protected TetriminoNode[] nodes;
    protected TetriminoNode[][] rotations;
    protected int id; // The current Tetrimino ID (First Tetrimino in game = 0, next one is 1, etc.)
    protected int direction = 1; // The current rotation direction of the Tetrimino

    public TetriminoNode[] getNodes() {
        return nodes;
    }

    public TetriminoNode[][] getRotations() {
        return rotations;
    }

    public void setID(int val) {
        this.id = val;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].id = this.id;
        }
    }

    public void rotate(int factor) {}

    public void updateRotations() {}

    protected boolean canRotate(int nextRow, int nextCol) {
        if (!(nextRow >= 0 && nextRow < MyGame.board.length && nextCol >= 0 && nextCol < MyGame.board[0].length)) return false;

        return true;
    }
}
