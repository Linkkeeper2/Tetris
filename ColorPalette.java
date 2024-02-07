import java.awt.Color;

public class ColorPalette {
    private Color[][] colors;
    public int currentPalette;

    public ColorPalette() {
        colors = new Color[10][7];
        currentPalette = 0;
        colors[0] = new Color[] {new Color(0, 255, 255), new Color(255, 0, 255), 
            new Color(255, 0, 0), new Color(0, 255, 0), new Color(255, 255, 0), 
            new Color(255, 100, 0), new Color(0, 0, 255)};

        colors[1] = new Color[] {new Color(0, 90, 158), new Color(158, 0, 95), 
            new Color(222, 98, 44), new Color(0, 255, 100), new Color(200, 155, 0), 
            new Color(200, 50, 0), new Color(55, 0, 200)};

        colors[2] = new Color[] {new Color(0, 0, 158), new Color(111, 0, 158), 
                new Color(140, 38, 0), new Color(0, 115, 10), new Color(130, 115, 10), 
                new Color(160, 15, 0), new Color(30, 0, 100)};
    
        for (int i = 3; i < colors.length; i++) {
            colors[i] = new Color[] {new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)), 
                new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)), 
                new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)), 
                new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)), 
                new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)), 
                new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256)), 
                new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256))};
        }
    }

    public Color[][] getColors() {
        return colors;
    }
}
