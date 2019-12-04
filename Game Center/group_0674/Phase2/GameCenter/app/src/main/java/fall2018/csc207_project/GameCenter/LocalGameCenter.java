package fall2018.csc207_project.GameCenter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fall2018.csc207_project.Interfaces.Game;

/**
 * LocalGameCenter for each user
 */
public class LocalGameCenter implements Serializable {
    /**
     * serial number of LocalGameCenter
     */
    private static final long serialVersionUID = 3L;

    /**
     * the number of slot
     * the number of auto save index
     */
    private final int SAVESLOTNUM = 3;

    private final int AUTOSAVEINDEX = 3;

    /**
     * map for local games
     * the current game which user is playing
     * the name of current game
     */
    private Map<String, LinkedList<List<Object>>> localGames;

    private Game curGame;

    private String curGameName;

    /**
     * construct local game center
     */
    public LocalGameCenter() {
        curGame = null;
        curGameName = null;
        localGames = new HashMap<>();
    }

    /**
     * get the current game which is playing
     *
     * @return the current game
     */
    public Game getCurGame() {
        return curGame;
    }

    /**
     * set the current game which will be playing
     *
     * @param game the game will be set as current game
     */
    public void setCurGame(Game game) {
        curGame = game;
    }

    /**
     * add a new game to local game center with the game name
     *
     * @param gameName the game which will be add to slot
     * @return whether the game can be added
     */
    public boolean addGame(String gameName) {
        if (localGames.containsKey(gameName))
            return false;
        else {
            LinkedList<List<Object>> slots = new LinkedList<>();
            for (int i = 0; i <= SAVESLOTNUM; i++) {
                slots.add(new LinkedList<>());
            }
            localGames.put(gameName, slots);
        }
        return true;
    }

    /**
     * set the name of current game
     *
     * @param gameName the name of game will be set
     */
    public void setCurGameName(String gameName) {
        this.curGameName = gameName;
    }


    /**
     * get the number of slot
     *
     * @return the the number of slot
     */
    public int getSAVESLOTNUM() {
        return SAVESLOTNUM;
    }

    /**
     * get the index of auto save
     *
     * @return the index of auto save
     */
    public int getAUTOSAVEINDEX() {
        return AUTOSAVEINDEX;
    }

    /**
     * get the name of current game
     *
     * @return the name of current game
     */
    public String getCurGameName() {
        return this.curGameName;
    }

    /**
     * get the set which contains games
     *
     * @return set of games
     */
    public Set<String> getGames() {
        return localGames.keySet();
    }

    /**
     * get the list of the name of current games
     *
     * @return the list of current games
     */
    public LinkedList<List<Object>> getSavingSlots() {
        return localGames.get(curGameName);
    }

    /**
     * remove the game
     *
     * @param gameName the name of game
     * @return whether the game is removed
     */
    public boolean removeGame(String gameName) {
        return localGames.remove(gameName) != null;
    }

    /**
     * save the game to the slot
     *
     * @param gameSlot  the slot of game
     * @param timeStamp the stamp of time
     * @return whether the user saves the game
     */
    public boolean saveGame(int gameSlot, String timeStamp) {

        if (gameSlot > SAVESLOTNUM || gameSlot < 0 || curGame == null || curGameName == null) {
            return false;
        }
        List<Object> temp = localGames.get(curGameName).get(gameSlot);
        temp.clear();
        temp.addAll(curGame.getSetting());
        temp.add(timeStamp);

        return true;
    }

    /**
     * load the game
     *
     * @param gameName the name of the loading game
     * @param gameSlot the slot which the game is saved
     * @return null or new game
     */
    public Game loadGame(String gameName, int gameSlot) {
        List<Object> settings = localGames.get(gameName).get(gameSlot);
        return new GameFactory().createGame(gameName, settings);
    }

    /**
     * creat a new game with the setting information
     *
     * @param gameName the name of game
     * @param settings the information of game
     * @return null or new game
     */
    public Game newGame(String gameName, List<Object> settings) {
        return new GameFactory().createGame(gameName, settings);
    }

    /**
     * auto save the game
     *
     * @param timestamp stamp of time
     */
    public void autoSave(String timestamp) {
        saveGame(AUTOSAVEINDEX, timestamp);
    }

}
