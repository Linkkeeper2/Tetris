import java.awt.Color;

public class Tetriminos {
    public class LongPiece extends Tetrimino {
        public LongPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            for (int i = 0; i < 4; i++) {
                nodes[i] = new TetriminoNode(new Color(0, 255, 255), 0, i + 2);
                MyGame.add(nodes[i], 0, i + 2);
            }

            this.nodes = nodes;
        }
    }

    public class TPiece extends Tetrimino  {
        public TPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            for (int i = 0; i < 3; i++) {
                nodes[i] = new TetriminoNode(new Color(255, 0, 255), i, 3);
                MyGame.add(nodes[i], i, 3);
            }

            nodes[3] = new TetriminoNode(new Color(255, 0, 255), 1, 4);
            MyGame.add(nodes[3], 1, 4);

            this.nodes = nodes;
        }
    }

    public class ZPiece extends Tetrimino  {
        public ZPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (255, 0, 0), 2, 3);
            nodes[1] = new TetriminoNode(new Color (255, 0, 0), 1, 3);
            nodes[2] = new TetriminoNode(new Color (255, 0, 0), 1, 4);
            nodes[3] = new TetriminoNode(new Color (255, 0, 0), 0, 4);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
        }
    }

    public class SPiece extends Tetrimino  {
        public SPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (0, 255, 0), 2, 4);
            nodes[1] = new TetriminoNode(new Color (0, 255, 0), 1, 4);
            nodes[2] = new TetriminoNode(new Color (0, 255, 0), 1, 3);
            nodes[3] = new TetriminoNode(new Color (0, 255, 0), 0, 3);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
        }
    }

    public class BoxPiece extends Tetrimino  {
        public BoxPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (255, 255, 0), 1, 4);
            nodes[1] = new TetriminoNode(new Color (255, 255, 0), 0, 4);
            nodes[2] = new TetriminoNode(new Color (255, 255, 0), 1, 3);
            nodes[3] = new TetriminoNode(new Color (255, 255, 0), 0, 3);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
        }
    }

    public class LPiece extends Tetrimino  {
        public LPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (255, 100, 0), 2, 4);
            nodes[1] = new TetriminoNode(new Color (255, 100, 0), 2, 3);
            nodes[2] = new TetriminoNode(new Color (255, 100, 0), 1, 3);
            nodes[3] = new TetriminoNode(new Color (255, 100, 0), 0, 3);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
        }
    }

    public class BackLPiece extends Tetrimino  {
        public BackLPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (0, 0, 255), 2, 3);
            nodes[1] = new TetriminoNode(new Color (0, 0, 255), 2, 4);
            nodes[2] = new TetriminoNode(new Color (0, 0, 255), 1, 4);
            nodes[3] = new TetriminoNode(new Color (0, 0, 255), 0, 4);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
        }
    }
}
