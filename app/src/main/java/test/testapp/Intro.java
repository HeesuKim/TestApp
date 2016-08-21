package test.testapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Intro extends AppCompatActivity {

    ImageView intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    intro=(ImageView) findViewById(R.id.imageView3);
                    Animation alphaAnim= AnimationUtils.loadAnimation(Intro.this,R.anim.fade_in);
                    Animation alphaAni1q= AnimationUtils.loadAnimation(Intro.this,R.anim.fade_out);
                    intro.startAnimation(alphaAnim);
                    Thread.sleep(3500);
                    intro.startAnimation(alphaAni1q);
                    isIntro();
                }
                catch (Exception e){}
            }
        }).start();

    }
        private void isIntro()
        {
            Intent intent = new Intent(this,Menu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }
}





