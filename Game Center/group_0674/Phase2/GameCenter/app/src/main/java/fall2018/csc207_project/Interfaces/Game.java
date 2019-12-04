package fall2018.csc207_project.Interfaces;

import java.util.List;

/**
 * interface of game
 */
public interface Game {

    /**
     * get the setting information
     *
     * @return the setting list
     */
    List<Object> getSetting();

    /**
     * notify the ScoreBoard
     */
    void notifyScoreBoard();

}
