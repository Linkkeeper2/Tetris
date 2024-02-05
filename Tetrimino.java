public class Tetrimino {
    protected TetriminoNode[] nodes;
    protected int id; // The current Tetrimino ID (First Tetrimino in game = 0, next one is 1, etc.)

    public TetriminoNode[] getNodes() {
        return nodes;
    }

    public void setID(int val) {
        this.id = val;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].id = this.id;
        }
    }
}
