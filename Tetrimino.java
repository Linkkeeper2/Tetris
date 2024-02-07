import java.awt.Color;

public class Tetrimino {
    protected TetriminoNode[] nodes;
    protected TetriminoNode[][] rotations;
    protected int id; // The current Tetrimino ID (First Tetrimino in game = 0, next one is 1, etc.)
    protected int direction = 1; // The current rotation direction of the Tetrimino
    protected Color color;
    protected int colorIndex; // Label for which color in the Palette the Tetrimino uses

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

        if (rotations == null) return;

        for (int i = 0; i < rotations.length; i++) {
            for (int k = 0; k < rotations[i].length; k++) {
                rotations[i][k].id = this.id;
            }
        }
    }

    public void rotate(int factor) {
        for (int i = 0; i < rotations[this.direction].length; i++) {
            TetriminoNode node = rotations[this.direction][i];

            if (!canRotate(node.row, node.col)) return;
        }

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            MyGame.board[node.row][node.col] = null;
        }

        nodes = rotations[this.direction];

        // Yes there is a more efficient way to do this, but for some reason it works better this way (Looping rotation)
        if (factor == 1) {
            if (this.direction < 3) this.direction++;
            else this.direction = 0;
        } else if (factor == -1) {
            if (this.direction > 0) this.direction--;
            else this.direction = 3;
        }

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            MyGame.board[node.row][node.col] = node;
        }
        
        MyGame.updateArray();
    }

    protected void createRotations() {}

    protected boolean canRotate(int nextRow, int nextCol) {
        if (!(nextRow >= 0 && nextRow < MyGame.board.length && nextCol >= 0 && nextCol < MyGame.board[0].length)) return false;

        if (MyGame.board[nextRow][nextCol] != null && MyGame.board[nextRow][nextCol].id != this.id) return false;

        return true;
    }

    protected String getType() {
        return "Default";
    }

    
}
