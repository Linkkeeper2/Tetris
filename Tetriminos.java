import java.awt.Color;

public class Tetriminos {
    public class IPiece extends Tetrimino {
        public IPiece() {
            this.colorIndex = 0;
            this.color = new Color(0, 255, 255);
            TetriminoNode[] nodes = new TetriminoNode[4];

            for (int i = 0; i < 4; i++) {
                nodes[i] = new TetriminoNode(color, 0, i + 2, colorIndex);
                MyGame.add(nodes[i], 0, i + 2);
            }

            this.nodes = nodes;
            createRotations();
        }

        public IPiece(boolean add) {
            if (!add) {
                this.color = new Color(0, 255, 255);
                TetriminoNode[] nodes = new TetriminoNode[4];

                for (int i = 0; i < 4; i++) {
                    nodes[i] = new TetriminoNode(color, 0, i + 2, colorIndex);
                }

                this.nodes = nodes;
            }
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length; i++) {
                rotations[0][i] = new TetriminoNode(color, 0, i + 2, colorIndex);
            }

            for (int i = 0; i < rotations[1].length; i++) {
                rotations[1][i] = new TetriminoNode(color, i, 4, colorIndex);
            }

            for (int i = 0; i < rotations[2].length; i++) {
                rotations[2][i] = new TetriminoNode(color, 2, i + 2, colorIndex);
            }

            for (int i = 0; i < rotations[3].length; i++) {
                rotations[3][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
        }
    
        protected String getType() {
            return "IPiece";
        }
    }

    public class TPiece extends Tetrimino  {
        public TPiece() {
            this.colorIndex = 1;
            this.color = new Color(255, 0, 255);
            TetriminoNode[] nodes = new TetriminoNode[4];

            for (int i = 0; i < 3; i++) {
                nodes[i] = new TetriminoNode(color, 1, i + 3, colorIndex);
                MyGame.add(nodes[i], 1, i + 3);
            }

            nodes[3] = new TetriminoNode(color, 0, 4, colorIndex);
            MyGame.add(nodes[3], 0, 4);

            this.nodes = nodes;
            createRotations();
        }

        public TPiece(boolean add) {
            if (!add) {
                this.color = new Color(255, 0, 255);
                TetriminoNode[] nodes = new TetriminoNode[4];

                for (int i = 0; i < 3; i++) {
                    nodes[i] = new TetriminoNode(color, 1, i + 3, colorIndex);
                }

                nodes[3] = new TetriminoNode(color, 0, 4, colorIndex);

                this.nodes = nodes;
            }
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length - 1; i++) {
                rotations[0][i] = new TetriminoNode(color, 1, i + 2, colorIndex);
            }
            rotations[0][3] = new TetriminoNode(color, 0, 3, colorIndex);

