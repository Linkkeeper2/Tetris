import java.awt.Color;

public class Pieces {
    public class LongPiece extends Tetrimino {
        public LongPiece() {
            super(new Color(0, 255, 255), 0, 2); // Centered on left
            MyGame.add(this, 0, 2);

            Tetrimino t = this;
            for (int i = 0; i < 3; i++) {
                MyGame.addTetriminoNode(t, 1, t.row, t.col);
                t = t.getNext();
            }
        }
    }

    public class TPiece extends Tetrimino {
        public TPiece() {
            super(new Color(255, 0, 255), 2, 3); // Centered on Bottom Left
            MyGame.add(this, 2, 3);

            Tetrimino t = this;
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // Down + Right
            MyGame.addTetriminoNode(t, 4, t.row, t.col);
        }
    }

    public class ZPiece extends Tetrimino {
        public ZPiece() {
            super(new Color(255, 0, 0), 2, 3); // Centered on Bottom Left
            MyGame.add(this, 2, 3);

            Tetrimino t = this;
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // Right
            MyGame.addTetriminoNode(t, 1, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
        }
    }

    public class SPiece extends Tetrimino {
        public SPiece() {
            super(new Color(0, 255, 0), 2, 4); // Centered on Bottom Right
            MyGame.add(this, 2, 4);

            Tetrimino t = this;
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // Left
            MyGame.addTetriminoNode(t, 0, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
        }
    }

    public class BoxPiece extends Tetrimino {
        public BoxPiece() {
            super(new Color(255, 255, 0), 1, 3); // Centered on Bottom Left
            MyGame.add(this, 1, 3);

            Tetrimino t = this;
            // Right
            MyGame.addTetriminoNode(t, 1, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // Left
            MyGame.addTetriminoNode(t, 0, t.row, t.col);
        }
    }

    public class LPiece extends Tetrimino {
        public LPiece() {
            super(new Color(235, 125, 52), 2, 4); // Centered on Bottom Right
            MyGame.add(this, 2, 4);

            Tetrimino t = this;
            // Left
            MyGame.addTetriminoNode(t, 0, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
        }
    }

    public class BackLPiece extends Tetrimino {
        public BackLPiece() {
            super(new Color(0, 0, 255), 2, 3); // Centered on Bottom Left
            MyGame.add(this, 2, 3);

            Tetrimino t = this;
            // Right
            MyGame.addTetriminoNode(t, 1, t.row, t.col);
            t = t.getNext();
            // Up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
            t = t.getNext();
            // up
            MyGame.addTetriminoNode(t, 2, t.row, t.col);
        }
    }
}
