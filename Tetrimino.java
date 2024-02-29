import java.awt.Color;

public class Tetrimino {
    protected TetriminoNode[] nodes;
    protected TetriminoNode[][] rotations;
    protected int id; // The current Tetrimino ID (First Tetrimino in game = 0, next one is 1, etc.)
    protected int direction = 0; // The current rotation direction of the Tetrimino
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
        if (!MyGame.board.alive) return;
        
        boolean wallKick = false;
        MyGame.doActions = false;

        int nextDirection = this.direction + 1 * factor;
        if (nextDirection < 0) nextDirection = 3;
        else if (nextDirection > 3) nextDirection = 0;

        for (int i = 0; i < rotations[nextDirection].length; i++) {
            TetriminoNode node = rotations[nextDirection][i];

            if (!canRotate(node.row, node.col)) wallKick = true;
        }

        if (wallKick) {
            wallKick(factor, nextDirection);
        } else {
            updateRotations(factor, new int[] {0, 0});
        }

        MyGame.doActions = true;
    }

    private void wallKick(int factor, int nextDirection) {
        int[][] offsets = new int[][] {{0, 0}, {0, 0}, {0, 0}, {0, 0}};
        if (getType().equals("IPiece"))
            switch (this.direction) {
                // 0
                case 0:
                    // 0 -> R
                    if (factor == 1) {
                        offsets = new int[][] {{-2, 0}, {1, 0}, {-2, -1}, {1, 2}};
                    } else { // 0 -> L
                        offsets = new int[][] {{-1, 0}, {2, 0}, {-1, 2}, {2, -1}};
                    }
                    break;
                
                // R
                case 1:
                    // R -> 2
                    if (factor == 1) {
                        offsets = new int[][] {{-1, 0}, {2, 0}, {-1, 2}, {2, -1}};
                    } else { // R -> 0
                        offsets = new int[][] {{2, 0}, {-1, 0}, {2, 1}, {-1, -2}};
                    }
                    break;
                
                // 2
                case 2:
                    // 2 -> L
                    if (factor == 1) {
                        offsets = new int[][] {{2, 0}, {-1, 0}, {2, 1}, {-1, -2}};
                    } else { // 2 -> R
                        offsets = new int[][] {{1, 0}, {-2, 0}, {1, -2}, {-2, 1}};
                    }
                    break;
                
                // L
                case 3:
                    // L -> 0
                    if (factor == 1) {
                        offsets = new int[][] {{1, 0}, {-2, 0}, {1, -2}, {-2, 1}};
                    } else { // L -> 2
                        offsets = new int[][] {{-2, 0}, {1, 0}, {-2, -1}, {1, 2}};
                    }
                    break;
            }

        else {
            switch (this.direction) {
                // 0
                case 0:
                    // 0 -> R
                    if (factor == 1) {
                        offsets = new int[][] {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}};
                    } else { // 0 -> L
                        offsets = new int[][] {{1, 0}, {1, 1}, {0, -2}, {1, -2}};
                    }
                    break;

                // R
                case 1:
                    // R -> 2
                    if (factor == 1) {
                        offsets = new int[][] {{1, 0}, {1, -1}, {0, 2}, {1, 2}};
                    } else { // R -> 0
                        offsets = new int[][] {{1, 0}, {1, -1}, {0, 2}, {1, 2}};
                    }
                    break;
                
                // 2
                case 2:
                    // 2 -> L
                    if (factor == 1) {
                        offsets = new int[][] {{1, 0}, {1, 1}, {0, -2}, {1, -2}};
                    } else { // 2 -> R
                        offsets = new int[][] {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}};
                    }
                    break;

                // L
                case 3:
                    // L -> 0
                    if (factor == 1) {
                        offsets = new int[][] {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}};
                    } else { // L -> 2
                        offsets = new int[][] {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}};
                    }
                    break;
            }
        }

        checkOffsets(offsets, factor, nextDirection);
    }

    private void updateRotations(int factor, int[] move) {
        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            MyGame.board.board[node.row][node.col] = null;
            node.row += move[1] * -1;
            node.col += move[0];
        }

        // Yes there is a more efficient way to do this, but for some reason it works better this way (Looping rotation)
        if (factor == 1) {
            if (this.direction < 3) this.direction++;
            else this.direction = 0;
        } else if (factor == -1) {
            if (this.direction > 0) this.direction--;
            else this.direction = 3;
        }

        nodes = rotations[this.direction];

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            MyGame.board.board[node.row][node.col] = node;
        }

        MyGame.board.updateArray();
        if (this.getType().equals("TPiece"))
            MyGame.tSpin = true;

        MyGame.notMove = true;
        if (MyGame.client != null) {
            SoundManager.playSound("sfx/Rotate.wav", false);
        }
        else {
            SoundManager.playSound("sfx/Action.wav", false);
        }
    }

    private void checkOffsets(int[][] offsets, int factor, int nextDirection) {
        boolean rotate;
        for (int r = 0; r < offsets.length; r++) {
            rotate = true;

            for (int i = 0; i < rotations[nextDirection].length; i++) {
                TetriminoNode node = rotations[nextDirection][i];
    
                if (!canRotate(node.row + offsets[r][1] * -1, node.col + offsets[r][0])) {
                    rotate = false;
                    break;
                }
            }

            if (!rotate) continue;

            for (int k = 0; k < rotations.length; k++) {
                for (int i = 0; i < rotations[k].length; i++) {
                    if (rotations[k].equals(nodes)) continue;
                    TetriminoNode node = rotations[k][i];
    
                    node.row += offsets[r][1] * -1;
                    node.col += offsets[r][0];
                }
            }

            updateRotations(factor, new int[] {offsets[r][0], offsets[r][1]});
            break;
        }
    }

    protected void createRotations() {}

    protected boolean canRotate(int nextRow, int nextCol) {
        if (!(nextRow >= 0 && nextRow < MyGame.board.board.length && nextCol >= 0 && nextCol < MyGame.board.board[0].length)) return false;

        if (MyGame.board.board[nextRow][nextCol] != null && MyGame.board.board[nextRow][nextCol].id != this.id) return false;

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