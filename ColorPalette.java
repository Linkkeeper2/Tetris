import java.io.IOException;

public class ColorPalette {
    public int currentPalette;
    public SpriteSheetLoader sheet;
    public int rows;

    public ColorPalette() {
        currentPalette = 0;
        this.rows = 10;
        try {
            sheet = new SpriteSheetLoader(24, 24, 10, 3);
        } catch (IOException e) {
        }
    }

    public ColorPalette(int rows) {
        currentPalette = 0;
        this.rows = rows;
        try {
            sheet = new SpriteSheetLoader(24, 24, rows, 3);
        } catch (IOException e) {
        }
    }

    public ColorPalette(int rows, String path) {
        currentPalette = 0;
        this.rows = rows;
        try {
            sheet = new SpriteSheetLoader(24, 24, rows, 3, path);
        } catch (IOException e) {
        }
    }
}
