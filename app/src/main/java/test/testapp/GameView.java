package test.testapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;
import java.util.Vector;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
    private GraphicObject [] gObject = new GraphicObject[10];
    private GraphicObject GO, BG;

    private int stFlag = 0;
    private int stPos = 1;

    double tWidth, tHeight;
    private String timeText = "5.00";

    public GameView(Context context, int width, int height) {
        super(context);
        setFocusable(true);
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());
        getHolder().addCallback(this);
        tWidth = width;
        tHeight = height;

        System.out.println("@@@@tWidth = " + tWidth + " // tHeight = " + tHeight);

        m_thread = new GameViewThread(getHolder(),this);

        //BG
        BG = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.bgg3),
                0, 0, 11, 8);
        BG.SetPosition((int) (-BG.getG_wid() + tWidth), ((int)(tHeight - BG.getG_hei()))/2);

        //System.out.println("@@@@idth = " + BG.getG_wid() + " // tHeight = " + BG.getG_hei());
        //System.out.println("$$r3r23$$$$tWidth = " + BG.getG_wid() + " // tHeight = " + BG.getG_hei());
        //BALL
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ba),
                (int) (tWidth * 1/6), (int) (tHeight * 1/4), 11, 7);
        gObject[0] = GO;
        //EARTH
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.e3),
                (int) (tWidth * 3/6), (int) (tHeight * 1/4), 3, 1);
        gObject[1] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.e3),
                (int) (tWidth * 3/6), (int) (tHeight * 3/4), 7, 2);
        gObject[2] = GO;
    }

    public boolean correctAns(int cAns, int posNum) {
        System.out.println("cAns , posNum -> " + cAns + ", " + posNum);
        if (posNum == gObject[1].getPos()) {
            return (cAns == gObject[1].getAns());
        } else {
            return (cAns == gObject[2].getAns());
        }

    }

    private int tEndFlag = 0;
    private Timer inTimer;
    private reTimer mtim;

    int answer = 3;


    int tCounter = 0;
    long tTimer;
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        GraphicObject vObj;
        if (stFlag == 1) {
            if(tCounter == 0) {
                tTimer = System.currentTimeMillis();
            }
            tCounter++;
            System.out.println("tCounter val = " + tCounter);
            if(tCounter == 60) {
                long dTimer = System.currentTimeMillis() - tTimer;
                System.out.println("출력값 : " + dTimer);
            }

            BG.Draw(canvas);
            for (int i = 0; i < 10; i++) {
                vObj = gObject[i];
                if (vObj != null){
                    vObj.Draw(canvas);
                }
            }

            if (tEndFlag == 0) {
                tEndFlag = 1;
                inTimer = new Timer();
                mtim = new reTimer(this, gObject);
                inTimer.schedule(mtim, 0, 5);
            }
            if (tEndFlag == 1) {
                if (mtim.getTime() <= 0) {
                    inTimer.cancel();
                    timeText = "5.00";
                    tEndFlag = 0;
                    if (correctAns(answer, stPos)) {
                        System.out.println("COR!!");
                        if (stPos == gObject[1].getPos()) {
                            gObject[2] = null;
                        } else {
                            gObject[1] = null;
                        }
                    } else {
                        System.out.println("WORRRR!!");
                        gObject[1] = null;
                        gObject[2] = null;
                    }
                } else {
                    timeText = String.format("%01d.%02d", mtim.getTime()/1000,
                            (mtim.getTime()%1000)/10);
                }
            }
            Paint p = new Paint();
            p.setTextSize(50);
            p.setColor(Color.BLACK);
            canvas.drawText("남은 선택 시간 : " + timeText + "초",
                    (int) (tWidth * 1/10), (int) (tHeight * 1/8), p);
        }
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
        if (BG.getX() >= 0) {
            BG.SetPosition(0, BG.getY());
        } else {
            BG.SetPosition(BG.getX() + (float)(BG.getG_wid() * 0.0001), BG.getY());
        }

        /*if(stFlag == 1 && tEndFlag == 1){
            if (mtim.getTime() >= 0) {
                gObject[1].SetPosition(gObject[1].getX() - (float)(tWidth * 0.0025),
                        gObject[1].getY());
                gObject[2].SetPosition(gObject[2].getX() - (float)(tWidth * 0.0025),
                        gObject[2].getY());
            }
        }*/
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
                    gObject[0].SetPosition(
                            (int) (tWidth * 1/6), ((int) (tHeight * 3/4))-3);
                    stPos = 2;
                } else if (stPos == 2){
                    gObject[0].SetPosition(
                            (int) (tWidth * 1/6), ((int) (tHeight * 1/4))-3);
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
