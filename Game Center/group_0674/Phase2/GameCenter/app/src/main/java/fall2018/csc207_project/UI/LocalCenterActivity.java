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
import fall2018.csc207_project.R;
import fall2018.csc207_project.SlidingTileGame.SlidingTileGame;
import fall2018.csc207_project.game2048.Game2048;

/**
 * the activity of local game center
 */
public class LocalCenterActivity extends AppCompatActivity {

    /**
     * the global center
     * the local game center
     */
    private GlobalCenter globalCenter;
    private LocalGameCenter localCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fall2018.csc207_project.R.layout.local_center);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        localCenter = globalCenter.
                getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());

        localCenter.setCurGame(null);

        TextView welcome = findViewById(fall2018.csc207_project.R.id.welcome);
        welcome.setText("Welcome!\nuser: " + globalCenter.getCurrentPlayer().getUsername());
        loadButtons();

        addAddGameButtonListener();
        addDeleteGameButtonListener();
        addLogOffButtonListener();

    }

    /**
     * the button of load
     */
    private void loadButtons() {

        Set<String> gameSet = localCenter.getGames();
        LinearLayout buttonLayout =
                findViewById(fall2018.csc207_project.R.id.local_center_button_layout);
        Button tmp;

        for(String s:gameSet) {
            tmp = new Button(this);
            buttonLayout.addView(tmp);
            if(s.equals(SlidingTileGame.GAMENAME)) {
                tmp.setText(fall2018.csc207_project.R.string.slidingTileGame);
                tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localCenter.setCurGameName(SlidingTileGame.GAMENAME);
                        gameLaunch();
                    }
                });
            }
            else if(s.equals(Game2048.GAMENAME)) {
                tmp.setText(fall2018.csc207_project.R.string.game2048);
                tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localCenter.setCurGameName(Game2048.GAMENAME);
                        gameLaunch();
                    }
                });
            }
            else if(s.equals(MineSweeperGame.GAMENAME)) {
                tmp.setText(fall2018.csc207_project.R.string.mineSweeperGame);
                tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localCenter.setCurGameName(MineSweeperGame.GAMENAME);
                        gameLaunch();
                    }
                });
            }
        }
    }

    /**
     * add the listener of adding game button
     */
    private void addAddGameButtonListener() {
        Button button = findViewById(fall2018.csc207_project.R.id.add_game_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToAddDelete(true);
            }
        });
    }

    /**
     * switch the interface of add or delete
     * @param add whether the game has been added
     */
    private void switchToAddDelete(boolean add) {
        Intent tmp = new Intent(this, AddDeleteGameActivity.class);
        tmp.putExtra("addGame?", add);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * add the listener of delete game button
     */
    private void addDeleteGameButtonListener() {
        Button button = findViewById(fall2018.csc207_project.R.id.delete_game_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToAddDelete(false);

            }
        });
    }

    /**
     * launch the game
     */
    private void gameLaunch() {
        Intent tmp = new Intent(this, StartingActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * add the listener of log off button
     */
    private void addLogOffButtonListener() {
        Button button = findViewById(R.id.log_off_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }

    /**
     * set back press
     */
    public void onBackPressed() {
        Intent tmp = new Intent(this, GlobalActivity.class);
        tmp = tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }
}
