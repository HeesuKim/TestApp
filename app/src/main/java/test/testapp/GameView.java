package test.testapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.Timer;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
    private GraphicObject [] gObject = new GraphicObject[10];
    private GraphicObject GO, BG;
    private calStage CS = new calStage();

    Random randNum = new Random();
    private int stFlag = 0;
    private int stPos = 1; //1 UP, 2 DOWN

    double tWidth, tHeight;
    private String timeText = "5.00";

    int curNum = mkRan(0, 9);
    int calNum = mkRan(1, 9);
    int preNum = 0;
    int ansPos = 0;
    int mSym = 1; // 1 + , 2 -

    private int tEndFlag = 0;
    private Timer inTimer;
    private reTimer mtim;
    private int stageNum = 1;
    private int waveCounter = 0;

    public void initStage() {
        gObject[5] = null;
        stFlag = 0;
        waveCounter = 0;
        BG = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.bgg3),
                0, 0, 11, 8);
        BG.SetPosition((int) (-BG.getG_wid() + tWidth), ((int)(tHeight - BG.getG_hei()))/2);

        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ba),
                (int) (tWidth * 1/6), (int) (tHeight * 1/4), 11, 7);
        gObject[0] = GO;
        //EARTH
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.en),
                (int) (tWidth * 3/6), (int) (tHeight * 1/4), 10, 1);
        gObject[1] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.en),
                (int) (tWidth * 3/6), (int) (tHeight * 3/4), 3, 2);
        gObject[2] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.en),
                (int) (tWidth * 5/6), (int) (tHeight * 1/4), 3, 3);
        gObject[3] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.en),
                (int) (tWidth * 5/6), (int) (tHeight * 3/4), 10, 4);
        gObject[4] = GO;

        preNum = CS.selStage(stageNum, curNum, calNum);
        if (preNum >= 50) {
            mSym = 2;
            preNum -= 50;
        } else {
            mSym = 1;
        }
        System.out.println("StartAnswer : " + preNum);

        int startRan = mkRan(1, 2);
        gObject[startRan].setBit(preNum);
        ansPos = startRan;
        if (startRan == 1) {
            gObject[2].setBit((preNum + 1)%10);
        } else {
            gObject[1].setBit(Math.abs(preNum - 1));
        }
    }

    public void setLength(int width, int height) {
        tWidth = width;
        tHeight = height;
        initStage();
    }

    public GameView(Context context) {
        super(context);
        setFocusable(true);
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());
        getHolder().addCallback(this);

        m_thread = new GameViewThread(getHolder(),this);

    }

    public int mkRan(int down, int up) {
        int cal =  up - down;
        int rst = Math.abs(randNum.nextInt() % (cal + 1));
        return (rst + down);
    }


    int pusF = 0;
    public void onPause() {
        /*try {
            synchronized (m_thread) {
                System.out.println("ThreadWait");
                m_thread.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //m_thread.setRunning(false);

        pusF = 1;
        if (m_thread != null && mtim != null) {
            m_thread.onPause(true);
            mtim.onPause(true);
        }
    }

    public void onResume() {
        //m_thread.setRunning(true);
        m_thread.onPause(false);
        mtim.onPause(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        GraphicObject vObj;
        if (stFlag == 1) {
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
                    waveCounter++;
                    System.out.println("ANSwaveCounter -> " + waveCounter);
                    timeText = "5.00";
                    tEndFlag = 0;
                    if (ansPos == stPos) {
                        System.out.println("COR!!");
                        if (stPos == 1) {
                            gObject[5] = gObject[1];
                        } else {
                            gObject[5] = gObject[2];
                        }
                        gObject[1] = gObject[3];
                        gObject[2] = gObject[4];

                        if (waveCounter >= 4) {
                            gObject[3] = null;
                            gObject[4] = null;
                            if (waveCounter == 5) {
                                stageNum++;
                                canvas.drawColor(Color.BLACK);
                                initStage();
                            }
                        } else {
                            gObject[3] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.en),
                                    (int) (tWidth * 5/6), (int) (tHeight * 1/4), 10, 3);
                            gObject[4] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.en),
                                    (int) (tWidth * 5/6), (int) (tHeight * 3/4), 10, 4);
                        }
                        curNum = preNum;
                        calNum = mkRan(1, 9);

                        preNum = CS.selStage(stageNum, curNum, calNum);
                        if (preNum >= 50) {
                            mSym = 2;
                            preNum -= 50;
                        } else {
                            mSym = 1;
                        }
                        System.out.println("ANSWER : " + preNum);

                        int selRan = mkRan(1, 2);
                        gObject[selRan].setBit(preNum);
                        if (selRan == 1) {
                            gObject[2].setBit((preNum + 1)%10);
                        } else {
                            gObject[1].setBit(Math.abs(preNum - 1));
                        }
                        ansPos = selRan;
                        /*mSym = mkRan(1, 2);
                        if (mSym == 1) {
                            preNum = (curNum + calNum) % 10;
                        } else {
                            if (curNum < calNum) {
                                preNum = (curNum + 10 - calNum) % 10;
                            } else {
                                preNum = (curNum - calNum) % 10;
                            }
                        }
                        answer = preNum%10;*/
                    } else {
                        System.out.println("WORRRR!!");
                        stageNum = 1;
                        canvas.drawColor(Color.BLACK);
                        initStage();
                    }
                } else {
                    timeText = String.format("%01d.%02d", mtim.getTime()/1000,
                            (mtim.getTime()%1000)/10);
                }
            }
            Paint p = new Paint();
            p.setTextSize(50);
            p.setColor(Color.WHITE);
            canvas.drawText("남은 선택 시간 : " + timeText + "초",
                    (int) (tWidth * 1/10), (int) (tHeight * 1/8), p);

            Paint p2 = new Paint();
            p2.setTextSize(50);
            p2.setColor(Color.WHITE);
            canvas.drawText("현재숫자 : " + curNum,
                    (int) (tWidth * 6/10), (int) (tHeight * 1/8), p2);

            Paint p3 = new Paint();
            p3.setTextSize(70);
            if (mSym == 1) {
                p3.setColor(Color.BLUE);
            } else {
                p3.setColor(Color.RED);
            }
            canvas.drawText("" + calNum,
                    gObject[0].getX(), gObject[0].getY(), p3);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("NONOT;CRRRRRRRRREATE");
        m_thread.setRunning(true);
        //m_thread.start();
        if (pusF == 0) {
            m_thread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       /* boolean retry = true;
        m_thread.setRunning(false);
        while (retry) {
            try {
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }*/
    }

    int moveBallF = 0;
    public void Update() {
        if (BG.getX() >= 0) {
            BG.SetPosition(0, BG.getY());
        } else {
            BG.SetPosition(BG.getX() + (float)(BG.getG_wid() * 0.0001), BG.getY());
        }
        System.out.println("TENS >> moveBallF/stPos : " + moveBallF + "/" + stPos);
        if (moveBallF == 1) {
           if (moveBall(stPos) == 1) {
               moveBallF = 0;
           }
        }

    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       return m_state.onKeyDown(keyCode, event);
    }*/

    public int moveBall(int curPos) {
        if (curPos == 1) {
            gObject[0].SetPosition(
                    gObject[0].getX(), gObject[0].getY() + (float)(tHeight * 0.30136)
            );
            if (gObject[0].getY() >= ((int) (tHeight * 3/4))) {
                stPos = 2;
                return 1;
            }
        } else {
            gObject[0].SetPosition(
                    gObject[0].getX(), gObject[0].getY() - (float)(tHeight * 0.30136)
            );
            if (gObject[0].getY() <= ((int) (tHeight * 1/4))) {
                stPos = 1;
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("TOUCHIN");
            if (stFlag == 0) {
                stFlag = 1;
            } else if (stFlag == 1) {
                moveBallF = 1;
                if (stPos == 1) {
                    /*gObject[0].SetPosition(
                            (int) (tWidth * 1/6), ((int) (tHeight * 3/4))-3);
                    stPos = 2;*/
                } else if (stPos == 2){
                    /*gObject[0].SetPosition(
                            (int) (tWidth * 1/6), ((int) (tHeight * 1/4))-3);
                    stPos = 1;*/
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
