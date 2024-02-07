import java.awt.Color;

public class ColorPalette {
    private Color[][] colors;
    public int currentPalette;

    public ColorPalette() {
        colors = new Color[2][7];
        currentPalette = 0;
        colors[0] = new Color[] {new Color(0, 255, 255), new Color(255, 0, 255), 
            new Color(255, 0, 0), new Color(0, 255, 0), new Color(255, 255, 0), 
            new Color(255, 100, 0), new Color(0, 0, 255)};

        colors[1] = new Color[] {new Color(0, 90, 158), new Color(158, 0, 95), 
            new Color(222, 98, 44), new Color(0, 255, 100), new Color(200, 155, 0), 
            new Color(200, 50, 0), new Color(55, 0, 200)};
    }

    public Color[][] getColors() {
        return colors;
    }
}
