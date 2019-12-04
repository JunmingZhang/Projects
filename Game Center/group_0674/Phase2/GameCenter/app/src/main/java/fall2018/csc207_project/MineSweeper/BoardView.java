package fall2018.csc207_project.MineSweeper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import fall2018.csc207_project.R;

/**
 * initialize the view of the board
 */
public class BoardView extends GridLayout {

    /**
     * data model of the game
     */
    private MineSweeperGame game;

    /**
     *
     */
    private MovementController movementController;

    /**
     * table of the tiles
     */
    private Button[][] tileTable;

    /**
     * default width
     */
    private int DEFAULT_WIDTH = 108;


    /**
     * construct the board view
     *
     * @param context game activity
     */
    public BoardView(Context context) {
        super(context);
        initBoardView(context);
    }

    /**
     * construct the board view
     *
     * @param context game activity
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoardView(context);
    }

    /**
     * construct the board view
     *
     * @param context      game activity
     * @param attrs        default setting in android
     * @param defStyleAttr default setting in android
     */
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBoardView(context);
    }

    /**
     * initialize the view of the game (game activity)
     *
     * @param context game activity
     */
    public void initBoardView(Context context) {
        int boardSize = 10;
        setColumnCount(boardSize);
        tileTable = new Button[boardSize][boardSize];
        movementController = new MovementController();
        //set overall grid layout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.setMargins(1, 1, 1, 1);

        //put tiles to the grid, assign each tile to a button
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                Button button = new Button(getContext());
                button.setLongClickable(true);
                final int row = x;
                final int col = y;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movementController.flip(getContext(), row, col);

                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        movementController.changeLabelState(getContext(), row, col);
                        return true;
                    }
                });

                tileTable[x][y] = button;
                FrameLayout fl = new FrameLayout(getContext());
                fl.addView(tileTable[x][y], params);
                addView(fl, DEFAULT_WIDTH, DEFAULT_WIDTH);
            }
        }

    }

    /**
     * set the view of each tile
     */
    public void setTableImage() {
        Tile tile;
        Board board = movementController.getGame().getBoard();
        Tile[][] boardTable = board.getTiles();
        int boardSize = board.getBoardSize();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                tile = boardTable[x][y];
                int resourceID = tile.getTileImage();
                tileTable[x][y].setBackgroundResource(resourceID);
            }
        }
    }

    /**
     * set the view of the tile by its index
     *
     * @param index index of the tile
     */
    public void setTileImage(int index) {
        Board board = movementController.getGame().getBoard();
        Tile[][] boardTable = board.getTiles();
        int boardSize = board.getBoardSize();
        int row = index / boardSize;
        int col = index % boardSize;
        Tile tile = boardTable[row][col];
        tileTable[row][col].setBackgroundResource(tile.getTileImage());
    }

    /**
     * assign game model to the controller
     *
     * @param game game model
     */
    public void setGame(MineSweeperGame game) {
        this.game = game;
        movementController.setGame(game);

    }

    MovementController getMController() {
        return movementController;
    }
}
