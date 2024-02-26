import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class TetriminoNode {
    public int id;
    public int row;
    public int col;
    public int colorIndex;
    public Tetrimino parent;
    public BufferedImage sprite;

    public TetriminoNode(Color c, int row, int col, int colorIndex) {
        this.colorIndex = colorIndex;
        this.row = row;
        this.col = col;

        if (this.colorIndex == -1) this.sprite = MyGame.palette.sheet.sprites[0];
    }

    public String toString() {
        return "Tetrimino at: (" + row + ", " + col + ")";
    }

    public void updateColor() {
        if (this.colorIndex != -1) this.sprite = MyGame.palette.sheet.sprites[MyGame.palette.currentPalette * 3 + colorIndex];
    }

    public void draw(Graphics pen) {
        pen.drawImage(this.sprite, col * MyGame.tileSize + MyGame.offset, row * MyGame.tileSize + MyGame.offset, null);
    }

    public void updateID() {
        // For the stray nodes
        this.id = -MyGame.tNum;
    }
}