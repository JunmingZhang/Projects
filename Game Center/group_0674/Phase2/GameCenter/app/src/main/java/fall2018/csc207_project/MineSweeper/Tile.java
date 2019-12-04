package fall2018.csc207_project.MineSweeper;

import java.io.Serializable;

import fall2018.csc207_project.R;


/**
 * basic data model, each tile of the game
 */
class Tile implements Serializable {

    /**
     * serial number of the tile
     */
    private static final long serialVersionUID = 24575L;

    /**
     * number on the tile
     */
    private int num;

    /**
     * if the tile has been flipped
     */
    private boolean flipped = false;

    /**
     * if the tile has been labelled
     */
    private boolean labeled = false;

    /**
     * if the bomb tile exploded
     */
    private boolean boom = false;

    /**
     * construct the tile by its number
     *
     * @param num number on the tile
     */
    Tile(int num) {
        this.num = num;
    }

    /**
     * construct the tile by an existing tile
     *
     * @param tile an existing tile
     */
    Tile(Tile tile) {
        this.num = tile.getNum();
        this.flipped = tile.getFliped();
        this.labeled = tile.getLabeled();
        this.boom = tile.getBoom();
    }

    /**
     * get if the tile has been flipped
     *
     * @return if the tile has been flipped
     */
    private boolean getFliped() {
        return flipped;
    }

    /**
     * get if the tile has been labeled to a flag
     *
     * @return if the tile has been labeled to a flag
     */
    private boolean getLabeled() {
        return labeled;
    }

    /**
     * get if the tile has been exploded if it is a bomb
     *
     * @return if the tile has been exploded if it is a bomb
     */
    private boolean getBoom() {
        return boom;
    }

    /**
     * set the number of the tile
     *
     * @param num number of the tile
     */
    void setNum(int num) {
        this.num = num;
    }

    /**
     * set the if the tile has been flagged
     *
     * @param labeled the tile has been flagged
     */
    void setLabeled(boolean labeled) {
        this.labeled = labeled;
    }

    /**
     * set tile flipped to be true
     */
    void setFliped() {
        this.flipped = true;
    }

    /**
     * set the bomb exploded to be true
     */
    void setBoom() {
        this.boom = true;
    }

    /**
     * get the number labeled on the tile
     *
     * @return the number labeled on the tile
     */
    int getNum() {
        return num;
    }

    /**
     * get if the tile has been labelled as a flag
     *
     * @return if the tile has been labelled
     */
    boolean isLabeled() {
        return labeled;
    }

    /**
     * get if the tile has been flipped
     *
     * @return if the tile has been flipped
     */
    boolean isFlipped() {
        return flipped;
    }

    /**
     * get the corresponding image of the tile
     *
     * @return the corresponding image of the tile
     */
    int getTileImage() {
        int ret;
        if (!isFlipped()) {
            if (isLabeled()) {
                ret = R.drawable.flag;
            } else {
                ret = R.drawable.button;
            }
        } else {
            if (num == 0) {
                ret = R.drawable.number_0;
            } else if (num == 1) {
                ret = R.drawable.number_1;
            } else if (num == 2) {
                ret = R.drawable.number_2;
            } else if (num == 3) {
                ret = R.drawable.number_3;
            } else if (num == 4) {
                ret = R.drawable.number_4;
            } else if (num == 5) {
                ret = R.drawable.number_5;
            } else if (num == 6) {
                ret = R.drawable.number_6;
            } else if (num == 7) {
                ret = R.drawable.number_7;
            } else if (num == 8) {
                ret = R.drawable.number_8;
            }
            // num >= 10
            else {
                // if this is a bomb and exploded
                if (boom) {
                    ret = R.drawable.bomb_exploded;
                } else {
                    ret = R.drawable.bomb_normal;
                }
            }
        }
        return ret;
    }
}
