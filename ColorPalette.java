import java.io.IOException;

public class ColorPalette {
    public int currentPalette;
    public SpriteSheetLoader sheet;

    public ColorPalette() {
        currentPalette = 0;
        try {
            sheet = new SpriteSheetLoader(24, 24, 10, 3);
        } catch (IOException e) {}
    }
}
