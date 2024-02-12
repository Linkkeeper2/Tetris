import java.awt.Color;

public class ColorPalette {
    private Color[][] colors;
    public int currentPalette;

    public ColorPalette() {
        colors = new Color[25][7];
        currentPalette = 0;

        /*
         * 0: I Piece
         * 1: T Piece
         * 2: Z Piece
         * 3: S Piece
         * 4: O Piece
         * 5: L Piece
         * 6: J Piece
         */

         // Default Colors
        colors[0] = new Color[] {new Color(0, 255, 255), new Color(255, 0, 255), 
            new Color(255, 0, 0), new Color(0, 255, 0), new Color(255, 255, 0), 
            new Color(255, 100, 0), new Color(0, 0, 255)};
        
        // Dark Colors
        colors[1] = new Color[] {new Color(0, 90, 158), new Color(158, 0, 95), 
            new Color(222, 98, 44), new Color(0, 255, 100), new Color(200, 155, 0), 
            new Color(200, 50, 0), new Color(55, 0, 200)};
        
        // Deep Colors
        colors[2] = new Color[] {new Color(0, 0, 158), new Color(111, 0, 158), 
                new Color(140, 38, 0), new Color(0, 115, 10), new Color(130, 115, 10), 
                new Color(160, 15, 0), new Color(30, 0, 100)};
        
        // Bright Colors
        colors[3] = new Color[] {new Color(135, 255, 255), new Color(167, 135, 255), 
            new Color(255, 135, 135), new Color(135, 255, 155), new Color(255, 255, 135), 
            new Color(255, 187, 135), new Color(135, 140, 255)};
        
        // Tarnished Colors
        colors[4] = new Color[] {new Color(56, 90, 105), new Color(77, 56, 105), 
            new Color(105, 56, 56), new Color(56, 105, 60), new Color(102, 105, 56), 
            new Color(105, 78, 56), new Color(56, 60, 105)};
        
        // Fruits
        colors[5] = new Color[] {new Color(135, 255, 255), new Color(223, 176, 255), 
            new Color(255, 56, 189), new Color(56, 255, 109), new Color(255, 255, 175), 
            new Color(255, 177, 74), new Color(150, 75, 255)};
    
        for (int i = 5; i < colors.length; i++) {
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
