package fall2018.csc207_project.Interfaces;

import android.content.Context;

/**
 * interface of controller
 */
public interface Controller {

    /**
     * @param context game activity
     * @param message message of content
     */
    void endGame(final Context context, final String message);

    /**
     * set the game
     *
     * @param game the game will be set
     */
    void setGame(Game game);


}
