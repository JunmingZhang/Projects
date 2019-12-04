package fall2018.csc207_project.MineSweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc207_project.GameCenter.GlobalCenter;
import fall2018.csc207_project.R;
import fall2018.csc207_project.UI.StartingActivity;

/**
 * a welcome page
 * <p>
 * adapted from https://github.com/lany192/Minesweeper.git, includes this activity and XML file
 * activity_splash, logo from github above as well
 * autoswitch activity cited from https://blog.csdn.net/wo_ha/article/details/50688296
 */

public class SplashActivity extends Activity {

    /**
     * global centre of the game
     */
    private GlobalCenter globalCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        globalCenter = (GlobalCenter) (getIntent().getSerializableExtra("GlobalCenter"));

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        GameComplexityActivity.class);
                intent.putExtra("GlobalCenter", globalCenter);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        };
        timer.schedule(timerTask, 1000 * 2);
    }

    @Override
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
