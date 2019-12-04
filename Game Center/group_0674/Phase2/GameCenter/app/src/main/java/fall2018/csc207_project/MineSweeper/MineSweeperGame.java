package fall2018.csc207_project.MineSweeper;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import fall2018.csc207_project.Interfaces.Game;

/**
 * game data model for mine sweeper
 */
public class MineSweeperGame extends Observable implements Game, Serializable {

    /**
     * serial number of the game model
     */
    private static final long serialVersionUID = 314346L;

    public static final String GAMENAME = "minesweeperGame";

    /**
     * board of this game
     */
    private Board board;

    /**
     * time used
     */
    private int secondPassed = 0;

    /**
     * number of flags the user can raise
     */
    private int flagsLeft;

    /**
     * if the toast of winning has been made
     */
    private boolean announced = false;

    /**
     * if the game ends, win or lose
     */
    private boolean gameOver = false;

    /**
     * if the player win the game
     * if lose, only gameOver is true, otherwise win and gameOver are both true
     */
    private boolean win = false;

    /**
     * if help has been used
     */
    private boolean help = true;

    /**
     * player for the game
     */
    private String player;

    /**
     * construct the game
     *
     * @param settings basic fields of the game
     */
    public MineSweeperGame(List<Object> settings) {
        //init the game if the game is not initialized
        if (settings.size() == 1)
            initWithComplexity((Integer) settings.get(0));
            //load the saved game if the player played before
        else {
            this.board = new Board((Board) (settings.get(0)));
            this.secondPassed = (Integer) (settings.get(1));
            this.flagsLeft = (Integer) (settings.get(2));
            this.announced = (Boolean) (settings.get(3));
            this.gameOver = (Boolean) (settings.get(4));
            this.win = (Boolean) (settings.get(5));
            this.help = (Boolean) (settings.get(6));
        }
    }

    /**
     * initialize the game with the complexity
     */
    private void initWithComplexity(int complexity) {
        board = new Board(complexity);
        flagsLeft = complexity;
    }

    @Override
    public List<Object> getSetting() {
        List<Object> settings = new LinkedList<>();
        settings.add(new Board(this.board));
        settings.add(this.secondPassed);
        settings.add(flagsLeft);
        settings.add(announced);
        settings.add(gameOver);
        settings.add(win);
        settings.add(help);

        return settings;
    }

    /**
     * store the time used of the game
     *
     * @param secondPassed time used
     */
    void setSecondPassed(int secondPassed) {
        this.secondPassed = secondPassed;
        setChanged();
    }

    /**
     * set if the win toast has been made
     */
    void setAnnounced() {
        this.announced = true;
    }

    /**
     * get the board of the game used
     *
     * @return board for the game
     */
    Board getBoard() {
        return board;
    }

    /**
     * get time used
     *
     * @return time used
     */
    int getSecondsPassed() {
        return this.secondPassed;
    }

    /**
     * get number of flags left
     *
     * @return number of flags left
     */
    int getFlagsLeft() {
        return this.flagsLeft;
    }

    /**
     * get if the player has won
     *
     * @return if the player has won
     */
    boolean getWin() {
        return this.win;
    }

    /**
     * get if the game ends
     *
     * @return if the game ends
     */
    boolean getGameOver() {
        return this.gameOver;
    }

    /**
     * get if the help has been used
     *
     * @return if the help has been used
     */
    boolean getHelp() {
        return help;
    }

    /**
     * get if win toast has been made
     *
     * @return if win toast has been made
     */
    boolean hasAnnouncedInverted() {
        return !this.announced;
    }

