package fall2018.csc207_project.MineSweeper;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import fall2018.csc207_project.GameCenter.ScoreBoard;

/**
 * scoreboard of minesweeper
 */
public class MineSweeperScoreBoard extends ScoreBoard implements Serializable {

    /**
     * serial number of the scoreboard
     */
    private static final long serialVersionUID = 8271998400642L;

    /**
     * starting of the mark, can be higher or lower
     */
    private final int BASECOUNT = 1000;

    @Override
    public int calculateScore(List<Object> setting) {
        if ((boolean) setting.get(5)) {
            Board board = new Board((Board) (setting.get(0)));
            //I am so kind that I give you one mark for bonus
            return (int) ((BASECOUNT - (Integer) (setting.get(1))) *
                    (1 + (float) board.getBombNum() / 20)) + 1;
        } else {
            return 0;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof LinkedList) {
            LinkedList<Object> temp = (LinkedList<Object>) arg;
            int score = calculateScore((List<Object>) (temp.getLast()));
            this.addNewScore((String) temp.getFirst(), score);
        }
    }


}
