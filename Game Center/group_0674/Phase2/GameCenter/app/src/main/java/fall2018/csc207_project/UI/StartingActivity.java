package fall2018.csc207_project.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.LocalGameCenter;
import fall2018.csc207_project.MineSweeper.MineSweeperGame;
import fall2018.csc207_project.MineSweeper.SplashActivity;
import fall2018.csc207_project.R;
import fall2018.csc207_project.SlidingTileGame.ComplexityActivity;
import fall2018.csc207_project.SlidingTileGame.SlidingTileGame;
import fall2018.csc207_project.game2048.Game2048;

/**
 * The starting activity of all the games.
 */
public class StartingActivity extends AppCompatActivity {

    private GlobalCenter globalCenter;
    private LocalGameCenter localCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        localCenter = globalCenter.
                getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());
        setContentView(fall2018.csc207_project.R.layout.game_page);
        initBand();

        addNewGameButtonListener();
        addLoadGameButtonListener();
        addSaveGameButtonListener();
        addMyScoreButtonListener();
        addGlobalScoreButtonListener();
    }

    /**
     * Initialize the message at the top of the activity.
     */
    private void initBand() {
        TextView band = findViewById(R.id.GameText);
        String gameName = localCenter.getCurGameName();
        if(gameName.equals(SlidingTileGame.GAMENAME))
            band.setText(R.string.sliding_tile_band);
        else if(gameName.equals(Game2048.GAMENAME))
            band.setText(R.string.game_2048_band);
        else if(gameName.equals(MineSweeperGame.GAMENAME))
            band.setText(R.string.mine_sweeper_band);

    }

    /**
     * Add a new game button listener and link to its button.
     */
    public void addNewGameButtonListener() {

        Button button = findViewById(fall2018.csc207_project.R.id.new_game_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(localCenter.getCurGameName().equals(SlidingTileGame.GAMENAME))
                    switchToSlidingComplexity();
                else if(localCenter.getCurGameName().equals(Game2048.GAMENAME))
                    switchTo2048();
                else if(localCenter.getCurGameName().equals(MineSweeperGame.GAMENAME))
                    switchToMineSweeperSplash();
            }
        });
    }

    /**
     * Add a load game button listener and link to its button.
     */
    public void addLoadGameButtonListener() {
        Button button = findViewById(fall2018.csc207_project.R.id.load_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGameSlot(true);
            }
        });
    }

    /**
     * Add a save game button listener and link to its button.
     */
    public void addSaveGameButtonListener() {
        Button button = findViewById(fall2018.csc207_project.R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGameSlot(false);
            }
        });
    }

    /**
     * Add a my score button listener and lin to its button.
     */
    public void addMyScoreButtonListener() {
        Button button = findViewById(R.id.my_score_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreBoard(true);
            }
        });
    }

    /**
     * Add a global score button listener and link to its button.
     */
    public void addGlobalScoreButtonListener() {
        Button button = findViewById(R.id.global_score_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreBoard(false);
            }
        });
    }

    /**
     * Switch to scoreboard activity.
     *
     * @param state
     */
    public void switchToScoreBoard(Boolean state) {
        Intent tmp = new Intent(this, ScoreBoardActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        tmp.putExtra("perPlayer?", state);
        startActivity(tmp);
        finish();
    }

    /**
     * Switch to sliding tile game complexity.
     */
    public void switchToSlidingComplexity() {
        Intent tmp = new Intent(this, ComplexityActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * Switch to game 2048 playing activity.
     */
    public void switchTo2048() {
        LinkedList<Object> settings = new LinkedList<>();
        settings.add(4);
        Game2048 game = (Game2048) localCenter.newGame(Game2048.GAMENAME, settings);
        Intent tmp = new Intent(this,
                fall2018.csc207_project.game2048.GameActivity.class);
        localCenter.setCurGame(game);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * Switch to minesweeper splash activity.
     */
    public void switchToMineSweeperSplash() {
        Intent tmp = new Intent(this, SplashActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * Switch to game slot page.
     *
     * @param saveOrLoad
     */
    public void switchToGameSlot(boolean saveOrLoad) {
        Intent tmp = new Intent(this, LoadOrSaveGameActivity.class);
        tmp.putExtra("loadGame?", saveOrLoad);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent tmp = new Intent(this, LocalCenterActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }
}
