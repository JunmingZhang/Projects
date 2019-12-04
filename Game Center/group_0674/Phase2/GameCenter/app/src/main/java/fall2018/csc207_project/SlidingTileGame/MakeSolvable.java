package fall2018.csc207_project.SlidingTileGame;

import java.util.ArrayList;

/**
 * An converter that can convert a random board into a solvable board
 */

class MakeSolvable {

    /**
     * the row of the blank tile.
     */
    private int blankTileRow;

    /**
     * the column of the blank tile.
     */
    private int blankTileCol;

    /**
     * complexity of the game or the size of the board.
     */
    private int complexity;

    /**
     * the inversion of the board.
     */
    private int inversion;

    /**
     * the board taken in
     */
    private Board board;

    /**
     * whether the board can be solved
     */
    private boolean solved;

    /**
     * the list of non-blank tiles' id in order
     */
    private ArrayList<Integer> tilesId = new ArrayList<>();

    /**
     * collect non-blank tiles' Id in a ArrayList in order and
     * find the col and row of blank tile.
     */
    private void recordData() {
        for (int row = 0; row != complexity; row++) {
            for (int col = 0; col != complexity; col++) {
                if (board.getTile(row, col).getId() == complexity * complexity) {
                    blankTileCol = col;
                    blankTileRow = row;
                } else {
                    tilesId.add(board.getTile(row, col).getId());
                }
            }
        }
    }

    /**
     * calculate the number of inversions of the board
     */
    private void findInversion() {
        inversion = 0;
        for (int i = 0; i < tilesId.size(); i++) {
            for (int j = i + 1; j < tilesId.size(); j++) {
                if (tilesId.get(i) > tilesId.get(j)) {
                    inversion += 1;
                }
            }
        }
    }

    private void judgeSolved() {

        if (complexity % 2 == 1) {
            solved = inversion % 2 == 0;
        } else {
            solved = ((complexity - blankTileRow) % 2 == 1) == (inversion % 2 == 0);
        }
    }

    /**
     * swap last two non-blank tiles if the board is unsolvable.
     */
    private void MakeBoardSolvable() {
        if (!solved) {
            if (blankTileRow == complexity - 1) {
                if (blankTileCol == complexity - 1) {
                    board.swapTiles(complexity - 1, complexity - 2,
                            complexity - 1, complexity - 3);
                } else if (blankTileCol == board.getNumCols() - 2) {
                    board.swapTiles(complexity - 1, complexity - 1,
                            complexity - 1, complexity - 3);
                }
            } else {
                board.swapTiles(complexity - 1, complexity - 1,
                        complexity - 1, complexity - 2);
            }

        }
    }

    /**
     * set the parameter board and collect relevant data from the board.
     *
     * @param board board taken in
     */

    void takeIn(Board board) {
        this.complexity = board.getNumCols();
        this.board = board;
        recordData();
        findInversion();
        judgeSolved();
    }

    /**
     * if the board is unsolvable , make it is and output the board.
     *
     * @return board
     */
    Board outputSolvableBoard() {
        MakeBoardSolvable();
        return board;
    }
}