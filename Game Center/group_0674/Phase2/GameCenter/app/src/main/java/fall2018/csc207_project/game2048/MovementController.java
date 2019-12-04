package fall2018.csc207_project.game2048;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


import fall2018.csc207_project.Interfaces.Game;
import fall2018.csc207_project.Interfaces.Controller;


/**
 * the controller of the game data model, connect between the view and the game data model
 */
public class MovementController implements Controller {

    /**
     * the data model of the game
     */
    private Game2048 game;

    /**
     * upwards movement index
     */
    private static final int UP = 1;

    /**
     * downwards movement index
     */
    private static final int DOWN = 2;

    /**
     * leftwards movement index
     */
    private static final int LEFT = 3;

    /**
     * rightwards movement index
     */
    private static final int RIGHT = 4;

    /**
     * target to win the game
     */
    private static final int TARGET = 2048;


    /**
     * construct the move controller
     *
     * @param game
     */
    MovementController(Game2048 game) {
        this.game = game;
    }

    /**
     * return the game from the movement controller
     *
     * @return the game from the movement controller
     */
    public Game2048 getGame() {
        return this.game;
    }

    /**
     * process each movement from the data model
     * and give feedback to the game activity
     *
     * @param context   game activity
     * @param direction direction of movement
     * @return if the movement is realizable
     */
    boolean processMovement(Context context, int direction) {
        boolean hasMoved = false;
        // check the direction by checking the index of the movement
        if (direction == UP) {
            hasMoved = game.touchUp();
        } else if (direction == DOWN) {
            hasMoved = game.touchDown();
        } else if (direction == LEFT) {
            hasMoved = game.touchLeft();
        } else if (direction == RIGHT) {
            hasMoved = game.touchRight();
        }

        if (!hasMoved) {
            if (!game.movesAvailable()) {
                String message = "GAME OVER!";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                game.notifyScoreBoard();
                endGame(context, message);
            }
        }

        boolean has2048 = game.getHighestTile() >= TARGET;

        if (has2048) {
            String message = "YOU WIN!";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            game.notifyScoreBoard();
            endGame(context, message);
        }
        return hasMoved;
    }

    /**
     * set the game data model
     *
     * @param game the game will be set
     */
    public void setGame(Game game) {
        this.game = (Game2048) game;
    }

    /**
     * perform undo method by the movement controller
     */
    void undo() {
        game.undo();
    }

    /**
     * perform restart method by the movement controller
     */
    public void restart() {
        game.restart();
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