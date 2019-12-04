package fall2018.csc207_project.GameCenter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AccountTest {

    @Test
    public void testSignIn() {
        Account account = new Account("ee", "1234");
        assertEquals(true, account.signIn("1234"));
    }
}
