package test.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
   // private IState m_state;
    private GraphicObject m_Image, n_Image, n_Image2;
    private SpriteAnimation m_spr;

    private int stFlag = 0;
    private int stPos = 0;

    private double tWidth, tHeight;

    private long curTime = 0;
    private String timeText;

    TimerTask tT;
    Timer mT;

    public GameView(Context context, int width, int height) {
        super(context);
        setFocusable(true);
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());
        getHolder().addCallback(this);
        tWidth = width;
        tHeight = height;

        m_thread = new GameViewThread(getHolder(),this);
        m_Image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ba));
        n_Image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ea));
        n_Image2 = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ea));

        m_Image.SetPosition((int) (tWidth * 1/6), (int) (tHeight * 1/4));
        n_Image.SetPosition((int) (tWidth * 3/6), (int) (tHeight * 1/4));
        n_Image.SetAnspos(0);
        n_Image2.SetPosition((int) (tWidth * 3/6), (int) (tHeight * 3/4));
        n_Image2.SetAnspos(1);


    }

    int timeFlag = 1;
   /* public void timeCounter(int flag) {
        if (flag == 1) {
            tT = new TimerTask() {
                @Override
                public void run() {
                    timeText = String.format("%01d.%03d", curTime/1000, curTime%1000);
                }
            };
            curTime++;
            System.out.println("curTime @@!@ -> " + curTime);
            mT = new Timer();
            mT.schedule(tT, 1000, 1000);
            timeFlag = 2;
        } else if (flag == 3){
            System.out.println("cancel -> " + curTime);
            mT.cancel();
        } else if (flag == 2) {

        }

    }*/

    public boolean correctAns(int cAns, int posNum) {
        if (posNum == n_Image.GetP()) {
            //ystem.out.println("!@#@#@#@posNum : " + posNum);
            return (cAns == n_Image.GetK());
        } else {
            //System.out.println("!@#@#@#@posNum2 : " + posNum);
            return (cAns == n_Image2.GetK());
        }

    }

    long baseTime = 0;
    int answer = 3;
    @Override
    public void onDraw(Canvas canvas) {
        /*Bitmap _scratch = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);*/
        //Bitmap _scratch = AppManager.getInstance().getBitmap(R.mipmap.ic_launcher);
        canvas.drawColor(Color.BLACK);
       // canvas.drawBitmap(_scratch, 50, 50, null);
        //m_state.Render(canvas);
        if (stFlag == 1) {
           // System.out.println("START : " + timeFlag);
            if (timeFlag == 1) {
                baseTime = System.currentTimeMillis();
                timeFlag = 2;
               // System.out.println("this : " + timeFlag);
            } else {
                curTime = System.currentTimeMillis() - baseTime;
                //System.out.println("&^&^&CurTime - > " + curTime);
            }
            if ((timeFlag) == 2 && (curTime >= 5000)) {
                if (correctAns(answer, stPos)) {
                    if (stPos == n_Image.GetP()) {
                        n_Image2.SetPosition(-1, -1);
                    } else {
                        n_Image.SetPosition(-1, -1);
                    }
                } else {
                    n_Image2.SetPosition(-1, -1);
                    n_Image.SetPosition(-1, -1);
                }
                curTime = 0;
                timeFlag = 1;
                //System.out.println("TIMEFLAG : " + timeFlag);
            }
            m_Image.Draw(canvas);
            n_Image.Draw(canvas);
            n_Image2.Draw(canvas);
            if (timeFlag == 2) {
                timeText = String.format("%01d.%03d", curTime/1000, curTime%1000);

                Paint p = new Paint();
                p.setTextSize(50);
                p.setColor(Color.WHITE);
                canvas.drawText("남은 선택 시간 : " + String.valueOf(timeText) + "초", (int) (tWidth * 1/10), (int) (tHeight * 1/8), p);
            }
        }
        //m_spr.Draw(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_thread.setRunning(true);
        m_thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        m_thread.setRunning(false);
        while (retry) {
            try {
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public void Update() {
        n_Image.SetAns(3);
        n_Image2.SetAns(7);
       /* if (stFlag != 0) {
            if (m_Image.GetX() >= tWidth) {
                m_Image.SetPosition((int) (tWidth * 1/6), (int) (tHeight * 1/4));
            } else {
                m_Image.SetPosition(m_Image.GetX() + 3, m_Image.GetY());
            }
        }*/

        //System.out.println("Pos : " + m_Image.GetX());
        /*long GameTime = System.currentTimeMillis();
        m_spr.Update(GameTime);*/
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       return m_state.onKeyDown(keyCode, event);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (stFlag == 0) {
                stFlag = 1;
            } else if (stFlag == 1) {
                if (stPos == 1) {
                    m_Image.SetPosition((int) (tWidth * 3/6), (int) (tHeight * 1/4));
                    stPos = 0;
                } else if (stPos == 0){
                    m_Image.SetPosition((int) (tWidth * 3/6), (int) (tHeight * 3/4));
                    stPos = 1;
                }
            }
        }
        return true;
    }

   /* public void ChangeGameStsate(IState _state) {
        if (m_state != null) {
            m_state.Destroy();
        }
        _state.Init();
        m_state = _state;
    }*/
}
