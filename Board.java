import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.TimerTask;

public class Board {
    public TetriminoNode[][] board;
    public Tetrimino currentTetrimino, nextTetrimino, heldTetrimino;

    public Board() {
        board = new TetriminoNode[20][10];
    }

    public void update() {
        ClearAnimation animation = MyGame.animation;

        if (currentTetrimino == null && MyGame.alive) {
            if (animation.rowsToClear.size() == 0) clearRow();

            if (animation.rowsToClear.size() == 0) {
                MyGame.held = false;
                swapTetriminos();
                move(1);
                MyGame.notMove = false;
            }
        } else if (currentTetrimino != null) {
            MyGame.messageCol = currentTetrimino.getNodes()[0].col;
            MyGame.messageRow = currentTetrimino.getNodes()[0].row;

            if (MyGame.direction != 0) {
                move(MyGame.direction);
            }
        }

        updateArray();
    }

    public void draw(Graphics pen) {
        // Draws the Board Tiles
        int tileSize = MyGame.tileSize;
        int offset = MyGame.offset;

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    pen.setColor(Color.gray);
                    pen.fillRect(c * tileSize + offset, r * tileSize + offset, tileSize, tileSize);
                    pen.setColor(Color.DARK_GRAY);
                    pen.drawRect(c * tileSize + offset, r * tileSize + offset, tileSize, tileSize);
                }
            }
        }

        // Draws the Tetrimino Nodes
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] != null) {
                    TetriminoNode curr = board[r][c];
                    
                    // Draws the current node to the board
                    curr.updateColor();
                    curr.draw(pen);

                    if (currentTetrimino != null) {
                        // Determines if the indicator for dropping the Tetrimino should be drawn for the current node
                        TetriminoNode[] nodes = currentTetrimino.getNodes();
                        boolean stop = true;

                        for (int i = 0; i < nodes.length; i++) {
                            if (nodes[i].equals(curr)) {
                                stop = false;
                                break;
                            }
                        }

                        if (!stop) {
                            // Draws the indicator for dropping the Tetrimino
                            for (int i = curr.row + 1; i < board.length; i++) {
                                if (board[i][c] == null) {
                                    pen.setColor(Color.LIGHT_GRAY);
                                    pen.fillRect(c * tileSize + offset, i * tileSize + offset, tileSize, tileSize);
                                    pen.setColor(Color.GRAY);
                                    pen.drawRect(c * tileSize + offset, i * tileSize + offset, tileSize, tileSize);
                                } else break;
                            }
                        }
                    }
                }
            }
        }

        if (MyGame.alive) {
            if (nextTetrimino != null) {
                TetriminoNode[] nodes = nextTetrimino.getNodes();
            
                // Draws the next Tetrimino
                for (int i = 0; i < nodes.length; i++) {
                    TetriminoNode node = nodes[i];
                    node.updateColor();
                    pen.drawImage(node.sprite, node.col * tileSize + (offset + board[0].length * tileSize - tileSize), node.row * tileSize + offset + 60, null);
                }
            }

            if (heldTetrimino != null) {
                TetriminoNode[] nodes = heldTetrimino.getNodes();

                // Draws the held Tetrimino
                for (int i = 0; i < nodes.length; i++) {
                    TetriminoNode node = nodes[i];
                    node.updateColor();
                    pen.drawImage(node.sprite, node.col * tileSize - 20, node.row * tileSize + 175, null);
                }
            }
        }
    }

    public void updateArray() {
        // Updates the the board used to move Tetriminos
        if (MyGame.menu == null) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] != null) {
                        TetriminoNode node = board[r][c];

                        if (node.row != r || node.col != c) {
                            board[r][c] = null;
                        }
                    }
                }
            }
        }
    }

    public void add(TetriminoNode t, int row, int col) {
        if (board[row][col] != null) MyGame.alive = false;
        board[row][col] = t;
    }

    public void automaticMove() {
        // Used for automatic Tetrimino movement to reschedule the task
        int time = MyGame.speed;
        if (MyGame.client != null) {
            if (MyGame.clock <= 20) time /= 6;
            else if (MyGame.clock <= 30) time /= 5;
            else if (MyGame.clock <= 60) time /= 4;
            else if (MyGame.clock <= 100) time /= 3;
            else if (MyGame.clock <= 150) time /= 2;
        }

        if (!MyGame.alive || !MyGame.doActions) {
            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    automaticMove();
                }
            }, (long)time);
            return;
        }

        moveTetriminos();
        MyGame.timer.schedule(new TimerTask() {
            public void run() {
                automaticMove();
            }
        }, (long)time);
    }

    public void moveTetriminos() {
        if (!MyGame.alive || MyGame.menu != null) return;

        if (currentTetrimino == null) return;

        TetriminoNode[] nodes = currentTetrimino.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].row >= board.length - 1) {
                if (!MyGame.notMove) currentTetrimino = null;
                return;
            }
            else if (board[nodes[i].row + 1][nodes[i].col] != null && board[nodes[i].row + 1][nodes[i].col].parent == null) {
                if (!MyGame.notMove) currentTetrimino = null;
                return;
            } 
            else if (board[nodes[i].row + 1][nodes[i].col] != null && !board[nodes[i].row + 1][nodes[i].col].parent.equals(nodes[i].parent)) {
                if (!MyGame.notMove) currentTetrimino = null;
                return;
            }
        }

        MyGame.tSpin = false;

        if (!MyGame.hardDropping) {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].row++;
            }
    
            TetriminoNode[][] rotations = currentTetrimino.getRotations();
    
            if (rotations != null) {
                for (int r = 0; r < rotations.length; r++) {
                    for (int c = 0; c < rotations[r].length; c++) {
                        if (rotations[r][c] != null) {
                            if (nodes.equals(rotations[r])) break;
                            
                            rotations[r][c].row++;
                        }
                    }
                }
            }
    
            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                board[node.row][node.col] = node;
            }
    
            updateArray();
        }
    }

    public Tetrimino getTetrimino() {
        if (!MyGame.alive) return null;

        int num = (int)(Math.random() * 7);
        Tetrimino t = null;

        switch (num) {
            case 0:
                t = new Tetriminos().new IPiece();
                break;

            case 1:
                t = new Tetriminos().new TPiece();
                break;

            case 2:
                t = new Tetriminos().new ZPiece();
                break;

            case 3:
                t = new Tetriminos().new SPiece();
                break;

            case 4:
                t = new Tetriminos().new OPiece();
                break;

            case 5:
                t = new Tetriminos().new LPiece();
                break;

            case 6:
                t = new Tetriminos().new JPiece();
                break;
        }

        t.setID(MyGame.tNum);
        MyGame.tNum++;
        MyGame.held = false;

        return t;
    }

    public Tetrimino getNextTetrimino() {
        if (!MyGame.alive) return null;

        String[] types = new String[] {"IPiece", "TPiece", "ZPiece", "SPiece", "OPiece", "LPiece", "JPiece"};

        int num = (int)(Math.random() * 7);

        while (types[num].equals(MyGame.prevType)) {
            num = (int)(Math.random() * 7);
        }

        MyGame.prevType = types[num];

        Tetrimino t = null;

        if (MyGame.pity >= MyGame.nextPity) {
            MyGame.pity = 0;
            MyGame.nextPity = (int)(Math.random() * 5) + 5;
            num = 0;
        }

        switch (num) {
            case 0:
                t = new Tetriminos().new IPiece(false);
                MyGame.pity = 0;
                break;

            case 1:
                t = new Tetriminos().new TPiece(false);
                break;

            case 2:
                t = new Tetriminos().new ZPiece(false);
                MyGame.pity++;
                break;

            case 3:
                t = new Tetriminos().new SPiece(false);
                MyGame.pity++;
                break;

            case 4:
                t = new Tetriminos().new OPiece(false);
                MyGame.pity++;
                break;

            case 5:
                t = new Tetriminos().new LPiece(false);
                MyGame.pity++;
                break;

            case 6:
                t = new Tetriminos().new JPiece(false);
                MyGame.pity++;
                break;
        }

        return t;
    }

    public void swapTetriminos() {
        if (board == null) return;

        if (nextTetrimino == null) return;

        switch (nextTetrimino.getType()) {
            case "IPiece":
                currentTetrimino = new Tetriminos().new IPiece();
                break;

            case "TPiece":
                currentTetrimino = new Tetriminos().new TPiece();
                break;

            case "ZPiece":
                currentTetrimino = new Tetriminos().new ZPiece();
                currentTetrimino.rotate(1);
                break;

            case "SPiece":
                currentTetrimino = new Tetriminos().new SPiece();
                currentTetrimino.rotate(1);
                break;

            case "OPiece":
                currentTetrimino = new Tetriminos().new OPiece();
                break;

            case "LPiece":
                currentTetrimino = new Tetriminos().new LPiece();
                break;

            case "JPiece":
                currentTetrimino = new Tetriminos().new JPiece();
                break;
        }

        if (currentTetrimino != null) currentTetrimino.setID(MyGame.tNum);
        MyGame.tNum++;

        nextTetrimino = getNextTetrimino();
    }

    public void hardDrop() {
        MyGame.tSpin = false;
        if (!MyGame.alive) return;

        MyGame.hardDropping = true;

        for (int k = 0; k < board.length; k++) {
            if (currentTetrimino == null) {
                MyGame.hardDropping = false;
                return;
            }

            TetriminoNode[] nodes = currentTetrimino.getNodes();

            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].row >= board.length - 1) {
                    currentTetrimino = null;
                    MyGame.hardDropping = false;
                    MyGame.held = false;
                    return;
                } else if (board[nodes[i].row + 1][nodes[i].col] != null && board[nodes[i].row + 1][nodes[i].col].id != nodes[i].id) {
                    currentTetrimino = null;
                    MyGame.hardDropping = false;
                    MyGame.held = false;
                    return;
                }
            }

            for (int i = 0; i < nodes.length; i++) {
                nodes[i].row++;
            }
    
            TetriminoNode[][] rotations = currentTetrimino.getRotations();
    
            if (rotations != null) {
                for (int r = 0; r < rotations.length; r++) {
                    for (int c = 0; c < rotations[r].length; c++) {
                        if (rotations[r][c] != null) {
                            boolean move = true;
                            if (nodes.equals(rotations[r])) move = false;
                            
                            if (move) rotations[r][c].row++;
                        }
                    }
                }
            }
    
            for (int i = 0; i < nodes.length; i++) {
                TetriminoNode node = nodes[i];
                board[node.row][node.col] = node;
            }
    
            updateArray();
        }

        MyGame.hardDropping = false;
        MyGame.held = false;
    }

    public void move(int direction) {
        if (!MyGame.alive || !MyGame.doActions) return;

        if (currentTetrimino == null) return;

        TetriminoNode[] nodes = currentTetrimino.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            switch (direction) {
                case -1:
                    if (nodes[i].col <= 0) return;
                    break;

                case 1:
                    if (nodes[i].col >= board[0].length - 1) return;
                    break;
            }

            if (board[nodes[i].row][nodes[i].col + 1 * direction] != null && board[nodes[i].row][nodes[i].col + 1 * direction].id != nodes[i].id) return;
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i].col += 1 * direction;
        }

        TetriminoNode[][] rotations = currentTetrimino.getRotations();

        if (rotations != null) {
            for (int r = 0; r < rotations.length; r++) {
                for (int c = 0; c < rotations[r].length; c++) {
                    if (rotations[r][c] != null) {
                        boolean move = true;
                        if (nodes.equals(rotations[r])) move = false;
                        if (move) rotations[r][c].col += 1 * direction;
                    }
                }
            }
        }

        for (int i = 0; i < nodes.length; i++) {
            TetriminoNode node = nodes[i];
            board[node.row][node.col] = node;
        }

        updateArray();
        if (MyGame.client != null) {
            SoundManager.playSound("sfx/Move.wav", false);
        }
        else {
            SoundManager.playSound("sfx/Action.wav", false);
        }
    }

    public void hold() {
        if (MyGame.menu != null || currentTetrimino == null) return;

        for (int i = 0; i < currentTetrimino.nodes.length; i++) {
            board[currentTetrimino.nodes[i].row][currentTetrimino.nodes[i].col] = null;
        }

        if (heldTetrimino == null) {
            switch (currentTetrimino.getType()) {
                case "IPiece":
                    heldTetrimino = new Tetriminos().new IPiece(false);
                    break;
    
                case "TPiece":
                    heldTetrimino = new Tetriminos().new TPiece(false);
                    break;
    
                case "ZPiece":
                    heldTetrimino = new Tetriminos().new ZPiece(false);
                    break;
    
                case "SPiece":
                    heldTetrimino = new Tetriminos().new SPiece(false);
                    break;
    
                case "OPiece":
                    heldTetrimino = new Tetriminos().new OPiece(false);
                    break;
    
                case "LPiece":
                    heldTetrimino = new Tetriminos().new LPiece(false);
                    break;
    
                case "JPiece":
                    heldTetrimino = new Tetriminos().new JPiece(false);
                    break;
            }
            
            currentTetrimino = null;
        } else {
            String type = currentTetrimino.getType();

            switch (heldTetrimino.getType()) {
                case "IPiece":
                    currentTetrimino = new Tetriminos().new IPiece();
                    break;
    
                case "TPiece":
                    currentTetrimino = new Tetriminos().new TPiece();
                    break;
    
                case "ZPiece":
                    currentTetrimino = new Tetriminos().new ZPiece();
                    currentTetrimino.rotate(1);
                    break;
    
                case "SPiece":
                    currentTetrimino = new Tetriminos().new SPiece();
                    currentTetrimino.rotate(1);
                    break;
    
                case "OPiece":
                    currentTetrimino = new Tetriminos().new OPiece();
                    break;
    
                case "LPiece":
                    currentTetrimino = new Tetriminos().new LPiece();
                    break;
    
                case "JPiece":
                    currentTetrimino = new Tetriminos().new JPiece();
                    break;
            }

            switch (type) {
                case "IPiece":
                    heldTetrimino = new Tetriminos().new IPiece(false);
                    break;
    
                case "TPiece":
                    heldTetrimino = new Tetriminos().new TPiece(false);
                    break;
    
                case "ZPiece":
                    heldTetrimino = new Tetriminos().new ZPiece(false);
                    break;
    
                case "SPiece":
                    heldTetrimino = new Tetriminos().new SPiece(false);
                    break;
    
                case "OPiece":
                    heldTetrimino = new Tetriminos().new OPiece(false);
                    break;
    
                case "LPiece":
                    heldTetrimino = new Tetriminos().new LPiece(false);
                    break;
    
                case "JPiece":
                    heldTetrimino = new Tetriminos().new JPiece(false);
                    break;
            }
        }

        MyGame.held = true;
        if (MyGame.client != null) {
            SoundManager.playSound("sfx/Hold.wav", false);
        }
        else {
            SoundManager.playSound("sfx/Action.wav", false);
        }
    }

    public void clearRow() {
        if (!MyGame.alive || board == null) return;

        int linesCleared = 0;

        for (int r = 0; r < board.length; r++) {
            boolean clear = true;

            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == null) {
                    clear = false;
                    break;
                }
            }

            if (clear) {
                MyGame.lines++;
                linesCleared++;

                MyGame.messageRow = r;
                MyGame.animation.rowsToClear.add(r);

                speedCalculation();
            }
        }

        if (MyGame.prevLinesCleared == 4 && linesCleared == 4) {
            MyGame.prevLinesCleared = linesCleared;
            linesCleared++;
        } else {
            if (linesCleared > 0) MyGame.prevLinesCleared = linesCleared;
        }
        
        int scoreAdded = 0;
        switch (linesCleared) {
            case 1:
                scoreAdded += 40 * (MyGame.level + 1);
                MyGame.score += 40 * (MyGame.level + 1);
                if (!MyGame.tSpin) MyGame.messages.add(new Message("+" + scoreAdded + " Single!"));
                else {
                    scoreAdded += 50 * (MyGame.level + 1);
                    MyGame.messages.add(new Message("+" + scoreAdded + " T-Spin Single!"));
                    MyGame.score += 50 * (MyGame.level + 1);
                }
                
                break;

            case 2:
                MyGame.score += 100 * (MyGame.level + 1);
                scoreAdded += 100 * (MyGame.level + 1);
                if (!MyGame.tSpin) MyGame.messages.add(new Message("+" + scoreAdded + " Double!"));
                else {
                    scoreAdded += 100 * (MyGame.level + 1);
                    MyGame.messages.add(new Message("+" + scoreAdded + " T-Spin Double!"));
                    MyGame.score += 100 * (MyGame.level + 1);
                }
                
                break;

            case 3:
                scoreAdded += 300 * (MyGame.level + 1);
                MyGame.score += 300 * (MyGame.level + 1);
                if (!MyGame.tSpin) MyGame.messages.add(new Message("+" + scoreAdded + " Triple!"));
                else {
                    scoreAdded += 300 * (MyGame.level + 1);
                    MyGame.messages.add(new Message("+" + scoreAdded + " T-Spin Triple!"));
                    MyGame.score += 300 * (MyGame.level + 1);
                }

                break;

            case 4:
                scoreAdded += 1200 * (MyGame.level + 1);
                MyGame.score += 1200 * (MyGame.level + 1);
                MyGame.messages.add(new Message("+" + scoreAdded + " Tetris!"));

                break;

            case 5:
                scoreAdded += 2000 * (MyGame.level + 1);
                MyGame.score += 2000 * (MyGame.level + 1);
                MyGame.messages.add(new Message("+" + scoreAdded + " Back-to-Back Tetris!"));
                
                break;
        }

        playClearSound(linesCleared);

        if (linesCleared > 0 && MyGame.client != null) {
            if (MyGame.client.queue.size() == 0) sendLines(linesCleared);
            else MyGame.client.changeTimer(linesCleared);
        }
        
        MyGame.tSpin = false;

        MyGame.messageDirection = (int)(Math.random() * 2);

        if (MyGame.messages.size() > 0) {
            Message msg = MyGame.messages.get(MyGame.messages.size() - 1);
            msg.x = MyGame.messageCol * MyGame.tileSize + MyGame.offset;
            msg.y = MyGame.messageRow * MyGame.tileSize + MyGame.offset;

            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    MyGame.messages.remove(msg);
                }
            }, (long)750);
        }
    }

    public void playClearSound(int lines) {
        switch (lines) {
            case 1:
                if (MyGame.client != null) {
                    SoundManager.playSound("sfx/Single.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/LineClearSolo.wav", false);
                }
                break;

            case 2:
                if (MyGame.client != null) {
                    SoundManager.playSound("sfx/Double.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/LineClearSolo.wav", false);
                }
                break;

            case 3:
                if (MyGame.client != null) {
                    SoundManager.playSound("sfx/Triple.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/LineClearSolo.wav", false);
                }
                break;

            case 4:
                if (MyGame.client != null) {
                    SoundManager.playSound("sfx/Tetris.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/TetrisSolo.wav", false);
                }
                break;

            case 5:
                if (MyGame.client != null) {
                    SoundManager.playSound("sfx/Tetris.wav", false);
                }
                else {
                    SoundManager.playSound("sfx/TetrisSolo.wav", false);
                }
                break;
        }
    }

    public void speedCalculation() {
        if ((MyGame.level + 1) * 10 - MyGame.lines <= 0) {
            MyGame.level++;
            MyGame.levelMessage.contents = "Level Up!";

            if (MyGame.client != null) {
                SoundManager.playSound("sfx/LUp.wav", false);
            }
            else {
                SoundManager.playSound("sfx/LevelUpSolo.wav", false);
            }

            if (MyGame.level == 9) {
                SoundManager.stopAllSounds();
                SoundManager.playSound("sfx/Level9.wav", true);
            }
            else if (MyGame.level == 19) {
                SoundManager.stopAllSounds();
                SoundManager.playSound("sfx/Level19.wav", true);
            }

            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    MyGame.levelMessage.contents = "";
                }
            }, (long)750);
        }
    }

    public void sendLines(int linesCleared) {
        // Sends lines to the other players when a lines are cleared
        if (MyGame.client != null && MyGame.client.output != null) {
            if (MyGame.linesToSend >= MyGame.timesCleared || linesCleared >= 4) {
                if (linesCleared > 0) {
                    String recieve = MyGame.client.lobby.get((int)(Math.random() * MyGame.client.lobby.size())).contents;
                    while (recieve.equals(MyGame.client.name) || recieve.equals("Lobby")) {
                        recieve = MyGame.client.lobby.get((int)(Math.random() * MyGame.client.lobby.size())).contents;
                    }
                    
                    MyGame.client.output.println(MyGame.client.name + " sent " + linesCleared + " lines to " + recieve);
                    MyGame.status.addMessage("Sent " + linesCleared + " lines to " + recieve + ".");

                    MyGame.linesToSend = 0;
                    MyGame.timesCleared = (int)(Math.random() * 4) + 1;
                }
            } else {
                if (MyGame.linesToSend < 5) MyGame.linesToSend += linesCleared;
            }
        }
    }

    public void recieveLines(int lines) {
        // Recieves lines in the queue
        if (MyGame.animation.clearing || MyGame.hardDropping) {
            MyGame.timer.schedule(new TimerTask() {
                public void run() {
                    recieveLines(lines);
                }
            }, (long)100);
            return;
        } 

        if (MyGame.menu == null && MyGame.alive) {
            int row = board.length - 1;

            for (int i = 0; i < lines; i++) {
                for (int r = 0; r < board.length - 1; r++) {
                    for (int c = 0; c < board[r].length; c++) {
                        if (board[r + 1][c] != null) {
                            board[r][c] = board[r + 1][c];
                            board[r + 1][c] = null;

                            if (board[r][c] != null) {
                                row = --board[r][c].row;
                            }
                        }
                    }
                }
            }

            for (int r = row + 1; r < row + 1 + lines; r++) {
                int stop = (int)(Math.random() * board[r].length);
                for (int c = 0; c < board[r].length; c++) {
                    if (c != stop) {
                        board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                        board[r][c].updateID();
                        updateArray();
                    }
                }
            }

            SoundManager.playSound("sfx/Alert.wav", false);

            MyGame.recieving = false;
        }
    }

    public void startGame() {
        MyGame.alive = true;
        board = new TetriminoNode[20][10];

        if (MyGame.client == null) {
            if (MyGame.level < 9) SoundManager.playSound("sfx/MusicSolo.wav", true);
            else if (MyGame.level < 19) SoundManager.playSound("sfx/Level9.wav", true);
            else SoundManager.playSound("sfx/Level19.wav", true);
            MyGame.clock = 10;
        }
        else {
            SoundManager.playSound("sfx/Battle.wav", false);
            MyGame.clock = 300;
            MyGame.client.deaths = 0;
        }
        
        MyGame.lines = 0;
        MyGame.score = 0;
        MyGame.timesCleared = 0;

        try {
            if (MyGame.client == null) {
                MyGame.level = MyGame.save.startLevel;
                MyGame.tileSize = 16;
                MyGame.palette.sheet = new SpriteSheetLoader(16, 16, 10, 3);
            }
            else {
                MyGame.level = 0;
                MyGame.tileSize = 25;
                MyGame.palette.sheet = new SpriteSheetLoader(25, 25, 10, 3, "gfx/PaletteBattle.png");
            }
        } catch (IOException e) {}

        MyGame.pity = 0;
        MyGame.nextPity = 5;
        MyGame.palette.currentPalette = 0;
        MyGame.menu = null;
        if (MyGame.client != null) recieveLines(0);
        MyGame.offset = 125;

        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();
        heldTetrimino = null;

        updateArray();
        move(1);

        MyGame.challenge = false;
    }

    public void startChallenge() {
        MyGame.challenge = true;

        MyGame.alive = true;
        MyGame.lines = 0;
        MyGame.score = 0;
        MyGame.level = MyGame.save.startLevel;

        MyGame.pity = 0;
        MyGame.nextPity = 5;
        MyGame.palette.currentPalette = 0;
        MyGame.menu = null;
        MyGame.offset = 125;

        board = new TetriminoNode[20][10];

        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();
        heldTetrimino = null;

        for (int r = board.length / 2 + 1; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (c % 2 == r % 2) {
                    board[r][c] = new TetriminoNode(Color.DARK_GRAY, r, c, -1);
                    board[r][c].updateID();
                }
            }
        }

        updateArray();
        move(1);

        if (MyGame.level < 9) SoundManager.playSound("sfx/MusicSolo.wav", true);
        else if (MyGame.level < 19) SoundManager.playSound("sfx/Level9.wav", true);
        else SoundManager.playSound("sfx/Level19.wav", true);
        MyGame.clock = 10;
    }

    public void reset() {
        MyGame.alive = true;
        MyGame.lines = 0;
        MyGame.score = 0;
        MyGame.level = 0;
        MyGame.pity = 0;
        MyGame.nextPity = 5;
        MyGame.palette.currentPalette = 0;
        MyGame.menu = null;
        board = new TetriminoNode[20][10];
        if (MyGame.client != null) recieveLines(0);
        MyGame.offset = 125;

        currentTetrimino = getTetrimino();
        nextTetrimino = getNextTetrimino();
        heldTetrimino = null;

        updateArray();
        move(1);

        if (MyGame.client != null) {
            MyGame.client.deaths++;
            MyGame.client.queue.clear();
        }
    }
}