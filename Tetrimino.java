import java.awt.Color;

public class Tetrimino {
    private Tetrimino next;
    private Color color;
    public int row;
    public int col;
    public boolean canMove;

    public Tetrimino(Color c, int row, int col) {
        color = c;
        this.row = row;
        this.col = col;
        canMove = true;
    }

    public Color getColor() {
        return color;
    }

    public Tetrimino getNext() {
        return next;
    }

    public void setNext(Tetrimino t) {
        next = t;
    }

    public String toString() {
        return "Tetrimino at: (" + row + ", " + col + ")";
    }

    public void outputNodes() {
        Tetrimino t = this;

        while (t != null) {
            System.out.println(t);
            t = t.getNext();
        }
    }
}