    /**
     * flip the tile at given location
     *
     * @param row horizontal coordinator of the tile
     * @param col veritical coordinator of the tile
     */
    void flipTile(int row, int col) {
        board.getTiles()[row][col].setFliped();
        int boardSize = board.getBoardSize();
        //if the tile flipped is 0, then we can flip other tiles around, if the tile is not a bomb
        if (board.getTiles()[row][col].getNum() == 0) {
            if (row != 0 && !board.getTiles()
                    [row - 1][col].isFlipped()) {
                flipTile(row - 1, col);
            }
            if (row < boardSize - 1 && !board.getTiles()
                    [row + 1][col].isFlipped()) {
                flipTile(row + 1, col);
            }
            if (col != 0 && !board.getTiles()
                    [row][col - 1].isFlipped()) {
                flipTile(row, col - 1);
            }
            if (col < boardSize - 1 && !board.getTiles()
                    [row][col + 1].isFlipped()) {
                flipTile(row, col + 1);
            }
            if (row != 0 && col != 0 && !board.getTiles()
                    [row - 1][col - 1].isFlipped()) {
                flipTile(row - 1, col - 1);
            }
            if (row != 0 && col < boardSize - 1 && !board.getTiles()
                    [row - 1][col + 1].isFlipped()) {
                flipTile(row - 1, col + 1);
            }
            if (row < boardSize - 1 && col != 0 && !board.getTiles()
                    [row + 1][col - 1].isFlipped()) {
                flipTile(row + 1, col - 1);
            }
            if (row < boardSize - 1 && col < boardSize - 1 && !board.getTiles()
                    [row + 1][col + 1].isFlipped()) {
                flipTile(row + 1, col + 1);
            }
        }
        //lose the game if one bomb is flipped
        if (board.getTiles()[row][col].getNum() == 10) {
            gameOver = true;
        }
        setChanged();
        notifyObservers(row * boardSize + col);
    }

    /**
     * check if the user win the game
     */
    void checkEnd() {
        int boardSize = board.getBoardSize();
        int notRevealed = boardSize * boardSize;
        int bombNotFound = board.getBombNum();
        for (Tile tile : board) {
            if (tile.isFlipped() || tile.isLabeled()) {
                notRevealed--;
            }
            if (tile.isLabeled() && tile.getNum() >= 10) {
                bombNotFound--;
            }
        }
        if (notRevealed == 0 && bombNotFound == 0) {
            win = true;
            gameOver = true;

            setChanged();
            notifyObservers();
        }
    }

    /**
     * set the player of the game
     *
     * @param name name of the player
     */
    void setPlayer(String name) {
        this.player = name;
    }

    /**
     * notify the scoreboard to sign up the new mark
     */
    @Override
    public void notifyScoreBoard() {
        setChanged();
        LinkedList info = new LinkedList<>();
        info.add(player);
        info.add(this.getSetting());
        notifyObservers(info);
    }


    /**
     * label or unlabel the flag on the tile
     *
     * @param row row index of the tile
     * @param col column index of the tile
     */
    void labelTile(int row, int col) {
        //if the tile has been flagged
        if (board.getTiles()[row][col].isLabeled()) {
            flagsLeft++;
            board.getTiles()
                    [row][col].setLabeled(!board.getTiles()[row][col].isLabeled());
            //if the tile has not been flagged and there are flags available left
        } else if (!board.getTiles()
                [row][col].isLabeled() && flagsLeft > 0) {
            flagsLeft--;
            board.getTiles()
                    [row][col].setLabeled(!board.getTiles()[row][col].isLabeled());
        }
        int boardSize = board.getBoardSize();
        setChanged();
        notifyObservers(row * boardSize + col);
    }

    /**
     * help to flip one tile with number
     */
    void help() {
        int position = 0;
        for (Tile tile : board) {
            if (!tile.isFlipped() && tile.getNum() > 0 && tile.getNum() < 10) {
                int row = position / board.getBoardSize();
                int col = position % board.getBoardSize();
                if (tile.isLabeled()) {
                    labelTile(row, col);
                }
                flipTile(row, col);
                break;
            }
            position++;
        }
        //invalidate help chance if the help has been used
        help = false;
    }
}
