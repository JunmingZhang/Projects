package fall2018.csc207_project.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.R;

/**
 * the global activity
 */
public class GlobalActivity extends AppCompatActivity {

    /**
     * the global center
     */
    private GlobalCenter globalCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamecenter_main);

        globalCenter = (GlobalCenter)getIntent().getSerializableExtra("GlobalCenter");
        if(globalCenter == null)
            loadGlobalCenter();
        addSignInButtonListener();
        addSignUPButtonListener();

    }

    /**
     * switch to SignIn interface
     */
    public void switchToSignIn() {
        Intent tmp = new Intent(this, SignInActivity.class);
        tmp = tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * switch to SignUp interface
     */
    public void switchToSignUp() {
        Intent tmp = new Intent(this, SignUpActivity.class);
        tmp = tmp.putExtra("GlobalCenter", globalCenter);
        startActivity(tmp);
        finish();
    }

    /**
     * add the listener for SignIn button
     */
    private void addSignInButtonListener() {
        Button startButton = findViewById(R.id.global_signin);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignIn();
            }
        });
    }

    /**
     * add the listener fot SignUp button
     */
    private void addSignUPButtonListener() {
        Button startButton = findViewById(R.id.global_signup);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignUp();
            }
        });
    }

    /**
     * load GlobalCenter interface
     */
    private void loadGlobalCenter(){
        ObjectInputStream in = null;
        try {
            FileInputStream fileIn = getApplicationContext().openFileInput("dataBase_0172.ser");
            in = new ObjectInputStream(fileIn);
            globalCenter = (GlobalCenter) in.readObject();

        } catch (FileNotFoundException e) {
            globalCenter = new GlobalCenter();
            globalCenter.saveAll(getApplicationContext());
        } catch (ClassNotFoundException e) {
            Log.e("setup global center", "Class not found: " + e.toString());
        } catch (IOException e) {
            Log.e("setup global center", "IOException: " + e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                }catch (IOException e) {
                    Log.e("setup global center", "IOException: " + e.toString());
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalCenter.saveAll(getApplicationContext());
    }

    /**
     * set back press
     */
    public void onBackPressed() {
        finish();
    }

}
