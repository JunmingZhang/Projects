package fall2018.csc207_project.game2048;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import fall2018.csc207_project.GameCenter.ScoreBoard;

/**
 * the scoreboard of the game 2048
 */
public class Game2048ScoreBoard extends ScoreBoard implements Serializable {
    /**
     * serial number of the scoreboard of 2048
     */
    private static final long serialVersionUID = 284675295723L;

    /**
     * the mark index in the setting
     */
    private final int SCOREINDEX = 3;

    /**
     * initialize the socreboard
     */
    public Game2048ScoreBoard() {
        super();
    }

    @Override
    public int calculateScore(List<Object> setting) {
        return (int) setting.get(SCOREINDEX);
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
