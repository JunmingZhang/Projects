package fall2018.csc207_project.SlidingTileGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import fall2018.csc207_project.Interfaces.Game;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */

public class SlidingTileGame extends Observable implements Game, Serializable {

    /**
     * the unique id for serialization
     */
    private static final long serialVersionUID = 8888L;

    /**
     * the game name
     */
    public static final String GAMENAME = "slidingTileGame";

    /**
     * player's username
     */
    private String player;
    /**
     * The board being managed.
     */
    private Board board;

    /**
     * The number of operations of player.
     */
    private int numSteps = 0;

    /**
     * limit for undo step.
     */
    private int undoStep = 3;

    /**
     * list for previous board.
     */
    private LinkedList<int[]> undoList = new LinkedList<>();

    /**
     * the complexity of the game
     */

    private int complexity = 3;

    /**
     * Manage a board that has been pre-populated.
     *
     * @param settings the settings of the game.
     *                 Preconditin: the first element of the settings should be current board, the second one should
     *                 be numSteps, the third one should be undoStep, and the last one should be
     *                 undoList containing previous board. settings should contain only 4 elements.
     */
    public SlidingTileGame(List<Object> settings) {
        if (settings.size() == 1)
            initWithComplexity((int) settings.get(0));
        else {
            this.complexity = (int) settings.get(0);
            this.board = (Board) settings.get(1);
            this.numSteps = (int) settings.get(2);
            this.undoStep = (int) settings.get(3);
            this.undoList = (LinkedList<int[]>) settings.get(4);
        }
    }


    /**
     * initial the sliding tiles game , given the complexity
     *
     * @param complexity
     */
    private void initWithComplexity(int complexity) {
        this.complexity = complexity;
        List<Tile> tiles = new ArrayList<>();
        // number of tiles. The first element in settings should be a int that represents number
        // of rows/columns.
        final int numTiles = complexity * complexity;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            // if tileNum is not numTiles - 1, we assign a image with correct number to it, and a
            // correct id to it
            if (tileNum != numTiles - 1) {
                tiles.add(new Tile(tileNum));
            } else {
                // if tileNum is numTiles -1, we assign blank image to it
                tiles.add(new Tile(-1));
                // we then set its id to numTiles
                tiles.get(numTiles - 1).setId(numTiles);
            }
        }
        Collections.shuffle(tiles);

        this.board = new Board(tiles, this.complexity);

        MakeSolvable converter = new MakeSolvable();
        converter.takeIn(board);
        this.board = converter.outputSolvableBoard();
    }

    /**
     * get the size of undoList
     *
     * @return the size of the undoList
     */
    int getUndoListSize() {
        return this.undoList.size();
    }

    /**
     * @return the setting information of the SlidingTileGame in an ArrayList, where the first
     * element is the current board, the second one is the number of steps the user used, the third
     * one is the preset undoStep of the game, and the last one is the list containing previous
     * boards for undo.
     */
    @Override
    public List<Object> getSetting() {
        LinkedList<Object> result = new LinkedList<>();
        result.add(this.complexity);
        result.add(this.board);
        result.add(this.numSteps);
        result.add(this.undoStep);
        result.add(this.undoList);
        return result;
    }

    void setPlayer(String name) {
        this.player = name;
    }

    /**
     * Set the undoStep
     *
     * @param undoStep number of customized undoStep
     */
    void setUndoStep(int undoStep) {
        this.undoStep = undoStep;
    }

    /**
     * add board to undoList if the size of undoList is less than undoStep, otherwise, remove the
     * first one and add the board to it.
     *
     * @param positions: first/second number: row/col of first swapped tile;
     *                   third/fourth number: row/col of second swapped tile;
     */
    private void addToUndoList(int[] positions) {
        if (this.undoList.size() < this.undoStep) {
            this.undoList.add(positions);
        } else {
            this.undoList.removeFirst();
            this.undoList.add(positions);
        }

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
     * If the undoList is not empty, SlidingTileGame's board is reset to the previous one.
     */
    void undo() {
        if (this.undoList.size() != 0) {
            int[] positions = this.undoList.removeLast();
            this.board.swapTiles(positions[0], positions[1], positions[2], positions[3]);
            this.numSteps += 1;
        }
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        // the solution for whether the puzzle is solved.
        boolean solved = true;
        // default id.
        int previous = board.getTile(0, 0).getId() - 1;

        for (Tile tile : board) {
            solved = previous + 1 == tile.getId() && solved;
            previous = tile.getId();
        }
        if (solved)
            notifyObservers("slidingTileGame");
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / this.board.numCols;
        int col = position % this.board.numCols;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == this.board.numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == this.board.numCols - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        // Which row for the current tapping Tile.
        int row = position / this.board.numRows;
        // Which column for the current tapping Tile.
        int col = position % this.board.numCols;
        // The blank id for the blank Tile.
        int blankId = this.board.numTiles();

        if (isValidTap(position)) {
            // Determine whether the row or column can reach one more position.
            int rMax, cMax;
            rMax = row == this.board.numRows - 1 ? 1 : 2;
            cMax = col == this.board.numCols - 1 ? 1 : 2;
            for (int r = row == 0 ? 0 : -1; r < rMax; r++) {
                for (int c = col == 0 ? 0 : -1; c < cMax; c++) {
                    if (r - c != 0 && r + c != 0
                            && this.board.getTile(row + r, col + c).getId() == blankId) {
                        this.addToUndoList(new int[]{row, col, row + r, col + c});//add the current state to undoList.
                        this.board.swapTiles(row, col, row + r, col + c);
                        this.numSteps += 1;
                    }
                }
            }
        }
    }
}
