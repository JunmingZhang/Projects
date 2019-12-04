package fall2018.csc207_project.SlidingTileGame;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import fall2018.csc207_project.Interfaces.Game;
import fall2018.csc207_project.Interfaces.Controller;

/**
 * the Controller of the Sliding tiles game
 */
public class MovementController implements Controller {

    /**
     * sliding tiles game that taken in
     */
    private SlidingTileGame slidingTileGame = null;

    /**
     * set the game
     *
     * @param game the game taken in
     */
    public void setGame(Game game) {
        this.slidingTileGame = (SlidingTileGame) game;
    }

    /**
     * According the input event from view, change the data model of the game.
     * Let tiles swap each other and toast message if necessary.
     *
     * @param context  current context
     * @param position the position that the player's finger touched
     * @param display  the boolean display
     */
    void processTapMovement(Context context, int position, boolean display) {
        if (slidingTileGame.isValidTap(position)) {
            slidingTileGame.touchMove(position);
            if (slidingTileGame.puzzleSolved()) {
                String message = "YOU WIN!";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                slidingTileGame.notifyScoreBoard();
                endGame(context, message);
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * change the data model when the player wants to undo.
     */
    void undo() {
        slidingTileGame.undo();
    }

    /**
     * toast the message "No more undo"
     *
     * @param context current context
     */

    void toastNoMoreUndo(Context context) {
        Toast.makeText(context, "No more undo!", Toast.LENGTH_SHORT).show();
    }

    /**
     * end the game after playing it by popping up a dialog
     *
     * @param context game activity
     * @param message message for making toast
     */
    public void endGame(final Context context, final String message) {
        GameActivity gameActivity = (GameActivity) context;
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final GameActivity gameActivity = (GameActivity) context;
                if (!gameActivity.isFinishing()) {
                    new AlertDialog.Builder(context)
                            .setTitle(message)
                            .setMessage("You can check the scoreboard.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.
                                    OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    gameActivity.switchToScoreBoard();
                                }
                            }).show();
                }
            }
        });
    }
}
