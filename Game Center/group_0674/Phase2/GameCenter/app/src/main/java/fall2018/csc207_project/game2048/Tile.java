package fall2018.csc207_project.game2048;


import java.io.Serializable;

/**
 * basic data model of 2048
 */
public class Tile implements Serializable {

    /**
     * serial number of the tile
     */
    private static final long serialVersionUID = 732521258574L;

    /**
     * number on each tile
     */
    private int num;

    /**
     * if the tile has been merged
     */
    private boolean mergedState;

    /**
     * construct the tile by number
     *
     * @param number the number on the tile
     */
    Tile(int number) {
        this.num = number;
        this.mergedState = false;
    }

    /**
     * set the number on the tile
     *
     * @param number number on the tile
     */
    void setNum(int number) {
        this.num = number;
    }

    /**
     * get the number on the tile
     *
     * @return the number on the tile
     */
    int getNum() {
        return this.num;
    }

    /**
     * set merge state to false
     */
    void setMergedState() {
        this.mergedState = false;
    }

    /**
     * get the merged state
     *
     * @return the merged state
     */
    boolean getMergedState() {
        return this.mergedState;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tile)
            return this.getNum() == ((Tile) o).getNum();
        else
            return super.equals(o);
    }

    /**
     * merge two tiles to one
     *
     * @param tile another
     * @return merged number
     */
    int merge(Tile tile) {
        //merge if two tiles are equal
        if (this.equals(tile)) {
            this.mergedState = true;
            this.setNum(num *= 2);
            return num;
        }
        return -1;
    }
}
