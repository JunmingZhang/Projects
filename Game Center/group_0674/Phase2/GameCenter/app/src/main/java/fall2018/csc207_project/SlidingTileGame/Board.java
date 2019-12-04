package fall2018.csc207_project.SlidingTileGame;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {
    private static final long serialVersionUID = 7777L;

    /**
     * The number of rows.
     */
    int numRows = 4;

    /**
     * The number of rows.
     */
    int numCols = 4;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == numRows * numCols
     *
     * @param tiles the tiles for the board
     */
    Board(List<Tile> tiles, int gridSize) {
        this.numCols = gridSize;
        this.numRows = gridSize;
        this.tiles = new Tile[this.numRows][this.numCols];
        Iterator<Tile> iter = tiles.iterator();
        for (int row = 0; row != this.numRows; row++) {
            for (int col = 0; col != this.numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    int numTiles() {
        return numRows * numCols;
    }

    /**
     * Return the number of row of the board, which is the same as numCols/numRows.
     *
     * @return the number of row of the board, which is the same as numCols/numRows.
     */
    int getNumRows() {
        return this.numRows;
    }

    /**
     * Return the number of row of the board, which is the same as numCols/numRows.
     *
     * @return the number of row of the board, which is the same as numCols/numRows.
     */
    int getNumCols() {
        return this.numCols;
    }


    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Return the Tiles
     *
     * @return Tiles
     */
    Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        // Temporary variable for storing a Tile.
        Tile temp;

        temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;

        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "BaseBoard{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * Returns an iterator for this BaseBoard.
     *
     * @return an iterator for this BaseBoard.
     */

    @Override
    public Iterator<Tile> iterator() {
        return new TileIterator();
    }

    /**
     * An Iterator for Tile class
     */
    private class TileIterator implements Iterator<Tile> {

        /**
         * The index of the next Tile to return.
         */
        private int current = 0;

        /**
         * Returns whether there is another Tile to return.
         *
         * @return whether here is another Tile to return.
         */
        @Override
        public boolean hasNext() {
            return current < numRows * numCols;
        }

        /**
         * Returns the next Tile.
         *
         * @return the next Tile.
         */
        @Override
        public Tile next() {
            // The Tile which are supposed to be return.
            Tile target;
            // The row and column position of the Tile.
            int row, col;

            row = current / numRows;
            col = current % numCols;
            try {
                target = tiles[row][col];
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
            current += 1;
            return target;
        }
    }
}
