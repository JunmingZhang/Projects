package fall2018.csc207_project.GameCenter;

import java.io.Serializable;

/**
 * account of GameCenter
 */
public class Account implements Serializable {

    /**
     * serial number of the account
     */
    private static final long serialVersionUID = 1L;

    /**
     * username of account
     * password of account
     */
    private String username;
    private String password;

    /**
     * construct a account input the username and password
     *
     * @param username the username of account
     * @param password the password of account
     */
    Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * get the username
     *
     * @return username the username of account
     */
    public String getUsername() {
        return username;
    }

    /**
     * set the username by input a string
     *
     * @param username the username of account
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get the password
     *
     * @return password the password of account
     */
    public String getPassword() {
        return password;
    }

    /**
     * set the password
     *
     * @param password the password of account
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * check whether the password which the user enter is correct
     *
     * @param password the password which the user enter
     * @return whether the password which input is equal to the account password
     */
    Boolean signIn(String password) {
        return (password.equals(this.password));
    }


}
