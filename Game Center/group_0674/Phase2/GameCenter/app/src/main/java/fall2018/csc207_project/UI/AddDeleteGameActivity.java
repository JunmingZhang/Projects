package fall2018.csc207_project.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Set;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.LocalGameCenter;
import fall2018.csc207_project.MineSweeper.MineSweeperGame;
import fall2018.csc207_project.MineSweeper.MineSweeperScoreBoard;
import fall2018.csc207_project.R;
import fall2018.csc207_project.SlidingTileGame.SlidingTileGame;
import fall2018.csc207_project.SlidingTileGame.SlidingTileScoreBoard;
import fall2018.csc207_project.game2048.Game2048;
import fall2018.csc207_project.game2048.Game2048ScoreBoard;

/**
 * the activity of delete game
 */
public class AddDeleteGameActivity extends AppCompatActivity {

    /**
     * the Global Center
     * the local Game center
     * the state of game
     */
    private GlobalCenter globalCenter;
    private LocalGameCenter localCenter;
    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_delete_game);
        state = getIntent().getBooleanExtra("addGame?", true);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        localCenter = globalCenter.
                getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());

        initTextView();
        if(state)
            addState();
        else
            deleteState();

    }

    /**
     * init the testView
     */
    private void initTextView() {
        if (!state) {
            TextView addDeleteBand = findViewById(R.id.add_delete_band);
            TextView tapBand = findViewById(R.id.tap_band);

            addDeleteBand.setText(R.string.gamesToDelete);
            tapBand.setText(R.string.tapToDelete);
        }
    }


    /**
     * add the state to game
     */
    private void addState() {

        //the set of game in local center
        Set<String> gameSet = localCenter.getGames();

        if (gameSet.contains(SlidingTileGame.GAMENAME))
            findViewById(R.id.sliding_tile_button).setVisibility(View.GONE);
        else
            addSlidingTileGameListener();

        if (gameSet.contains(Game2048.GAMENAME))
            findViewById(R.id.game2048_button).setVisibility(View.GONE);
        else
            addGame2048Listener();

        if (gameSet.contains(MineSweeperGame.GAMENAME))
            findViewById(R.id.snake_button).setVisibility(View.GONE);
        else
            addMineSweeperListener();

    }

    /**
     * add the Listener of SlidingTile game
     */
    private void addSlidingTileGameListener() {
        final Button button = findViewById(R.id.sliding_tile_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localCenter.addGame(SlidingTileGame.GAMENAME);
                globalCenter.addScoreBoard(SlidingTileGame.GAMENAME, new SlidingTileScoreBoard());
                button.setVisibility(View.GONE);
            }
        });
    }

    /**
     * add the listener of game2048
     */
    private void addGame2048Listener() {
        final Button button = findViewById(R.id.game2048_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localCenter.addGame(Game2048.GAMENAME);
                globalCenter.addScoreBoard(Game2048.GAMENAME, new Game2048ScoreBoard());
                button.setVisibility(View.GONE);
            }
        });
    }

    /**
     * add the listener of MineSweeper
     */
    private void addMineSweeperListener() {
        final Button button = findViewById(R.id.snake_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localCenter.addGame(MineSweeperGame.GAMENAME);
                globalCenter.addScoreBoard(MineSweeperGame.GAMENAME, new MineSweeperScoreBoard());
                button.setVisibility(View.GONE);
            }
        });
    }

    /**
     * delete the state of game
     */
    private void deleteState() {
        Set<String> gameSet = localCenter.getGames();
        LinearLayout ll = findViewById(R.id.add_delete_game_button_layout);
        ll.removeAllViews();
        // set the game
        for (String s : gameSet) {
            final Button tmp = new Button(this);
            ll.addView(tmp);
            // Game Sliding Tile, 2048 and MineSweeper
            if (s.equals(SlidingTileGame.GAMENAME)) {
                tmp.setText(R.string.slidingTileGame);
                tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localCenter.removeGame(SlidingTileGame.GAMENAME);
                        tmp.setVisibility(View.GONE);
                    }
                });
            } else if (s.equals(Game2048.GAMENAME)) {
                tmp.setText(R.string.game2048);
                tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localCenter.removeGame(Game2048.GAMENAME);
                        tmp.setVisibility(View.GONE);
                    }
                });
            } else if (s.equals(MineSweeperGame.GAMENAME)) {
                tmp.setText(R.string.mineSweeperGame);
                tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localCenter.removeGame(MineSweeperGame.GAMENAME);
                        tmp.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    /**
     * set back press
     */
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
