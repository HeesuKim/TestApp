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
    private GraphicObject GO, BG, BG2, Num, Num2;
    private SpriteAnimation ball;
    private calStage CS = new calStage();

    Random randNum = new Random();
    private int stFlag = 0;
    private int stPos = 1; //1 UP, 2 DOWN
    private int ballPos = 1;

    double tWidth, tHeight;
    private String timeText = "2.00";

    int curNum, calNum;
    int preNum = 0;
    int ansPos = 0;

    private int tEndFlag = 0;
    private Timer inTimer;
    private reTimer mtim;
    private int stageNum = 1;
    private int waveCounter = 0;

    int selFlag = 0;

    int ansArr [] = new int[2];
    int speed = 1;


    String str;
    Paint pnt;

    public void initStage() {
        curNum = mkRan(0, 9);
        preNum = 0;
        ansPos = 0;
        tEndFlag = 0;
        ballPos = 1;
        stPos = 1;
        gObject[5] = null;
        stFlag = 0;
        waveCounter = 0;

        //INFO
        Num = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0),
                (int) (tWidth * 1/11), (int) (tHeight * 5/10));
        Num2 = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0),
                (int) (tWidth * 4/10), (int) (tHeight * 1/8));

        //BACKGROUND
        BG = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.background),
                0, 0);
        BG2 = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.background),
                0, 0);
        BG.SetPosition((int) (-BG.getG_wid() + tWidth), ((int)(tHeight - BG.getG_hei()))/2);

        //BALL
        ball = new SpriteAnimation(AppManager.getInstance().getBitmap(R.drawable.ball_ani));
        ball.InitSpriteData((int) ball.getG_wid(), (int) ball.getG_hei(), 60, 6);
        ball.SetPosition((int) (tWidth * 1/6), (int) (tHeight * 1/4) - (int) (ball.getG_hei()*1/3));
        //EARTH
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                (int) (tWidth * 3/6), (int) (tHeight * 1/4));
        gObject[1] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                (int) (tWidth * 3/6), (int) (tHeight * 3/4));
        gObject[2] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                (int) (tWidth * 5/6), (int) (tHeight * 1/4));
        gObject[3] = GO;
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                (int) (tWidth * 5/6), (int) (tHeight * 3/4));
        gObject[4] = GO;

        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_sel),
                (int) (tWidth * 5/6), (int) (tHeight * 3/4));
        gObject[6] = GO;

        ansArr = CS.selStage(stageNum, curNum);
        preNum = ansArr[0];
        calNum = ansArr[1];
        System.out.println("NTPSStartAnswer : " + preNum);

        int startRan = mkRan(1, 2);
        gObject[startRan].setBit(preNum);
        ansPos = startRan;
        if (startRan == 1) {
            gObject[2].setBit((preNum + 1)%10);
        } else {
            gObject[1].setBit(Math.abs(preNum - 1));
        }
        pnt = new Paint();
        str = CS.stageInfo(stageNum);
        pnt.setAntiAlias(true);
        pnt.setColor(Color.WHITE);
        pnt.setTextSize(50);
        //canvas.drawText(str, (int) (tWidth * 1/11), (int) (tHeight * 5/10), pnt);
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
        pusF = 1;
        if (m_thread != null && mtim != null) {
            m_thread.onPause(true);
            mtim.onPause(true);
        }
    }

    public void onResume() {
        m_thread.onPause(false);
        mtim.onPause(false);
    }


    @Override
    public void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            if (stFlag == 0) {
                canvas.drawText(str, (int) (tWidth * 2/11), (int) (tHeight * 5/10), pnt);
            }
            GraphicObject vObj;
            if (stFlag == 1) {
                BG.Draw(canvas);
                ball.Draw(canvas);
                for (int i = 1; i < 10; i++) {
                    vObj = gObject[i];
                    if (vObj != null){
                        vObj.Draw(canvas);
                    }
                }

                if (tEndFlag == 0) {
                    tEndFlag = 1;
                    inTimer = new Timer();
                    mtim = new reTimer(this, gObject);
                    if (stageNum >= 5) {
                        mtim.setSpeed(2);
                        speed = 2;
                    }
                    inTimer.schedule(mtim, 0, 5);
                }
                if (tEndFlag == 1) {
                    if (mtim.getTime() <= 0) {
                        inTimer.cancel();
                        waveCounter++;
                        System.out.println("ANSwaveCounter -> " + waveCounter);
                        //timeText = "2.00";
                        tEndFlag = 0;
                        if (ansPos == stPos) {
                            System.out.println("COR!!");
                            if (ballPos != stPos) {
                                moveBallF = 1;
                            }
                            gObject[5] = gObject[stPos];
                            gObject[5].setBit(10);
                            gObject[1] = gObject[3];
                            gObject[2] = gObject[4];

                            if (waveCounter >= ((10 * speed) - 1)) {
                                gObject[3] = null;
                                gObject[4] = null;
                                if (waveCounter == (10 * speed)) {
                                    stageNum++;
                                    canvas.drawColor(Color.BLACK);
                                    initStage();
                                }
                            } else {
                                gObject[3] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                                        (int) (tWidth * 5/6), (int) (tHeight * 1/4));
                                gObject[4] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                                        (int) (tWidth * 5/6), (int) (tHeight * 3/4));
                            }
                            curNum = preNum;

                            ansArr = CS.selStage(stageNum, curNum);
                            preNum = ansArr[0];
                            calNum = ansArr[1];
                            System.out.println("NTPSANSWER : " + preNum);

                            int selRan = mkRan(1, 2);
                            gObject[selRan].setBit(preNum);
                            if (selRan == 1) {
                                gObject[2].setBit((preNum + 1)%10);
                            } else {
                                gObject[1].setBit(Math.abs(preNum - 1));
                            }
                            ansPos = selRan;
                        } else {
                            System.out.println("WORRRR!!");
                            stageNum = 1;
                            canvas.drawColor(Color.BLACK);

                            initStage();
                        }
                    }/* else {
                        timeText = String.format("%01d.%02d", mtim.getTime()/1000,
                                (mtim.getTime()%1000)/10);
                    }*/
                }
                Num.setNum(curNum);
                Num.Draw(canvas);

                Num2.setNum(calNum);
                Num2.SetPosition(ball.getX(), ball.getY());
                Num2.Draw(canvas);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_thread.setRunning(true);
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
    long gameTime;
    public void Update() {
        BG.SetPosition(BG.getX() + (float)(BG.getG_wid() * 0.008), BG.getY());
        if (BG.getX() >= tWidth) {
            BG.SetPosition(BG.getX() - (int) tWidth * 2, BG.getY());
        }
        if (BG.getX() >= 0) {
            BG2.SetPosition((int) (tWidth - BG.getX()), BG2.getY());
        } else {
            BG2.SetPosition(-((int) (tWidth + BG.getX())), BG2.getY());
        }

        gameTime = System.currentTimeMillis();
        ball.Update(gameTime);

        if (moveBallF == 1) {
           if (moveBall(ballPos) == 1) {
               moveBallF = 0;
           }
        }

        selEarth(stPos);

    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       return m_state.onKeyDown(keyCode, event);
    }*/

    public int moveBall(int curPos) {
        if (curPos == 1) {
            if (ball.getY() + (float)(tHeight * 0.02)
                    >= (int) (tHeight * 3/4) - (int) (ball.getG_hei()*1/3)) {
                ball.SetPosition(
                        ball.getX(), (int) (tHeight * 3/4) - (int) (ball.getG_hei()*1/3));
                ballPos = 2;
                return 1;
            } else {
                ball.SetPosition(
                        ball.getX(), ball.getY() + (float)(tHeight * 0.02));
            }
        } else {
            if (ball.getY() + (float)(tHeight * 0.02)
                    <= (int) (tHeight * 1/4) - (int) (ball.getG_hei()*1/3)) {
                ball.SetPosition(
                        ball.getX(), (int) (tHeight * 1/4) - (int) (ball.getG_hei()*1/3));
                ballPos = 1;
                return 1;
            } else {
                ball.SetPosition(
                        ball.getX(), ball.getY() - (float)(tHeight * 0.02));
            }
        }
        return 0;
    }

    public void selEarth(int curPos) {
        if (selFlag == 0) {
            gObject[6].SetPosition(gObject[curPos].getX(), gObject[curPos].getY());
            selFlag = 1;
        } else {
            gObject[6].SetPosition(gObject[curPos].getX(), gObject[curPos].getY());
            selFlag = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (stFlag == 0) {
                stFlag = 1;
            } else if (stFlag == 1) {
                if (stPos == 1) {
                   /* ball.SetPosition(
                            (int) (tWidth * 1/6), (int) (tHeight * 3/4) - (int) (ball.getG_hei()*1/3));*/
                    stPos = 2;
                } else if (stPos == 2){
                    /*ball.SetPosition(
                            (int) (tWidth * 1/6), (int) (tHeight * 1/4) - (int) (ball.getG_hei()*1/3));*/
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
