package fall2018.csc207_project.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.LocalGameCenter;
import fall2018.csc207_project.GameCenter.ScoreBoard;
import fall2018.csc207_project.R;

/**
 * The activity of the scoreboard
 */
public class ScoreBoardActivity extends AppCompatActivity {

    private GlobalCenter globalCenter;
    private boolean state;
    private ScoreBoard scoreboard;
    private LinearLayout canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        state = getIntent().getBooleanExtra("perPlayer?", true);
        LocalGameCenter localCenter = globalCenter.
                getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());
        scoreboard = (ScoreBoard) globalCenter.getScoreBoards().get(localCenter.getCurGameName());
        canvas = findViewById(R.id.score_board_canvas);

        TextView band = findViewById(R.id.score_board_band);
        if(!state) {
            band.setText(R.string.global_score_band);
            displayGlobalScore();
        } else
            displayPlayerScore();

    }

    /**
     * Display 10 scores of the current user.
     */
    public void displayPlayerScore() {
        Integer[] playerScorers = scoreboard.
                getPlayerScore(globalCenter.getCurrentPlayer().getUsername());
        for (int x = 0; x < playerScorers.length; x++) {
            scoreColumnView((x+1)+"",
                    globalCenter.getCurrentPlayer().getUsername(),
                    playerScorers[x] == null || playerScorers[x] == 0 ? "-":playerScorers[x]+"");

        }
    }

    /**
     * Display 10 highest user scores across the whole game. Each player appear once only.
     */
    public void displayGlobalScore() {
        Map<Integer, String> indexList = scoreboard.getIndexList();
        int[] topScores = scoreboard.getTopScores();

        for (int x = 0; x < topScores.length; x++) {
            scoreColumnView((x + 1) + "",
                    indexList.get(x) == null ? "-" : indexList.get(x),
                    topScores[x] == 0 ? "-" : topScores[x] + "");
        }

    }

    /**
     * Set the view of scores for each column.
     *
     * @param rankNum   rank number
     * @param user      the username
     * @param topScores the scores
     */

    private void scoreColumnView(String rankNum, String user, String topScores) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        TextView rank = new TextView(this);
        TextView name = new TextView(this);
        TextView score = new TextView(this);

        generateTextView(rank,rankNum);
        generateTextView(name, user);
        generateTextView(score,topScores);

        ll.addView(rank);
        ll.addView(name);
        ll.addView(score);

        canvas.addView(ll);
    }

    /**
     * Generate a textView with specific setting.
     *
     * @param view the textView
     * @param text the text to be set
     */
    private void generateTextView(TextView view, String text) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        param.setMargins(0,20,0,0);
        view.setText(text);
        view.setTextSize(21);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(param);
    }

    @Override
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


}
