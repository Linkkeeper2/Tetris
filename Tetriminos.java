import java.awt.Color;

public class Tetriminos {
    public class IPiece extends Tetrimino {
        public IPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            for (int i = 0; i < 4; i++) {
                nodes[i] = new TetriminoNode(new Color(0, 255, 255), 0, i + 2);
                MyGame.add(nodes[i], 0, i + 2);
            }

            this.nodes = nodes;
            createRotations();
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length; i++) {
                rotations[0][i] = new TetriminoNode(new Color(0, 255, 255), 0, i + 2);
            }

            for (int i = 0; i < rotations[1].length; i++) {
                rotations[1][i] = new TetriminoNode(new Color(0, 255, 255), i, 4);
            }

            for (int i = 0; i < rotations[2].length; i++) {
                rotations[2][i] = new TetriminoNode(new Color(0, 255, 255), 2, i + 2);
            }

            for (int i = 0; i < rotations[3].length; i++) {
                rotations[3][i] = new TetriminoNode(new Color(0, 255, 255), i, 3);
            }
        }
    }

    public class TPiece extends Tetrimino  {
        public TPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            for (int i = 0; i < 3; i++) {
                nodes[i] = new TetriminoNode(new Color(255, 0, 255), 1, i + 3);
                MyGame.add(nodes[i], 1, i + 3);
            }

            nodes[3] = new TetriminoNode(new Color(255, 0, 255), 0, 4);
            MyGame.add(nodes[3], 0, 4);

            this.nodes = nodes;
            createRotations();
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length - 1; i++) {
                rotations[0][i] = new TetriminoNode(new Color(255, 0, 255), 1, i + 2);
            }
            rotations[0][3] = new TetriminoNode(new Color(255, 0, 255), 0, 3);

            for (int i = 0; i < rotations[1].length - 1; i++) {
                rotations[1][i] = new TetriminoNode(new Color(255, 0, 255), i, 3);
            }
            rotations[1][3] = new TetriminoNode(new Color(255, 0, 255), 1, 4);

            for (int i = 0; i < rotations[2].length - 1; i++) {
                rotations[2][i] = new TetriminoNode(new Color(255, 0, 255), 1, i + 2);
            }
            rotations[2][3] = new TetriminoNode(new Color(255, 0, 255), 2, 3);

            for (int i = 0; i < rotations[3].length - 1; i++) {
                rotations[3][i] = new TetriminoNode(new Color(255, 0, 255), i, 3);
            }
            rotations[3][3] = new TetriminoNode(new Color(255, 0, 255), 1, 2);
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
            createRotations();
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            rotations[0][0] = new TetriminoNode(new Color(255, 0, 0), 0, 2);
            rotations[0][1] = new TetriminoNode(new Color(255, 0, 0), 0, 3);
            rotations[0][2] = new TetriminoNode(new Color(255, 0, 0), 1, 3);
            rotations[0][3] = new TetriminoNode(new Color(255, 0, 0), 1, 4);

            rotations[1][0] = new TetriminoNode(new Color(255, 0, 0), 0, 4);
            rotations[1][1] = new TetriminoNode(new Color(255, 0, 0), 1, 4);
            rotations[1][2] = new TetriminoNode(new Color(255, 0, 0), 1, 3);
            rotations[1][3] = new TetriminoNode(new Color(255, 0, 0), 2, 3);

            rotations[2][0] = new TetriminoNode(new Color(255, 0, 0), 1, 2);
            rotations[2][1] = new TetriminoNode(new Color(255, 0, 0), 1, 3);
            rotations[2][2] = new TetriminoNode(new Color(255, 0, 0), 2, 3);
            rotations[2][3] = new TetriminoNode(new Color(255, 0, 0), 2, 4);

            rotations[3][0] = new TetriminoNode(new Color(255, 0, 0), 0, 3);
            rotations[3][1] = new TetriminoNode(new Color(255, 0, 0), 1, 3);
            rotations[3][2] = new TetriminoNode(new Color(255, 0, 0), 1, 2);
            rotations[3][3] = new TetriminoNode(new Color(255, 0, 0), 2, 2);
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
            createRotations();
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            rotations[0][0] = new TetriminoNode(new Color(0, 255, 0), 1, 2);
            rotations[0][1] = new TetriminoNode(new Color(0, 255, 0), 1, 3);
            rotations[0][2] = new TetriminoNode(new Color(0, 255, 0), 0, 3);
            rotations[0][3] = new TetriminoNode(new Color(0, 255, 0), 0, 4);

            rotations[1][0] = new TetriminoNode(new Color(0, 255, 0), 0, 3);
            rotations[1][1] = new TetriminoNode(new Color(0, 255, 0), 1, 3);
            rotations[1][2] = new TetriminoNode(new Color(0, 255, 0), 1, 4);
            rotations[1][3] = new TetriminoNode(new Color(0, 255, 0), 2, 4);

            rotations[2][0] = new TetriminoNode(new Color(0, 255, 0), 2, 2);
            rotations[2][1] = new TetriminoNode(new Color(0, 255, 0), 2, 3);
            rotations[2][2] = new TetriminoNode(new Color(0, 255, 0), 1, 3);
            rotations[2][3] = new TetriminoNode(new Color(0, 255, 0), 1, 4);

            rotations[3][0] = new TetriminoNode(new Color(0, 255, 0), 0, 2);
            rotations[3][1] = new TetriminoNode(new Color(0, 255, 0), 1, 2);
            rotations[3][2] = new TetriminoNode(new Color(0, 255, 0), 1, 3);
            rotations[3][3] = new TetriminoNode(new Color(0, 255, 0), 2, 3);
        }
    }

    public class OPiece extends Tetrimino  {
        public OPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (255, 255, 0), 1, 4);
            nodes[1] = new TetriminoNode(new Color (255, 255, 0), 0, 4);
            nodes[2] = new TetriminoNode(new Color (255, 255, 0), 1, 3);
            nodes[3] = new TetriminoNode(new Color (255, 255, 0), 0, 3);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            this.direction = -1;
        }
    }

    public class LPiece extends Tetrimino  {
        public LPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (255, 100, 0), 0, 4);
            nodes[1] = new TetriminoNode(new Color (255, 100, 0), 1, 4);
            nodes[2] = new TetriminoNode(new Color (255, 100, 0), 1, 3);
            nodes[3] = new TetriminoNode(new Color (255, 100, 0), 1, 2);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            createRotations();
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length - 1; i++) {
                rotations[0][i] = new TetriminoNode(new Color(255, 100, 0), 1, i + 2);
            }
            rotations[0][3] = new TetriminoNode(new Color(255, 100, 0), 0, 4);

            for (int i = 0; i < rotations[1].length - 1; i++) {
                rotations[1][i] = new TetriminoNode(new Color(255, 100, 0), i, 3);
            }
            rotations[1][3] = new TetriminoNode(new Color(255, 100, 0), 2, 4);

            for (int i = 0; i < rotations[2].length - 1; i++) {
                rotations[2][i] = new TetriminoNode(new Color(255, 100, 0), 1, i + 2);
            }
            rotations[2][3] = new TetriminoNode(new Color(255, 100, 0), 2, 2);

            for (int i = 0; i < rotations[3].length - 1; i++) {
                rotations[3][i] = new TetriminoNode(new Color(255, 100, 0), i, 3);
            }
            rotations[3][3] = new TetriminoNode(new Color(255, 100, 0), 0, 2);
        }
    }

    public class JPiece extends Tetrimino  {
        public JPiece() {
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(new Color (0, 0, 255), 0, 2);
            nodes[1] = new TetriminoNode(new Color (0, 0, 255), 1, 2);
            nodes[2] = new TetriminoNode(new Color (0, 0, 255), 1, 3);
            nodes[3] = new TetriminoNode(new Color (0, 0, 255), 1, 4);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            createRotations();
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length - 1; i++) {
                rotations[0][i] = new TetriminoNode(new Color(0, 0, 255), 1, i + 2);
            }
            rotations[0][3] = new TetriminoNode(new Color(0, 0, 255), 0, 2);

            for (int i = 0; i < rotations[1].length - 1; i++) {
                rotations[1][i] = new TetriminoNode(new Color(0, 0, 255), i, 3);
            }
            rotations[1][3] = new TetriminoNode(new Color(0, 0, 255), 0, 4);

            for (int i = 0; i < rotations[2].length - 1; i++) {
                rotations[2][i] = new TetriminoNode(new Color(0, 0, 255), 1, i + 2);
            }
            rotations[2][3] = new TetriminoNode(new Color(0, 0, 255), 2, 4);

            for (int i = 0; i < rotations[3].length - 1; i++) {
                rotations[3][i] = new TetriminoNode(new Color(0, 0, 255), i, 3);
            }
            rotations[3][3] = new TetriminoNode(new Color(0, 0, 255), 2, 2);
        }
    }
}
