import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheetLoader {
    BufferedImage spriteSheet = ImageIO.read(new File(MyGame.account.skin));

    int width;
    int height;
    int rows;
    int columns;
    BufferedImage[] sprites;

    public SpriteSheetLoader(int width, int height, int rows, int columns) throws IOException {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        sprites = new BufferedImage[rows * columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sprites[j + (i * columns)] = spriteSheet.getSubimage(j * width, i * height, width, height);
            }
        }
    }

    public SpriteSheetLoader(int width, int height, int rows, int columns, String path) throws IOException {
        spriteSheet = ImageIO.read(new File(path));
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        sprites = new BufferedImage[rows * columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sprites[j + (i * columns)] = spriteSheet.getSubimage(j * width, i * height, width, height);
            }
        }
    }
}