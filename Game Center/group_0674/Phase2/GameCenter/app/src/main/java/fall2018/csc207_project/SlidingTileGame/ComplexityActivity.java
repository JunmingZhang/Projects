package fall2018.csc207_project.SlidingTileGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.LocalGameCenter;
import fall2018.csc207_project.R;
import fall2018.csc207_project.UI.StartingActivity;

/**
 * The setting for SlidingTileGame activity.
 */
public class ComplexityActivity extends AppCompatActivity {
    /**
     * global center passed from last activity
     */
    private GlobalCenter globalCenter;
    /**
     * local center passed from last activity
     */
    private LocalGameCenter localCenter;

    /**
     * the maximum of usage of undo is 3
     */
    int undoStep = 3;

    /**
     * initial the graphical interface
     *
     * @param savedInstanceState saved instance state
     */

    protected void onCreate(Bundle savedInstanceState) {
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        localCenter = globalCenter.getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complexity);
        add3x3ButtonListener();
        add4x4ButtonListener();
        add5x5ButtonListener();
        addSetUndoButtonListener();
    }

    /**
     * if the back button is pressed, then return the lst interface.
     */
    public void onBackPressed() {
        Intent tmp = new Intent(this, StartingActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * listen to the 3*3 button
     * if the button is pressed, then begin the game of complexity of 3.
     */
    private void add3x3ButtonListener() {
        Button l3Button = findViewById(R.id.button3x3);
        l3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame(3);
                finish();
            }
        });
    }

    /**
     * listen to the 4*4 button
     * if the button is pressed, then begin the game of complexity of 4.
     */
    private void add4x4ButtonListener() {
        Button l3Button = findViewById(R.id.button4x4);
        l3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame(4);
                finish();
            }
        });
    }

    /**
     * listen to the 5*5 button
     * if the button is pressed, then begin the game of complexity of 5.
     */
    private void add5x5ButtonListener() {
        Button l3Button = findViewById(R.id.button5x5);
        l3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame(5);
                finish();
            }
        });
    }

    /**
     * listen to the undo button
     */
    private void addSetUndoButtonListener() {

        Button button = findViewById(R.id.accept_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText undoStepsText = findViewById(R.id.undo_step_input);
                String steps = undoStepsText.getText().toString();
                if (steps.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field can't be empty!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    undoStep = Integer.parseInt(steps);
                    Toast.makeText(getApplicationContext(), "Setting complete!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * begin the game
     *
     * @param complexity the complexity of the new game.
     */
    private void switchToGame(int complexity) {
        LinkedList<Object> settings = new LinkedList<>();
        settings.add(complexity);
        SlidingTileGame game = (SlidingTileGame) localCenter.newGame(SlidingTileGame.GAMENAME, settings);
        game.setUndoStep(undoStep);
        Intent tmp = new Intent(this, GameActivity.class);
        tmp.putExtra("GlobalCenter", globalCenter);
        localCenter.setCurGame(game);
        startActivity(tmp);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }
}
