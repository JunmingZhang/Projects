package fall2018.csc207_project.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fall2018.csc207_project.GameCenter.EmptyFieldException;
import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.GameCenter.PasswordNotMatchingException;
import fall2018.csc207_project.GameCenter.UsernameExistException;
import fall2018.csc207_project.R;

/**
 * The sign up activity of the game center.
 */
public class SignUpActivity extends AppCompatActivity {

    private GlobalCenter globalCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        globalCenter = (GlobalCenter)getIntent().getSerializableExtra("GlobalCenter");
        addSignUpButtonListener();

    }

    /**
     * Add a sign up button listener and link it to its button.
     */
    private void addSignUpButtonListener(){
        Button loginButton = findViewById(R.id.signup_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSignUp()) {
                    localCenter();
                }
            }
        });
    }

    /**
     * Check whether sign up succeed or not.
     *
     * @return sign up success or not.
     */
    private boolean checkSignUp() {
        EditText user = findViewById(R.id.signup_username);
        EditText pswInput = findViewById(R.id.signup_password);
        EditText psw2Input = findViewById(R.id.signup_password2);
        String userName = user.getText().toString();
        String password = pswInput.getText().toString();
        String password2 = psw2Input.getText().toString();

        try {
            return globalCenter.signUp(userName,password,password2);
        }catch (EmptyFieldException e) {
            Toast.makeText(getApplicationContext(), "Field can't be empty!",
                    Toast.LENGTH_SHORT).show();
        }catch (PasswordNotMatchingException e) {
            Toast.makeText(getApplicationContext(), "Password not matching!",
                    Toast.LENGTH_SHORT).show();
        }catch (UsernameExistException e) {
            Toast.makeText(getApplicationContext(), "Username already exist!",
                    Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error!",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Switch to local center activity.
     */
    private void localCenter(){
        Intent tmp = new Intent(this, LocalCenterActivity.class);
        tmp = tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Intent tmp = new Intent(this, GlobalActivity.class);
        startActivity(tmp);
        finish();
    }
}
