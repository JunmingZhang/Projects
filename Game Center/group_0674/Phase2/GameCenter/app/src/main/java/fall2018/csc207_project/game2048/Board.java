package fall2018.csc207_project.game2048;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * board of game 2048
 */
public class Board implements Serializable {

    /**
     * serial number of the board of 2048
     */
    private static final long serialVersionUID = 99472526420521L;

    /**
     * size of the board
     * defaulted 4
     */
    private int boardSize;

    /**
     * tiles in the board
     */
    private Tile[][] tileTable;

    /**
     * a list of empty spots on the tile
     */
    private List<Integer> blankTileList = new LinkedList<>();


    /**
     * construct the board by input complexity
     *
     * @param complexity the size of the board
     */
    Board(int complexity) {
        this.boardSize = complexity;
        tileTable = new Tile[boardSize][boardSize];
        ;
        addTiles();
        addRandomTile();
        addRandomTile();
    }

    /**
     * construct the board by an existing board
     *
     * @param board existing board
     */
    Board(Board board) {
        this.boardSize = board.getBoardSize();
        tileTable = new Tile[boardSize][boardSize];
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                tileTable[x][y] = new Tile(board.getTile(x, y).getNum());
            }
        }
        blankTileList = new LinkedList<>();
        //add tile numbers of the old board to new board
        blankTileList.addAll(board.getBlankTileList());
    }

    /**
     * return the linked list of numbers of all all tiles
     *
     * @return the linked list of numbers of all all tiles
     */
    private List<Integer> getBlankTileList() {
        return this.blankTileList;
    }

    /**
     * get the tile by given index
     *
     * @param x horizontal coordinator
     * @param y vertical coordinator
     * @return the tile by given horizontal and vertical indices
     */
    Tile getTile(int x, int y) {
        return tileTable[x][y];
    }

    /**
     * get the board size
     *
     * @return the board size
     */
    int getBoardSize() {
        return this.boardSize;
    }

    public void setBoardSize(int size) {
        this.boardSize = size;
    }

    /**
     * set merge state of all tiles to false
     */
    void clearMerged() {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                tileTable[x][y].setMergedState();
            }
        }
    }

    /**
     * set values of tiles on the board by the given list
     *
     * @param list list of numbers
     */
    void syncBoard(List<Integer> list) {
        int count = 0;
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                tileTable[x][y].setNum(list.get(count));
                count += 1;
            }
        }
    }


    /**
     * randomly add a tile of 2 or 4 to the board
     */
    void addRandomTile() {
        blankTileList.clear();

        //check empty spots
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (tileTable[i / boardSize][i % boardSize].getNum() <= 0)
                blankTileList.add(i);
        }
        //add a tile randomly to the board
        int randomIndex = (int) (Math.random() * blankTileList.size());
        int point = blankTileList.get(randomIndex);
        tileTable[point / boardSize][point % boardSize].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    /**
     * add tiles to the board
     */
    private void addTiles() {
        Tile tile;
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                tile = new Tile(0);
                tileTable[x][y] = tile;
            }
        }
    }
}