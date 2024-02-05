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
            this.rotations = new TetriminoNode[4][4];
        }

        public void rotate(int factor) {
            for (int i = 0; i < rotations[this.direction].length; i++) {
                TetriminoNode node = rotations[this.direction][i];

                if (!canRotate(node.row, node.col)) return;
            }

            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                MyGame.board[node.row][node.col] = null;
            }

            nodes = rotations[this.direction];

            // Yes there is a more efficient way to do this, but for some reason it works better this way (Looping rotation)
            if (factor == 1) {
                if (this.direction < 3) this.direction++;
                else this.direction = 0;
            } else if (factor == -1) {
                if (this.direction > 0) this.direction--;
                else this.direction = 3;
            }

            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                MyGame.board[node.row][node.col] = node;
            }
            
            MyGame.updateArray();
        }

        public void updateRotations() {
            rotations = new TetriminoNode[4][4];

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
        }
    }
}
