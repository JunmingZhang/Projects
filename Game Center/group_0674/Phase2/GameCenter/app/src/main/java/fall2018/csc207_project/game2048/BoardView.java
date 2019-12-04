package fall2018.csc207_project.game2048;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import fall2018.csc207_project.R;


public class BoardView extends GridLayout {
    /**
     * the error permitted for each movement
     */
    public final static int THRESHOLD = 5;

    /**
     * the textview on each button
     */
    private TextView[][] tileLabel;

    /**
     * movement controller to control the game data model
     */
    private MovementController mController;

    /**
     * the width of the board (actual length on screen)
     */
    private static final int DEFAULT_WIDTH = 245;

    /**
     * the width of the board (cells/row)
     */
    private int complexity = 4;

    /**
     * game data model
     */
    private Game2048 game;


    /**
     * consruct the board view
     *
     * @param context game activity
     */
    public BoardView(Context context) {
        super(context);
        initBoardView(context);
    }

    /**
     * construct the view of the board
     *
     * @param context the game activity
     * @param attrs   attribute set
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoardView(context);
    }

    /**
     * construct the view of the board
     *
     * @param context      the game activity
     * @param attrs        attribute set
     * @param defStyleAttr attribute set
     */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBoardView(context);
    }

    /**
     * make the label of text view on each tile
     *
     * @param label text view on each tile
     */
    private void formatTextView(TextView label) {
        label.setTextSize(35);
        label.setTypeface(null, Typeface.BOLD);
        label.setTextColor(getResources().getColor(R.color.default2048TextColor));
        label.setGravity(Gravity.CENTER);
    }

    /**
     * set the game data model
     *
     * @param game game data model
     */
    public void setGame(Game2048 game) {
        this.game = game;
        mController.setGame(game);
        initViewTable();
    }

    /**
     * return the movement controller of the view
     *
     * @return the movement controller of the view
     */
    public MovementController getMController() {
        return mController;
    }

    /**
     * make the label of text view on each tile by the number on the tile
     *
     * @param label text view on each tile by the number on the tile
     * @param num   the number on the tile
     */
    private void setTextViewLabel(TextView label, int num) {
        String n = num + "";
        if (num <= 0)
            label.setText("");
        else
            label.setText(n);
    }

    /**
     * initialize the view of the grid
     */
    private void initViewTable() {
        //initialize the width of the grid
        Board board = mController.getGame().getBoard();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.setMargins(10, 10, 0, 0);

        //put tiles on the board
        for (int y = 0; y < complexity; y++) {
            for (int x = 0; x < complexity; x++) {
                tileLabel[x][y] = new TextView(getContext());
                FrameLayout fl = new FrameLayout(getContext());
                formatTextView(tileLabel[x][y]);
                setTileColor(tileLabel[x][y], board.getTile(x, y).getNum());
                fl.addView(tileLabel[x][y], params);
                setTextViewLabel(tileLabel[x][y], board.getTile(x, y).getNum());
                addView(fl, DEFAULT_WIDTH, DEFAULT_WIDTH);
            }
        }
    }

    /**
     * update the display of the board after each move
     */
    public void updateDisplay() {
        Board board = mController.getGame().getBoard();
        for (int y = 0; y < complexity; y++) {
            for (int x = 0; x < complexity; x++) {
                setTextViewLabel(tileLabel[x][y], board.getTile(x, y).getNum());
                setTileColor(tileLabel[x][y], board.getTile(x, y).getNum());
            }
        }
    }


    /**
     * set the text color by the number of the tile
     *
     * @param label the coloured text on the tile
     * @param num   the number on the tile
     */
    private void setTileColor(TextView label, int num) {
        switch (num) {
            case 0:
                label.setBackgroundColor(getResources().getColor(R.color.tileHolder));
                break;
            case 2:
                label.setBackgroundColor(getResources().getColor(R.color.tile_2));
                break;
            case 4:
                label.setBackgroundColor(getResources().getColor(R.color.tile_4));
                break;
            case 8:
                label.setBackgroundColor(getResources().getColor(R.color.tile_8));
                break;
            case 16:
                label.setBackgroundColor(getResources().getColor(R.color.tile_16));
                break;
            case 32:
                label.setBackgroundColor(getResources().getColor(R.color.tile_32));
                break;
            case 64:
                label.setBackgroundColor(getResources().getColor(R.color.tile_64));
                break;
            case 128:
                label.setBackgroundColor(getResources().getColor(R.color.tile_128));
                break;
            case 256:
                label.setBackgroundColor(getResources().getColor(R.color.tile_256));
                break;
            case 512:
                label.setBackgroundColor(getResources().getColor(R.color.tile_512));
                break;
            case 1024:
                label.setBackgroundColor(getResources().getColor(R.color.tile_1024));
                break;
            case 2048:
                label.setBackgroundColor(getResources().getColor(R.color.tile_2048));
                break;
        }
        if (num > 4)
            label.setTextColor(getResources().getColor(R.color.white));
        else
            label.setTextColor(getResources().getColor(R.color.default2048TextColor));
    }

    /**
     * initialize the view on the board and
     * process the touch of the player
     *
     * @param context game activity
     */
    private void initBoardView(final Context context) {

        //set column length by the complexity
        setColumnCount(complexity);
        tileLabel = new TextView[complexity][complexity];
        mController = new MovementController(game);

        //react based on the user's touch
        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY, offsetX, offsetY;
            private boolean hasMoved;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        hasMoved = mController.processMovement(getContext(), getTouchDirection(offsetX, offsetY));
                        if (hasMoved)
                            updateDisplay();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * get the direction the player move, which should be a vector
     *
     * @param offsetX horizontal coordinate of the move
     * @param offsetY vertical coordinate of the move
     * @return the vector of the movement (2D)
     */
    private int getTouchDirection(float offsetX, float offsetY) {
        int direction = 0;
        if (Math.abs(offsetX) > Math.abs(offsetY)) {
            if (offsetX < -THRESHOLD) {
                direction = 3;
            } else if (offsetX > THRESHOLD) {
                direction = 4;
            }
        } else {
            if (offsetY < -THRESHOLD) {
                direction = 1;
            } else if (offsetY > THRESHOLD) {
                direction = 2;
            }
        }
        return direction;
    }


}

