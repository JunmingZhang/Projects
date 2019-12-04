package fall2018.csc207_project.game2048;

import android.content.Intent;
import android.os.Bundle;
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
import fall2018.csc207_project.Interfaces.GameActivityInterface;
import fall2018.csc207_project.R;
import fall2018.csc207_project.UI.ScoreBoardActivity;
import fall2018.csc207_project.UI.StartingActivity;

/**
 * the activity to perform the game
 */
public class GameActivity extends AppCompatActivity implements GameActivityInterface, Observer {

    /**
     * the launch center
     */
    private GlobalCenter globalCenter;

    /**
     * the view of the game activity
     */
    private BoardView gameView;

    /**
     * the data model of this game
     */
    Game2048 game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_2048_activity);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        LocalGameCenter localGameCenter = globalCenter.getLocalGameCenter(globalCenter.
                getCurrentPlayer().getUsername());
        gameView = findViewById(R.id.gameView);
        game = (Game2048) localGameCenter.getCurGame();
        game.setPlayer(globalCenter.getCurrentPlayer().getUsername());
        gameView.setGame(game);
        game.addObserver(this);
        game.addObserver((Game2048ScoreBoard) (globalCenter.getScoreBoards().
                get(Game2048.GAMENAME)));

        setUndoButtonListener();
        setRestartButtonListener();
        initTextView();
    }

    /**
     * autosave the game with date and time
     */
    private void autoSave() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(Calendar.getInstance().getTime());
        globalCenter.getLocalGameCenter(globalCenter.getCurrentPlayer().
                getUsername()).autoSave(timeStamp);
    }


    /**
     * activate the back button on android screen
     * on the left bottom
     */
    public void onBackPressed() {
        Intent tmp = new Intent(this, StartingActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * initialize the view of the text on the screen, like score, highest mark and undo step
     */
    private void initTextView() {
        Game2048ScoreBoard scoreBoard = (Game2048ScoreBoard) (globalCenter.getScoreBoards().
                get(Game2048.GAMENAME));
        TextView score = findViewById(R.id.current_score);
        TextView highest = findViewById(R.id.highest_score);
        TextView undoStepLeft = findViewById(R.id.undo_step_left_2048);
        score.setText("Score\n" + game.getScore());
        highest.setText("Best\n" + scoreBoard.getPlayerTopScore(globalCenter.getCurrentPlayer().
                getUsername()));
        undoStepLeft.setText("(" + game.getUndoStep() + ")");

    }

    /**
     * activate undo button
     * call the undo method on the game
     */
    public void setUndoButtonListener() {
        Button button = findViewById(R.id.undo_button_2048);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call undo method of the game
                gameView.getMController().undo();
            }
        });

    }

    /**
     * activate the restart button
     * call the restart method on the game data model
     */
    public void setRestartButtonListener() {
        Button button = findViewById(R.id.restart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call restart method of the game
                gameView.getMController().restart();
            }
        });

    }

    @Override
    public void update(Observable o, Object arg) {
        TextView score = findViewById(R.id.current_score);
        TextView undoStepLeft = findViewById(R.id.undo_step_left_2048);
        score.setText("Score\n" + game.getScore());
        undoStepLeft.setText("(" + game.getUndoStep() + ")");
        gameView.updateDisplay();
        autoSave();
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
