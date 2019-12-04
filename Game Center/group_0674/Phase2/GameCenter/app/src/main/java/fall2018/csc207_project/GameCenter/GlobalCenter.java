package fall2018.csc207_project.GameCenter;

import android.app.Activity;
import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The GlobalCenter for Application
 */

public class GlobalCenter implements Serializable {

    /**
     * the serial number of global center
     */
    private static final long serialVersionUID = 2L;

    /**
     * the map from String to account
     * the map from String to LocalGameCenter
     * the map from String to ScoreBoard
     * the current account of currentPlayer
     */
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, LocalGameCenter> localCenters = new HashMap<>();
    private Map<String, ScoreBoard> scoreboards = new HashMap<>(); // gameName -> ScoreBoard
    private Account currentPlayer;


    /**
     * get the scoreboard
     *
     * @return scoreboard
     */
    public Map getScoreBoards() {
        return scoreboards;
    }

    /**
     * check the username and password
     *
     * @param username the username which user input
     * @param password the password which user input
     * @return whether the username and password is matched
     */
    public Boolean signIn(String username, String password) {

        if (accounts.containsKey(username)) {
            Account user = accounts.get(username);
            if (user.signIn(password)) {
                currentPlayer = user;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * sign up a nwe user
     *
     * @param username  the username which user input
     * @param password1 the password which user input
     * @param password2 the password which user confirmed
     * @return Sign in the game use the new account
     * @throws Exception username exist or password not match or user does not exist
     */
    public boolean signUp(String username, String password1, String password2) throws Exception {
        if (username.equals("") || password1.equals("") || password2.equals(""))
            throw new EmptyFieldException();
        else if (accounts.containsKey(username)) {
            throw new UsernameExistException();
        } else if (!password1.equals(password2)) {
            throw new PasswordNotMatchingException();
        } else {
            Account newAccount = new Account(username, password1);
            accounts.put(username, newAccount);
            addLocalGameCenter(username);
            return signIn(username, password1);
        }
    }

    /**
     * add the local game center for each user
     *
     * @param username the username which the user log in
     */
    private void addLocalGameCenter(String username) {
        LocalGameCenter newLocalCenter = new LocalGameCenter();
        localCenters.put(username, newLocalCenter);
    }

    /**
     * get the information of current player
     *
     * @return current player
     */
    public Account getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * get the local game center for each user
     *
     * @param username the user which user log in
     * @return local game center
     */
    public LocalGameCenter getLocalGameCenter(String username) {
        return localCenters.get(username);
    }

    /**
     * add game to scoreboard
     *
     * @param gameName   the name of game which will be added to scoreboard
     * @param scoreboard the scoreboard that the score will be added to
     */
    public void addScoreBoard(String gameName, ScoreBoard scoreboard) {
        if (!scoreboards.containsKey(gameName)) {
            scoreboards.put(gameName, scoreboard);
        }
    }

    /**
     * save all the data of this application
     *
     * @param context the global game center
     */
    public void saveAll(Context context) {
        ObjectOutputStream out = null;
        try {
            FileOutputStream fileOut = context.openFileOutput("dataBase_0172.ser", Activity.MODE_PRIVATE);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            fileOut.getFD().sync();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