            for (int i = 0; i < rotations[1].length - 1; i++) {
                rotations[1][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
            rotations[1][3] = new TetriminoNode(color, 1, 4, colorIndex);

            for (int i = 0; i < rotations[2].length - 1; i++) {
                rotations[2][i] = new TetriminoNode(color, 1, i + 2, colorIndex);
            }
            rotations[2][3] = new TetriminoNode(color, 2, 3, colorIndex);

            for (int i = 0; i < rotations[3].length - 1; i++) {
                rotations[3][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
            rotations[3][3] = new TetriminoNode(color, 1, 2, colorIndex);
        }
    
        protected String getType() {
            return "TPiece";
        }
    }

    public class ZPiece extends Tetrimino  {
        public ZPiece() {
            this.colorIndex = 2;
            this.color = new Color(255, 0, 0);
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(color, 2, 3, colorIndex);
            nodes[1] = new TetriminoNode(color, 1, 3, colorIndex);
            nodes[2] = new TetriminoNode(color, 1, 4, colorIndex);
            nodes[3] = new TetriminoNode(color, 0, 4, colorIndex);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            createRotations();
        }

        public ZPiece(boolean add) {
            if (!add) {
                this.color = new Color(255, 0, 0);
                TetriminoNode[] nodes = new TetriminoNode[4];

                nodes[0] = new TetriminoNode(color, 2, 3, colorIndex);
                nodes[1] = new TetriminoNode(color, 1, 3, colorIndex);
                nodes[2] = new TetriminoNode(color, 1, 4, colorIndex);
                nodes[3] = new TetriminoNode(color, 0, 4, colorIndex);

                this.nodes = nodes;
            }
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            rotations[0][0] = new TetriminoNode(color, 0, 2, colorIndex);
            rotations[0][1] = new TetriminoNode(color, 0, 3, colorIndex);
            rotations[0][2] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[0][3] = new TetriminoNode(color, 1, 4, colorIndex);

            rotations[1][0] = new TetriminoNode(color, 0, 4, colorIndex);
            rotations[1][1] = new TetriminoNode(color, 1, 4, colorIndex);
            rotations[1][2] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[1][3] = new TetriminoNode(color, 2, 3, colorIndex);

            rotations[2][0] = new TetriminoNode(color, 1, 2, colorIndex);
            rotations[2][1] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[2][2] = new TetriminoNode(color, 2, 3, colorIndex);
            rotations[2][3] = new TetriminoNode(color, 2, 4, colorIndex);

            rotations[3][0] = new TetriminoNode(color, 0, 3, colorIndex);
            rotations[3][1] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[3][2] = new TetriminoNode(color, 1, 2, colorIndex);
            rotations[3][3] = new TetriminoNode(color, 2, 2, colorIndex);
        }
    
        protected String getType() {
            return "ZPiece";
        }
    }

    public class SPiece extends Tetrimino  {
        public SPiece() {
            this.colorIndex = 3;
            this.color = new Color(0, 255, 0);
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(color, 2, 4, colorIndex);
            nodes[1] = new TetriminoNode(color, 1, 4, colorIndex);
            nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
            nodes[3] = new TetriminoNode(color, 0, 3, colorIndex);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            createRotations();
        }

        public SPiece(boolean add) {
            if (!add) {
                this.color = new Color(0, 255, 0);
                TetriminoNode[] nodes = new TetriminoNode[4];

                nodes[0] = new TetriminoNode(color, 2, 4, colorIndex);
                nodes[1] = new TetriminoNode(color, 1, 4, colorIndex);
                nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
                nodes[3] = new TetriminoNode(color, 0, 3, colorIndex);

                this.nodes = nodes;
            }
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            rotations[0][0] = new TetriminoNode(color, 1, 2, colorIndex);
            rotations[0][1] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[0][2] = new TetriminoNode(color, 0, 3, colorIndex);
            rotations[0][3] = new TetriminoNode(color, 0, 4, colorIndex);

            rotations[1][0] = new TetriminoNode(color, 0, 3, colorIndex);
            rotations[1][1] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[1][2] = new TetriminoNode(color, 1, 4, colorIndex);
            rotations[1][3] = new TetriminoNode(color, 2, 4, colorIndex);

            rotations[2][0] = new TetriminoNode(color, 2, 2, colorIndex);
            rotations[2][1] = new TetriminoNode(color, 2, 3, colorIndex);
            rotations[2][2] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[2][3] = new TetriminoNode(color, 1, 4, colorIndex);

            rotations[3][0] = new TetriminoNode(color, 0, 2, colorIndex);
            rotations[3][1] = new TetriminoNode(color, 1, 2, colorIndex);
            rotations[3][2] = new TetriminoNode(color, 1, 3, colorIndex);
            rotations[3][3] = new TetriminoNode(color, 2, 3, colorIndex);
        }
    
        protected String getType() {
            return "SPiece";
        }
    }

    public class OPiece extends Tetrimino  {
        public OPiece() {
            this.colorIndex = 4;
            this.color = new Color(255, 255, 0);
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(color, 1, 4, colorIndex);
            nodes[1] = new TetriminoNode(color, 0, 4, colorIndex);
            nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
            nodes[3] = new TetriminoNode(color, 0, 3, colorIndex);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            this.direction = -1;
        }

        public OPiece(boolean add) {
            if (!add) {
                this.color = new Color(255, 255, 0);
                TetriminoNode[] nodes = new TetriminoNode[4];

                nodes[0] = new TetriminoNode(color, 1, 4, colorIndex);
                nodes[1] = new TetriminoNode(color, 0, 4, colorIndex);
                nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
                nodes[3] = new TetriminoNode(color, 0, 3, colorIndex);

                this.nodes = nodes;
                this.direction = -1;
            }
        }
    
        protected String getType() {
            return "OPiece";
        }
    }

    public class LPiece extends Tetrimino  {
        public LPiece() {
            this.colorIndex = 5;
            this.color = new Color(255, 100, 0);
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(color, 0, 4, colorIndex);
            nodes[1] = new TetriminoNode(color, 1, 4, colorIndex);
            nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
            nodes[3] = new TetriminoNode(color, 1, 2, colorIndex);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            createRotations();
        }

        public LPiece(boolean add) {
            if (!add) {
                this.color = new Color(255, 100, 0);
                TetriminoNode[] nodes = new TetriminoNode[4];

                nodes[0] = new TetriminoNode(color, 0, 4, colorIndex);
                nodes[1] = new TetriminoNode(color, 1, 4, colorIndex);
                nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
                nodes[3] = new TetriminoNode(color, 1, 2, colorIndex);

                this.nodes = nodes;
            }
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length - 1; i++) {
                rotations[0][i] = new TetriminoNode(color, 1, i + 2, colorIndex);
            }
            rotations[0][3] = new TetriminoNode(color, 0, 4, colorIndex);

            for (int i = 0; i < rotations[1].length - 1; i++) {
                rotations[1][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
            rotations[1][3] = new TetriminoNode(color, 2, 4, colorIndex);

            for (int i = 0; i < rotations[2].length - 1; i++) {
                rotations[2][i] = new TetriminoNode(color, 1, i + 2, colorIndex);
            }
            rotations[2][3] = new TetriminoNode(color, 2, 2, colorIndex);

            for (int i = 0; i < rotations[3].length - 1; i++) {
                rotations[3][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
            rotations[3][3] = new TetriminoNode(color, 0, 2, colorIndex);
        }
    
        protected String getType() {
            return "LPiece";
        }
    }

    public class JPiece extends Tetrimino  {
        public JPiece() {
            this.colorIndex = 6;
            this.color = new Color(0, 0, 255);
            TetriminoNode[] nodes = new TetriminoNode[4];

            nodes[0] = new TetriminoNode(color, 0, 2, colorIndex);
            nodes[1] = new TetriminoNode(color, 1, 2, colorIndex);
            nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
            nodes[3] = new TetriminoNode(color, 1, 4, colorIndex);

            for (int i = 0; i < 4; i++) {
                MyGame.add(nodes[i], nodes[i].row, nodes[i].col);
            }

            this.nodes = nodes;
            createRotations();
        }

        public JPiece(boolean add) {
            if (!add) {
                this.color = new Color(0, 0, 255);
                TetriminoNode[] nodes = new TetriminoNode[4];

                nodes[0] = new TetriminoNode(color, 0, 2, colorIndex);
                nodes[1] = new TetriminoNode(color, 1, 2, colorIndex);
                nodes[2] = new TetriminoNode(color, 1, 3, colorIndex);
                nodes[3] = new TetriminoNode(color, 1, 4, colorIndex);

                this.nodes = nodes;
            }
        }

        protected void createRotations() {
            rotations = new TetriminoNode[4][4];

            // Default
            for (int i = 0; i < rotations[0].length - 1; i++) {
                rotations[0][i] = new TetriminoNode(color, 1, i + 2, colorIndex);
            }
            rotations[0][3] = new TetriminoNode(color, 0, 2, colorIndex);

            for (int i = 0; i < rotations[1].length - 1; i++) {
                rotations[1][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
            rotations[1][3] = new TetriminoNode(color, 0, 4, colorIndex);

            for (int i = 0; i < rotations[2].length - 1; i++) {
                rotations[2][i] = new TetriminoNode(color, 1, i + 2, colorIndex);
            }
            rotations[2][3] = new TetriminoNode(color, 2, 4, colorIndex);

            for (int i = 0; i < rotations[3].length - 1; i++) {
                rotations[3][i] = new TetriminoNode(color, i, 3, colorIndex);
            }
            rotations[3][3] = new TetriminoNode(color, 2, 2, colorIndex);
        }
    
        protected String getType() {
            return "JPiece";
        }
    }
}
