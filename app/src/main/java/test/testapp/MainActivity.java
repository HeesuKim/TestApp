package test.testapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(new GameView(this));
        /*Display dis = getWindowManager().getDefaultDisplay();
        System.out.println("wid : " + dis.getWidth());
        System.out.println("hei : " + dis.getHeight());*/
    }
}
