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

    //일시정지, 재개에 필요한 변수들
    private PopupWindow pw;
    private LayoutInflater inflater;
    private View popupWindow;
    private Button btClose, btConti;
    int resumeF = 0;

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

        //일시정지시 출력될 팝업창에 관련된 변수들
        inflater =
                (LayoutInflater) getBaseContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.pause_popup,
                (ViewGroup)findViewById(R.id.popup_view));
        btClose = (Button) popupWindow.findViewById(R.id.pop_bt1);
        btConti = (Button) popupWindow.findViewById(R.id.pop_bt2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*onResume은 앱 첫실행시에도 수행되므로 구분이 필요
        * 따라서 일시정지 플래그가 1일 경우에만 팝업을 띄움*/
        if (resumeF == 1){
            pw = new PopupWindow(popupWindow, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); //현재 액티비티 종료
                }
            });
            btConti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();   //팝업창 닫기
                    GV.onResume();  //게임뷰 재시작
                }
            });
            pw.showAtLocation(popupWindow, Gravity.CENTER, 0, 0);
            resumeF = 0;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        GV.onPause();
        resumeF = 1; //일시정지 플래그
    }
}
