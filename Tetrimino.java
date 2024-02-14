import java.awt.Color;

public class Tetrimino {
    protected TetriminoNode[] nodes;
    protected TetriminoNode[][] rotations;
    protected int id; // The current Tetrimino ID (First Tetrimino in game = 0, next one is 1, etc.)
    protected int direction = 1; // The current rotation direction of the Tetrimino
    protected Color color;
    protected int colorIndex; // Label for which color in the Palette the Tetrimino uses

    public TetriminoNode[] getNodes() {
        return nodes;
    }

    public TetriminoNode[][] getRotations() {
        return rotations;
    }

    public void setID(int val) {
        this.id = val;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].id = this.id;
        }

        if (rotations == null) return;

        for (int i = 0; i < rotations.length; i++) {
            for (int k = 0; k < rotations[i].length; k++) {
                rotations[i][k].id = this.id;
            }
        }
    }

    public void rotate(int factor) {
        boolean wallKick = false;

        for (int i = 0; i < rotations[this.direction].length; i++) {
            TetriminoNode node = rotations[this.direction][i];

            if (!canRotate(node.row, node.col)) wallKick = true;
        }

        if (wallKick) {
            wallKick(factor);
        } else {
            updateRotations(factor);
        }
    }

    private void wallKick(int factor) {
        int[][] offsets;
        if (getType().equals("IPiece"))
            switch (this.direction) {
                case 0:
                    if (factor == 1) {
                        offsets = new int[][] {{2, 0}, {-1, 0}, {2, 1}, {-1, -2}};
                    } else {
                        offsets = new int[][] {{1, 0}, {-2, 0}, {1, -2}, {-2, 1}};
                    }

                    checkOffsets(offsets, factor);
                    break;

                case 1:
                    if (factor == 1) {
                        offsets = new int[][] {{-2, 0}, {1, 0}, {-2, -1}, {1, 2}};
                    } else {
                        offsets = new int[][] {{-1, 0}, {2, 0}, {-1, 2}, {2, -1}};
                    }

                    checkOffsets(offsets, factor);
                    break;

                case 2:
                    if (factor == 1) {
                        offsets = new int[][] {{-1, 0}, {2, 0}, {-1, 2}, {2, -1}};
                    } else {
                        offsets = new int[][] {{-2, 0}, {1, 0}, {-2, -1}, {1, 2}};
                    }

                    checkOffsets(offsets, factor);
                    break;
            }

        else {
            switch (this.direction) {
                case 0:
                    if (factor == 1) {
                        offsets = new int[][] {{1, 0}, {1, -1}, {0, 2}, {1, 2}};
                    } else {
                        offsets = new int[][] {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}};
                    }
                    
                    checkOffsets(offsets, factor);
                    break;

                case 1:
                    if (factor == 1) {
                        offsets = new int[][] {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}};
                    } else {
                        offsets = new int[][] {{1, 0}, {1, 1}, {0, -2}, {1, -2}};
                    }
                    
                    checkOffsets(offsets, factor);
                    break;

                case 2:
                    if (factor == 1) {
                        offsets = new int[][] {{1, 0}, {1, -1}, {0, 2}, {1, 2}};
                    } else {
                        offsets = new int[][] {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}};
                    }

                    checkOffsets(offsets, factor);
                    break;
            }
        }
    }

    private void updateRotations(int factor) {
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
        //SoundManager.playSound("sfx/Rotate.wav", false);
    }

    private void checkOffsets(int[][] offsets, int factor) {
        boolean rotate;
        for (int r = 0; r < offsets.length; r++) {
            rotate = true;

            for (int i = 0; i < rotations[this.direction].length; i++) {
                TetriminoNode node = rotations[this.direction][i];
    
                if (!canRotate(node.row + offsets[r][1], node.col + offsets[r][0])) {
                    rotate = false;
                    break;
                }
            }

            if (!rotate) continue;

            for (int k = 0; k < rotations.length; k++) {
                for (int i = 0; i < rotations[k].length; i++) {
                    TetriminoNode node = rotations[k][i];
    
                    node.row += offsets[r][1];
                    node.col += offsets[r][0];
                }
            }

            updateRotations(factor);
        }
    }

    protected void createRotations() {}

    protected boolean canRotate(int nextRow, int nextCol) {
        if (!(nextRow >= 0 && nextRow < MyGame.board.length && nextCol >= 0 && nextCol < MyGame.board[0].length)) return false;

        if (MyGame.board[nextRow][nextCol] != null && MyGame.board[nextRow][nextCol].id != this.id) return false;

        return true;
    }

    protected String getType() {
        return "Default";
    }

    protected void setNodesParent() {
        // Sets the nodes parent Tetrimino
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].parent = this;
        }

        if (rotations == null) return;
        
        for (int i = 0; i < rotations.length; i++) {
            for (int k = 0; k < rotations[i].length; k++) {
                rotations[i][k].parent = this;
            }
        }
    }
}