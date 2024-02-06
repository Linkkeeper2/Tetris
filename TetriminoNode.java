import java.awt.Color;

public class TetriminoNode {
    private Color color;
    public int id;
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

    public Color getDarkColor() {
        int r;
        int g;
        int b;
        int deduction = 40; // Amount each rgb value is reduced by

        if (color.getRed() - deduction >= 0) r = color.getRed() - deduction;
        else r = 0;

        if (color.getGreen() - deduction >= 0) g = color.getGreen() - deduction;
        else g = 0;

        if (color.getBlue() - deduction >= 0) b = color.getBlue() - deduction;
        else b = 0;

        return new Color(r, g, b);
    }

    public String toString() {
        return "Tetrimino at: (" + row + ", " + col + ")";
    }
}
