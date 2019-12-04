package fall2018.csc207_project.SlidingTileGame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.LocalGameCenter;
import fall2018.csc207_project.GameCenter.ScoreBoard;
import fall2018.csc207_project.Interfaces.GameActivityInterface;
import fall2018.csc207_project.R;
import fall2018.csc207_project.UI.ScoreBoardActivity;
import fall2018.csc207_project.UI.StartingActivity;

/**
 * the game activity of sliding tiles game
 */

public class GameActivity extends AppCompatActivity implements GameActivityInterface, Observer {

    /**
     * The board manager.
     */
    private SlidingTileGame slidingTileGame;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Constants for swiping directions. Should be an enum, probably.
     */
    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;

    /**
     * the width and the height of the column
     */
    private static int columnWidth, columnHeight;

    /**
     * the global center passed from the last activity
     */
    private GlobalCenter globalCenter;


    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        LocalGameCenter localGameCenter = globalCenter.getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());

        slidingTileGame = (SlidingTileGame) localGameCenter.getCurGame();
        createTileButtons(this);
        ScoreBoard scoreBoard = (SlidingTileScoreBoard) globalCenter.getScoreBoards().get(SlidingTileGame.GAMENAME);
        slidingTileGame.setPlayer(globalCenter.getCurrentPlayer().getUsername());
        slidingTileGame.addObserver(scoreBoard);

        setContentView(R.layout.sliding_tile_activity);

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(slidingTileGame.getBoard().getNumCols());
        gridView.setGame(slidingTileGame);
        slidingTileGame.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / slidingTileGame.getBoard().getNumCols();
                        columnHeight = displayHeight / slidingTileGame.getBoard().getNumRows();

                        display();
                    }
                });

        addUndoButtonListener();

    }

    /**
     * listen to the undo button
     */
    public void addUndoButtonListener() {
        Button button = findViewById(R.id.undo_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingTileGame.getUndoListSize() == 0)
                    gridView.getMController().toastNoMoreUndo(getApplicationContext());
                else
                    gridView.getMController().undo();
            }
        });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = slidingTileGame.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != slidingTileGame.getBoard().getNumRows(); row++) {
            for (int col = 0; col != slidingTileGame.getBoard().getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = slidingTileGame.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / slidingTileGame.getBoard().getNumRows();
            int col = nextPos % slidingTileGame.getBoard().getNumCols();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }

    @Override
    public void update(Observable o, Object arg) {
        display();

        autoSave();
    }

    /**
     * auto-save the game data without press save button
     */

    private void autoSave() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(Calendar.getInstance().getTime());
        globalCenter.getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername()).autoSave(timeStamp);
    }

    /**
     * if the back button is pressed,  return the last interface.
     */
    public void onBackPressed() {
        Intent tmp = new Intent(this, StartingActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    @Override
    public void switchToScoreBoard() {
        Intent tmp = new Intent(this, ScoreBoardActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }
}
