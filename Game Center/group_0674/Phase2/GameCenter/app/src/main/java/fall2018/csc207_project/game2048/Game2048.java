package fall2018.csc207_project.game2048;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import fall2018.csc207_project.Interfaces.Game;

/**
 * the game data model of 2048
 */
public class Game2048 extends Observable implements Game, Serializable {

    /**
     * serial number of the game
     */
    private static final long serialVersionUID = 772895212901L;

    /**
     * the name of game stored in the internal storage
     */
    public static final String GAMENAME = "game2048";

    /**
     * the player name
     */
    private String player;

    /**
     * the board data model
     */
    private Board board;  // settings index 0

    /**
     * the complexity of the game (# of cells / row)
     */
    private int complexity;

    /**
     * number of undo that can be done
     */
    private int undoStep; // settings index 2

    /**
     * score of the game
     */
    private int score;     // settings index 1

    /**
     * the highest number on the current board
     */
    private int highestTile;

    /**
     * if the move can be done
     */
    private boolean moveAvailable;

    /**
     * track the undo path
     */
    private LinkedList<LinkedList<Integer>> undoList = new LinkedList<>(); // setting index 3


    /**
     * construct the game data model of 2048
     *
     * @param settings parametres needed to build the game
     */
    public Game2048(List<Object> settings) {
        //if this is new, initialize it
        if (settings.size() == 1) {
            this.complexity = (Integer) settings.get(0);
            this.board = new Board(complexity);
            this.undoStep = 3;
            this.score = 0;

            addToUndoList();

            //load the autosave version
        } else {
            this.board = new Board((Board) settings.get(0));
            this.complexity = (Integer) settings.get(1);
            this.undoStep = (Integer) settings.get(2);
            this.score = (Integer) settings.get(3);
            this.highestTile = (Integer) settings.get(4);
            this.moveAvailable = (Boolean) settings.get(5);
            this.undoList = new LinkedList<>();
            this.undoList.addAll((LinkedList) settings.get(6));


            addToUndoList();
        }

    }

    @Override
    public List<Object> getSetting() {
        LinkedList<Object> result = new LinkedList<>();
        result.add(new Board(board));
        result.add(complexity);
        result.add(undoStep);
        result.add(score);
        result.add(highestTile);
        result.add(moveAvailable);
        result.add(undoList);

        return result;
    }

    /**
     * set the player of the game
     *
     * @param playerName the name of the player
     */
    void setPlayer(String playerName) {
        this.player = playerName;
    }

    /**
     * get the name of the player
     *
     * @return the name of the player
     */
    String getPlayer() {
        return player;
    }

    /**
     * get the number of undo available
     *
     * @return the number of undo available
     */
    int getUndoStep() {
        return undoStep;
    }

    /**
     * restart the game from game data model
     * re-initialize the new game
     */
    public void restart() {
        this.board = new Board(complexity);
        this.undoStep = 3;
        this.score = 0;
        this.undoList.clear();
        addToUndoList();
        setChanged();
        notifyObservers();
    }

    /**
     * add undo path to the game data model
     */
    private void addToUndoList() {
        //if the number of moves is less than undo available
        if (this.undoList.size() < this.undoStep + 1)
            this.undoList.add(record(board));
            //if the number of moves is more than undo available
        else {
            this.undoList.removeFirst();
            this.undoList.add(record(board));
        }
    }


    /**
     * record all the tile numbers on the board
     *
     * @param board the board data for the game
     * @return the list of the tile numbers on the board
     */
    private LinkedList<Integer> record(Board board) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int y = 0; y < board.getBoardSize(); y++) {
            for (int x = 0; x < board.getBoardSize(); x++) {
                list.add(board.getTile(x, y).getNum());
            }
        }
        list.add(score);
        return list;
    }


    /**
     * perform undo on the data model
     */
    void undo() {
        if (this.undoList.size() >= 2 && undoStep != 0) {
            undoList.removeLast();
            LinkedList<Integer> temp = undoList.getLast();
            board.syncBoard(temp);
            undoStep -= 1;
            this.score = temp.getLast();
            setChanged();
            notifyObservers();
        }
    }


    /**
     * get the highest number on the tile of this board
     *
     * @return the highest number on the tile of this board
     */
    int getHighestTile() {
        return highestTile;
    }

    /**
     * get the complexity of this game
     *
     * @return the complexity of this game
     */
    public int getComplexity() {
        return complexity;
    }

    /**
     * get the board data model
     *
     * @return the board data model
     */
    public Board getBoard() {
        return board;
    }

    /**
     * get the score of the game
     *
     * @return the score of the game
     */
    public int getScore() {
        return score;
    }

    /**
     * perform the touch move as a vector on the data model
     * and calculate if the move is valid
     *
     * @param countDownFrom move from left bottom or right top
     * @param yDirection    vertical coordinate of the move
     * @param xDirection    horizontal coordinate of the move
     * @return if the move is valid
     */
    private boolean doMove(int countDownFrom, int yDirection, int xDirection) {
        boolean moved = false;
        int target = 2048;

        /**
         * traverse through all the tiles on the board
         */
        for (int i = 0; i < complexity * complexity; i++) {
            int j = Math.abs(countDownFrom - i);
            int r = j / complexity;
            int c = j % complexity;

            if (board.getTile(r, c).getNum() == 0)
                continue;

            int nextR = r + xDirection;
            int nextC = c + yDirection;

            /**
             * check if the move is available
             */
            while (nextR >= 0 && nextR < complexity && nextC >= 0 && nextC < complexity) {
                Tile next = board.getTile(nextR, nextC);
                Tile curr = board.getTile(r, c);
                if (next.getNum() == 0) {
                    if (moveAvailable)
                        return true;

                    next.setNum(curr.getNum());
                    curr.setNum(0);
                    r = nextR;
                    c = nextC;
                    nextR += xDirection;
                    nextC += yDirection;
                    moved = true;

                } else if (next.equals(curr) && !next.getMergedState()) {
                    if (moveAvailable)
                        return true;

                    int value = next.merge(curr);

                    if (value > highestTile)
                        highestTile = value;
                    score += value;
                    board.getTile(r, c).setNum(0);
                    moved = true;
                    break;
                } else
                    break;
            }
        }

        if (moved && highestTile < target) {
            board.clearMerged();
            board.addRandomTile();
            addToUndoList();
            setChanged();
            notifyObservers();
        }

        return moved;
    }

    @Override
    public void notifyScoreBoard() {
        setChanged();
        LinkedList info = new LinkedList<>();
        info.add(player);
        info.add(this.getSetting());
        notifyObservers(info);
    }

    /**
     * if move up is valid
     *
     * @return if move up is valid
     */
    boolean touchUp() {
        return doMove(0, -1, 0);
    }

    /**
     * if move down is valid
     *
     * @return if move up is valid
     */
    boolean touchDown() {
        return doMove(complexity * complexity - 1,
                1, 0);
    }

    /**
     * if move left is valid
     *
     * @return if move up is valid
     */
    boolean touchLeft() {
        return doMove(0, 0, -1);
    }

    /**
     * if move right is valid
     *
     * @return if move up is valid
     */
    boolean touchRight() {
        return doMove(complexity * complexity - 1,
                0, 1);
    }


    /**
     * overall the move is available from four directions
     *
     * @return overall the move is available from four directions
     */
    boolean movesAvailable() {
        moveAvailable = true;
        boolean hasMoves = touchUp() || touchDown() || touchLeft() || touchRight();
        moveAvailable = false;
        return hasMoves;
    }


}
