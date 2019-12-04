package fall2018.csc207_project.MineSweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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
 * activity performs game
 * <p>
 * timer adapted from https://github.com/lany192/Minesweeper.git
 * resource of images for tiles from https://github.com/marcellelek/Minesweeper
 */
public class GameActivity extends AppCompatActivity implements GameActivityInterface, Observer {

    /**
     * global centre of the game
     */
    private GlobalCenter globalCenter;

    /**
     * local centre of each user
     */
    private LocalGameCenter localCenter;

    /**
     * view of the game
     */
    private BoardView gameView;

    /**
     * text view of bombs left in the game
     */
    TextView mineCountText;

    /**
     * text view of time left in the game
     */
    TextView timerCountText;

    /**
     * text view of help chance left in the game
     */
    TextView helpLeft;

    /**
     * timer of the game
     */
    private Handler timer = new Handler();

    /**
     * data model of the game
     */
    private MineSweeperGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_mine_sweeper);

        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        localCenter = globalCenter.getLocalGameCenter(globalCenter.
                getCurrentPlayer().getUsername());
        ScoreBoard scoreBoard = (MineSweeperScoreBoard) globalCenter.getScoreBoards().
                get(MineSweeperGame.GAMENAME);

        // find the game model from the local centre
        // which realize game autosave and change of complexity
        game = (MineSweeperGame) localCenter.getCurGame();
        game.setPlayer(globalCenter.getCurrentPlayer().getUsername());

        //initialize game view
        gameView = findViewById(R.id.grid);
        gameView.setGame(game);
        gameView.setTableImage();

        game.addObserver(this);
        game.addObserver(scoreBoard);

        //set text view of the game
        this.mineCountText = findViewById(R.id.bomb);
        this.timerCountText = findViewById(R.id.time);
        this.helpLeft = findViewById(R.id.help_left);

        timerCountText.setText(String.valueOf(game.getSecondsPassed()));
        mineCountText.setText(String.valueOf(game.getFlagsLeft()));
        String helpAvailable = "(" + String.valueOf(game.getHelp() ? 1 : 0) + ")";
        helpLeft.setText(helpAvailable);

        addHelpButtonListener();

        if (game.getSecondsPassed() < 999) {
            startTimer();
        }
    }

    /**
     * start timing
     */
    public void startTimer() {

        timer.removeCallbacks(updateTimeElasped);
        // tell timer to run call back after 1 second
        timer.postDelayed(updateTimeElasped, 1000);

    }

    /**
     * stop timing
     */
    public void stopTimer() {
        // disable call backs
        timer.removeCallbacks(updateTimeElasped);
    }

    // timer call back when timer is ticked
    private Runnable updateTimeElasped = new Runnable() {
        public void run() {
            long currentMilliseconds = System.currentTimeMillis();
            int secondsPassed = game.getSecondsPassed();
            ++secondsPassed;

            gameView.getMController().changeTime(secondsPassed);

            timerCountText.setText(String.valueOf(secondsPassed));

            // add notification
            timer.postAtTime(this, currentMilliseconds);
            // notify to call back after 1 seconds
            // basically to remain in the timer loop
            timer.postDelayed(updateTimeElasped, 1000);

            if (secondsPassed >= 999) {
                stopTimer();
            }
        }
    };

    @Override
    public void update(Observable o, Object arg) {
        //change the image of the tile if flipped
        if (arg instanceof Integer)
            gameView.setTileImage((int) arg);
        if (game.getGameOver()) {
            stopTimer();
        }
        //reset flags number after each move
        mineCountText.setText(String.valueOf(game.getFlagsLeft()));
        autoSave();
    }

    /**
     * auto save the game model by time played
     */
    private void autoSave() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(Calendar.getInstance().getTime());
        globalCenter.getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername()).
                autoSave(timeStamp);
    }

    /**
     * call help method of the game model
     * only one help chance
     */
    public void addHelpButtonListener() {
        Button helpButton = findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // invalidate the button if game end
                if (!game.getGameOver()) {
                    if (game.getHelp()) {
                        // change help left to zero on the screen
                        helpLeft.setText("(0)");
                        gameView.getMController().helpPressed();
                    } else {
                        gameView.getMController().toastHelpUsedUp(getApplicationContext());
                    }
                }
            }
        });
    }

    /**
     * connect the game with global center
     */
    public void onBackPressed() {
        Intent tmp = new Intent(this, StartingActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }

    @Override
    public void switchToScoreBoard() {
        Intent tmp = new Intent(this, ScoreBoardActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }
}
