package fall2018.csc207_project.SlidingTileGame;


import android.content.pm.LabeledIntent;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import fall2018.csc207_project.GameCenter.ScoreBoard;

/**
 * The SlidingTileScoreBoard
 */

public class SlidingTileScoreBoard extends ScoreBoard implements Serializable {
    /**
     * unique id for serialization
     */
    private static final long serialVersionUID = 8271927480642L;

    /**
     * constructor that same as its parent class "ScoreBoard"
     */
    public SlidingTileScoreBoard() {
        super();
    }

    /**
     * calculate scores based on setting
     *
     * @param setting the boardSize at index 0 and moves at index 1 of slidingTileGame
     * @return score based on setting
     * Precondition: boardSize need to be at index 0 and number of moves took should at index 1
     */
    @Override
    public int calculateScore(List<Object> setting) {
        int maxScore = 1000;
        Integer boardSize = (Integer) setting.get(0);
        Integer moves = (Integer) setting.get(2);
        int roughScore = 0;
        if (boardSize.equals(5)) {
            roughScore = maxScore - moves;
        } else if (boardSize.equals(4)) {
            roughScore = maxScore - 3 * moves;
        } else if (boardSize.equals(3)) {
            roughScore = maxScore - 5 * moves;
        }
        if (roughScore > 300) {
            return roughScore;
        } else {
            return 300;
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