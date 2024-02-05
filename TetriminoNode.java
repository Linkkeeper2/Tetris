import java.awt.Color;

public class TetriminoNode {
    private Color color;
    public int row;
    public int col;

    public TetriminoNode(Color c, int row, int col) {
        color = c;
        this.row = row;
        this.col = col;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return "Tetrimino at: (" + row + ", " + col + ")";
    }
}
