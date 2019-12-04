package fall2018.csc207_project.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.LocalGameCenter;
import fall2018.csc207_project.Interfaces.Game;
import fall2018.csc207_project.MineSweeper.Board;
import fall2018.csc207_project.MineSweeper.MineSweeperGame;
import fall2018.csc207_project.R;
import fall2018.csc207_project.SlidingTileGame.SlidingTileGame;
import fall2018.csc207_project.game2048.Game2048;

/**
 * activity of load or save
 */
public class LoadOrSaveGameActivity extends AppCompatActivity {

    /**
     * the global center
     * the local game center
     * the state of game
     */
    private GlobalCenter globalCenter;
    private LocalGameCenter localCenter;
    private boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_and_save);
        state = getIntent().getBooleanExtra("loadGame?", true);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));
        localCenter = globalCenter.
                getLocalGameCenter(globalCenter.getCurrentPlayer().getUsername());

        initTextView();
        addSlotButton();
    }

    /**
     * init TextView
     */
    private void initTextView() {
        if(state) {
            TextView addDeleteBand = findViewById(R.id.save_load_band);
            TextView tapBand = findViewById(R.id.select_band);

            addDeleteBand.setText(R.string.load_band_text);
            tapBand.setText(R.string.tap_load);
        }
    }

    /**
     * add slot Button
     */
    private void addSlotButton() {
        final LinkedList<List<Object>> slots = localCenter.getSavingSlots();
        LinearLayout ll = findViewById(R.id.save_load_slot);
        for(int i=0;i<localCenter.getSAVESLOTNUM();i++) {
            final Button button = new Button(this);
            button.setId(i);
            initButtonLabel(button, slots.get(i));
            ll.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state) {
                        try {
                            Game game = localCenter.loadGame(localCenter.getCurGameName(),
                                    button.getId());
                            switchToGame(game);
                        } catch (IndexOutOfBoundsException e) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "There is no saved game for this block yet!",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                    } else {
                        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                                .format(Calendar.getInstance().getTime());
                        localCenter.saveGame(button.getId(), timeStamp);
                        initButtonLabel(button, slots.get(button.getId()));
                    }
                }
            });
        }
        if(state) {
            addAutoSaveView();
        }

    }

    /**
     * add view of AutoSave
     */
    private void addAutoSaveView() {
        LinearLayout ll = findViewById(R.id.save_load_slot);
        TextView tv = new TextView(this);
        Button button = new Button(this);
        initButtonLabel(button, localCenter.getSavingSlots().get(localCenter.getAUTOSAVEINDEX()));
        tv.setText(R.string.auto_save_slot);
        tv.setTextSize(17);
        tv.setGravity(Gravity.CENTER);
        ll.addView(tv);
        ll.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game game = localCenter.loadGame(localCenter.getCurGameName(),
                        localCenter.getAUTOSAVEINDEX());
                switchToGame(game);
            }
        });
    }

    /**
     * switch to the specific game
     * @param game one of the game which user chooses
     */
    private void switchToGame(Game game) {

        if(game instanceof SlidingTileGame) {
            Intent tmp = new Intent(this,
                    fall2018.csc207_project.SlidingTileGame.GameActivity.class);
            tmp.putExtra("GlobalCenter", globalCenter);
            localCenter.setCurGame(game);
            startActivity(tmp);
        } else if(game instanceof Game2048) {
            Intent tmp = new Intent(this,
                    fall2018.csc207_project.game2048.GameActivity.class);
            tmp.putExtra("GlobalCenter", globalCenter);
            localCenter.setCurGame(game);
            startActivity(tmp);
        } else if(game instanceof MineSweeperGame) {
            Intent tmp = new Intent(this,
                    fall2018.csc207_project.MineSweeper.GameActivity.class);
            tmp.putExtra("GlobalCenter", globalCenter);
            localCenter.setCurGame(game);
            startActivity(tmp);
        }

        finish();
    }

    /**
     * init the label of button
     * @param button the button which will be click
     * @param slot the slot which the button will be put in
     */
    private void initButtonLabel(Button button, List<Object> slot) {
        if(slot.size() == 0) {
            button.setText("(Blank Slot)");
        } else if(localCenter.getCurGame() instanceof SlidingTileGame){
            int complexity = (Integer) ((LinkedList<Object>) slot).getFirst();
            String time = (String) ((LinkedList<Object>) slot).getLast();
            button.setText("Complexity: " + complexity + "x" + complexity
                    + "\n" + "Save Time: " + time);
        } else if(localCenter.getCurGame() instanceof Game2048) {
            int score = (Integer) slot.get(3);
            String time = (String) ((LinkedList<Object>) slot).getLast();
            button.setText("Score: "+score+"\n" + "Save Time: "+time);
        } else if(localCenter.getCurGame() instanceof MineSweeperGame) {
            Board board = (Board) ((LinkedList<Object>) slot).getFirst();
            int timePassed = (Integer) ((LinkedList<Object>) slot).get(1);
            int bombNum = board.getBombNum();
            String time = (String) ((LinkedList<Object>) slot).getLast();
            button.setText("Time Passed: " + timePassed + "\n" + "Bomb Number: "
                    + bombNum + "\n" + "Save time: " + time);
        }
    }

    /**
     * set back press
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
}
