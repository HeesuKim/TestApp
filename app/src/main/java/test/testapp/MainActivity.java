package test.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

public class MainActivity extends Activity {
    private GameView GV;
    private PopupWindow pw;
    private LayoutInflater inflater;
    private View popupWindow;
    private Button btClose, btConti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point outSize = new Point();
        Display dis = getWindowManager().getDefaultDisplay();
        dis.getSize(outSize);
        GV = new GameView(this);
        GV.setLength(outSize.x, outSize.y);
        setContentView(GV);
        inflater =
                (LayoutInflater) getBaseContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.pause_popup,
                (ViewGroup)findViewById(R.id.popup_view));
        btClose = (Button) popupWindow.findViewById(R.id.pop_bt1);
        btConti = (Button) popupWindow.findViewById(R.id.pop_bt2);
    }

   /* LayoutInflater inflater =
            (LayoutInflater) getBaseContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View layout = inflater.inflate(R.layout.pause_popup,
            (ViewGroup)findViewById(R.id.popup_view));
    Button btClose = (Button) layout.findViewById(R.id.pop_bt1);
    Button btConti = (Button) layout.findViewById(R.id.pop_bt2);*/

    int resumeF = 0;
    @Override
    protected void onResume() {
        System.out.println("KSTEN : onResume" + " // " + resumeF);
        super.onResume();
        if (resumeF == 1){
            //GV.onResume();

            pw = new PopupWindow(popupWindow, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            btConti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                    GV.onResume();
                }
            });
            pw.showAtLocation(popupWindow, Gravity.CENTER, 0, 0);
            resumeF = 0;
        }
    }

    @Override
    protected void onStop() {
        System.out.println("KSTEN : onStop");
        super.onStop();
        GV.onPause();
        resumeF = 1;
    }
}
