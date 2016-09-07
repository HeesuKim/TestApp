package test.testapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;

/**
 * 게임뷰 관련 클래스, 메인 클래스
 * Created by Administrator on 2016-08-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
    private GraphicObject [] gObject = new GraphicObject[10];       //땅 관련 이미지
    private GraphicObject GO, BG, BG2, Num, Num2;                   //숫자, 배경 관련 이미지
    private GraphicObject [] caNum = new GraphicObject[3];          //계산숫자 이미지
    private SpriteAnimation ball;                                   //메인 공
    private GraphicObject leg;
    private GraphicObject [] caEgg = new GraphicObject [3];         //알
    private GraphicObject [] ansNum = new GraphicObject[2];         //선택지 숫자

    private calStage CS = new calStage();
    Random randNum = new Random();

    private int stFlag = 0; //게임 스타트 플래그
    private int stPos = 1;  //답 선택 위치 (1 UP, 2 DOWN)
    private int ballPos = 1;//공의 위치

    double tWidth, tHeight; //단말기별 가로길이 세로길이

    int curNum, calNum;                     //현재수, 메인계산숫자(아래의 calcNum[0]과 같은 값)
    private int [] calcNum = {31, 31, 31};  //난이도에 따라 추가되는 계산숫자(1, 2번 배열)
    int preNum = 0;                         //이전 계산 결과
    int ansPos = 0;                         //답 선택 위치

    //타이머 관련 변수
    private int tEndFlag = 0;               //땅 이동 및 선택시간 타이머 플래그(0 : 실행중, 1 : 정지)
    private Timer inTimer;
    private reTimer mtim;

    private int stageNum = 1;               //스테이지 번호
    private int waveCounter = 0;            //웨이브 개수 표현

    int selFlag = 0;                        //땅 선택에 따른 위치 플래그

    int ansArr [] = new int[2];             //기존 계산수와 메인계산숫자 배열
    int speed = 1;                          //땅, 타이머 속도변수

    private String[] stageInfo;             //스테이지 설명문 String 배열
    Paint pnt, pathPnt;                     //텍스트와 선의 설정정보를 위한 paint
    Path path;                              //메인공과 기존계산수 연결을 위한 선 관련 path

    private static MediaPlayer bgm;         //BGM 변수
    int bgmStop = 0;                        //BGM 정지 관련 플래그

    int pusF = 0;                           //일시정지, 재개 관련 플래그

    String finStr;                          //무한모드 관련 string

    int moveBallF = 0;                      //공의 움직임 상태 관련 플래그
    long gameTime;                          //애니메이션 적용을 위한 현재 게임시간 표시 변수

    public GameView(Context context) {
        super(context);
        setFocusable(true);
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());
        getHolder().addCallback(this);
        m_thread = new GameViewThread(getHolder(),this);

        //터치효과를 위한 Sound설정
        SoundManager.getInstance().Init(context);
        SoundManager.getInstance().addSound(2, R.raw.touch);
        //BGM을 위한 MediaPlayer설정
        bgm = MediaPlayer.create(context, R.raw.sans);
        bgm.setLooping(true);   //BGM이 끝까지 재생되면 처음부터 다시 시작
    }

    //게임이 처음 시작되었을 때 단말기의 가로세로길이를 받아오고 첫스테이지 초기설정
    public void setLength(int width, int height) {
        tWidth = width;
        tHeight = height;
        initStage();
    }

    //down ~ up 사이의 수 중에서 하나의 숫자를 무작위로 반환
    public int mkRan(int down, int up) {
        int cal =  up - down;
        int rst = Math.abs(randNum.nextInt() % (cal + 1));
        return (rst + down);
    }

    //스테이지 초기설정
    public void initStage() {
        //BGM이 실행중이라면 정지하고 플래그 설정
        if (bgm.isPlaying()) {
            bgm.stop();
            bgmStop = 1;
        }

        curNum = mkRan(0, 9);       //처음 시작 수 설정
        preNum = 0;
        ansPos = 0;
        tEndFlag = 0;
        ballPos = 1;                //공 기본 위치
        stPos = 1;                  //땅 선택 기본 위치
        gObject[5] = null;          //땅 null로 초기화
        stFlag = 0;                 //게임상태 플래그 (0 : stage설명상태, 1 : 게임중상태)
        waveCounter = 0;

        //INFO(Num : 기존계산수(새에 표시), Num2 : 메인EGG 위에 표시될 수)
        Num = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0),
                (int) (tWidth * 1/13), (int) (tHeight * 5/10));
        Num2 = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0),
                (int) (tWidth * 4/10), (int) (tHeight * 1/8));

        //BACKGROUND(같은 배경 두개를 이어붙임)
        BG = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.background1),
                0, 0);
        BG.aboutBG((int) tWidth, (int) tHeight);
        BG2 = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.background2),
                0, 0);
        BG2.aboutBG((int) tWidth, (int) tHeight);
        //BG.SetPosition((int) (BG.getG_wid() + tWidth), ((int)(tHeight - BG.getG_hei()))/2);

        //BIRD & LEG
        ball = new SpriteAnimation(AppManager.getInstance().getBitmap(R.drawable.ball_ani));
        ball.InitSpriteData((int) ball.getG_wid(), (int) ball.getG_hei(), 50, 8);
        ball.SetPosition((int) (tWidth * 1/6), (int) (tHeight * 1/4) - (int) (ball.getG_hei()*1/3));
        leg = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.leg), 0, 0);


        //EGG & INFOS (추가되는 공들과 그 공들 위에 표시될 수)
        for (int i = 0; i < 3; i++) {
            caEgg[i] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.egg), 0, 0);

            /*caEgg[i] = new SpriteAnimation(AppManager.getInstance().getBitmap(R.drawable.ball_ani));
            caEgg[i].InitSpriteData((int) ball.getG_wid(), (int) ball.getG_hei(), 60, 6);*/
            caNum[i] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0), 0, 0);
        }
        //추가 공들의 위치는 메인공의 왼쪽과 오른쪽
        caEgg[1].SetPosition((int) (caEgg[0].getX() - caEgg[0].getG_wid()),
                (int) (caEgg[0].getY()));
        caEgg[2].SetPosition((int) (caEgg[0].getX() + caEgg[0].getG_wid()),
                (int) (caEgg[0].getY()));

        //EARTH (1~4 : 오른쪽에서 다가오는 땅, 5 : 선택되어 공의 받침이 될 땅)
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

        //EARTH HIGHTLIGHT (선택된 땅을 표시하기 위한 노란색 커버)
        GO = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_sele),
                (int) (tWidth * 5/6), (int) (tHeight * 3/4));
        gObject[6] = GO;

        //둥지에 표시될 선택지 숫자
        ansNum[0] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0), 0, 0);
        ansNum[1] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.num_0), 0, 0);

        //calStage에서 계산결과배열을 받아 게임뷰의 각 요소들에 복사
        ansArr = CS.selStage(stageNum, curNum);
        preNum = ansArr[0]; //기존수 업데이트
        calNum = ansArr[1]; //메인공 계산수 업데이트
        for (int i = 0; i < 3; i++) {   //추가되는 공 계산수 업데이트
            calcNum[i] = ansArr[i+1];
        }

        int startRan = mkRan(0, 1);
        ansNum[startRan].setNum(preNum);
        ansPos = startRan + 1;
        if (startRan == 0) {
            ansNum[1].setNum((preNum + 1)%10);
        } else {
            ansNum[0].setNum(Math.abs(preNum - 1));
        }
        /*int startRan = mkRan(1, 2);         //위 아래를 랜덤으로 선택하여 답 위치 결정
        gObject[startRan].setBit(preNum);   //답 위치의 땅에 숫자 그리기
        ansPos = startRan;
        if (startRan == 1) {                //답 위치의 반대 땅에 답과 1 차이나는 숫자 그리기
            gObject[2].setBit((preNum + 1)%10);
        } else {
            gObject[1].setBit(Math.abs(preNum - 1));
        }*/

        //스테이지 설명 관련 paint설정
        pnt = new Paint();
        stageInfo = CS.stageInfo(stageNum);
        pnt.setAntiAlias(true);
        pnt.setColor(Color.WHITE);
        pnt.setTextSize(50);

       /* //기존수와 메인공 잇는 선 관련 paint설정
        path = new Path();
        pathPnt = new Paint();
        pathPnt.setAntiAlias(true);
        pathPnt.setStrokeWidth(20);
        pathPnt.setColor(Color.WHITE);
        pathPnt.setStyle(Paint.Style.STROKE);*/
    }

    //일시정지 요청
    public void onPause() {
        pusF = 1;
        //쓰레드와 땅,제한시간 타이머를 체크하여 각각 일시정지 요청)
        if (m_thread != null && mtim != null) {
            m_thread.onPause(true);
            mtim.onPause(true);
            if (bgm.isPlaying()) {  //BGM이 재생중일때만 BGM 일시정지 요청
                bgm.pause();
            }
        }
    }

    //일시정지 후 재개 요청
    public void onResume() {
        m_thread.onPause(false);
        mtim.onPause(false);
        //게임이 실행중인 상태에서 일시정지가 되었을 때만 BGM 재개 요청
        if (pusF == 1 && stFlag == 1) {
            bgm.start();
        }
    }

    /*공을 움직이는 메소드
    * 현재 위치를 받아와 반대방향으로 옮김
    * 일정 위치 이상으로 좌표가 이동하면 멈추는 형식으로
    * 이동의 시작과 끝을 구현
    * return값이 1이 되면 이동이 완료되었음을 의미
    * 0일때는 계속 이동시킴*/
    public int moveBall(int curPos) {
        if (curPos == 1) {
            if (ball.getY() + (float)(tHeight * 0.02)
                    >= (int) (tHeight * 3/5) - (int) (ball.getG_hei()*1/3)) {
                ball.SetPosition(
                        ball.getX(), (int) (tHeight * 3/5) - (int) (ball.getG_hei()*1/3));
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

    //선택 땅을 하이라이트(노란색)로 표시하기 위한 메소드
    public void selEarth(int curPos) {
        if (selFlag == 0) {
            gObject[6].SetPosition(gObject[curPos].getX(), gObject[curPos].getY());
            selFlag = 1;
        } else {
            gObject[6].SetPosition(gObject[curPos].getX(), gObject[curPos].getY());
            selFlag = 0;
        }
    }

    //쓰레드 실행중 계속 같이 돌아가는 그리기 함수
    @Override
    public void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK); //이미지 잔상효과를 없애기 위해 검은색으로 지워주고 시작
            if (stFlag == 0) {  //스테이지 설명 상태 (게임시작직전)
                if (stageNum >= 9) {    //무한모드 진입시 표시될 스테이지 설명
                    finStr = "STAGE " + stageNum;
                    pnt.setColor(Color.WHITE);
                    canvas.drawText(finStr,
                            (int) (tWidth * 2/11), (int) (tHeight * 4/10), pnt);
                    finStr = "더이상 난이도 상승이 없는 무한모드입니다.";
                    pnt.setColor(Color.RED);
                    canvas.drawText(finStr,
                            (int) (tWidth * 2/11), (int) (tHeight * 5/10), pnt);
                } else {    //9스테이지(무한모드) 이전의 스테이지 설명
                    for (int i = 0; i < 5; i++) {
                        if (stageInfo[i] != null) {
                            if (i == 4) {   //"추가계산" 텍스트 노란색으로 강조
                                pnt.setColor(Color.YELLOW);
                            } else {
                                pnt.setColor(Color.WHITE);
                            }
                            //한 줄씩 출력
                            canvas.drawText(stageInfo[i],
                                    (int) (tWidth * 2/11), (int) (tHeight * (4 + i)/10), pnt);
                        }
                    }
                }
            }
            GraphicObject vObj;
            if (stFlag == 1) { //스테이지 설명 후 게임 진행중 상태
                BG.DrawRe(canvas);        //배경
                BG2.DrawRe(canvas);        //배경
                //기존수와 메인공 연결선 path정보 설정부분
                /*path.reset();   //path정보 초기화
                path.moveTo((float) (Num.getX() + (Num.getG_wid())),
                        (float) (Num.getY()+ (Num.getG_hei()/2)));  //path시작위치
                path.quadTo((float) (tWidth * 3/13), (float) (Num.getY()+ (Num.getG_hei()/2)),
                        (float) (ball.getX() + (ball.getG_wid()/12)),
                        (float) (ball.getY() + (ball.getG_hei()/2)));   //path경로설정
                canvas.drawPath(path, pathPnt); //설정한 path대로 선 그리기*/

                ball.Draw(canvas);  //새 그리기
                leg.SetPosition((float) (ball.getX() + (((ball.getG_wid()/8) - leg.getG_wid())/2)),
                        (float) (ball.getY() + ball.getG_hei() - 5));   //발 위치 조정
                /*System.out.println("TSMBIRD : " + ball.getG_wid()/8 + " LEG : " + leg.getG_wid());*/

                // 알 그리기
                caEgg[0].SetPosition(
                        (float) (ball.getX() + ((ball.getG_wid()/8 - caEgg[0].getG_wid())/2)),
                        (float) (leg.getY() + leg.getG_hei() - 5));
                caEgg[0].Draw(canvas);
                if (stageNum >= 5) {    //스테이지 5이상시 추가계산공 1개 그리기
                    caEgg[1].SetPosition((int) (caEgg[0].getX() - caEgg[0].getG_wid()),
                            (int) (caEgg[0].getY()));
                    caEgg[1].Draw(canvas);
                }
                if (stageNum >= 7) {    //스테이지 7이상시 추가계산공 2개 그리기
                    caEgg[2].SetPosition((int) (caEgg[0].getX() + caEgg[0].getG_wid()),
                            (int) (caEgg[0].getY()));
                    caEgg[2].Draw(canvas);
                }

                //발 그리기
                leg.Draw(canvas);

                //땅 그리기
                for (int i = 1; i < 10; i++) {
                    vObj = gObject[i];
                    if (vObj != null){
                        vObj.Draw(canvas);
                    }
                }

                //선택할 둥지 숫자 그리기
                ansNum[0].SetPosition(
                        gObject[1].getX() + (float) (gObject[1].getG_wid()/2 - ansNum[0].getG_wid()/2),
                        gObject[1].getY());
                ansNum[0].Draw(canvas);
                ansNum[1].SetPosition(
                        gObject[2].getX() + (float) (gObject[2].getG_wid()/2 - ansNum[1].getG_wid()/2),
                        gObject[2].getY());
                ansNum[1].Draw(canvas);

                //땅 이동, 시간제한 관련 타이머 부분
                if (tEndFlag == 0) {    //타이머 시작(기존 타이머가 0이 되었거나 새로 시작할 때)
                    tEndFlag = 1;   //타이머가 동작하고 있음을 플래그로 표시
                    inTimer = new Timer();
                    mtim = new reTimer(this, gObject);
                    if (stageNum >= 3) { //스테이지 3부터 2배속
                        mtim.setSpeed(2);
                        speed = 2;
                    }
                    inTimer.schedule(mtim, 0, 5); //5ms마다 지정된 타이머task 수행
                }
                if (tEndFlag == 1) {    //타이머 동작중
                    if (mtim.getTime() <= 0) {  //시간제한 끝
                        inTimer.cancel();   //타이머를 멈추고
                        waveCounter++;      //웨이브 개수를 한개 증가
                        tEndFlag = 0;       //새롭게 타이머를 시작하기 위해 기존 타이머가 0이 되었음을 표시

                        if (ansPos == stPos) {  //정답일경우
                            if (ballPos != stPos) { //공의 현재위치가 답의 위치와 다를 때 공 이동설정
                                moveBallF = 1;
                            }
                            /*gObject[5] = gObject[stPos];    //5번 땅에 선택된 땅 복사
                            gObject[5].setBit(10);          //5번 땅의 숫자표시를 삭제*/
                            gObject[1] = gObject[3];        //뒤에 있던 땅 정보들 앞 땅으로 복사
                            gObject[2] = gObject[4];

                            if (waveCounter >= ((10 * speed) - 1)) {
                                //스테이지의 마지막 웨이브가 끝나기 직전에
                                //더이상 새로운 땅이 생성되지 않도록 함
                                gObject[3] = null;
                                gObject[4] = null;
                                if (waveCounter == (10 * speed)) { //스테이지 웨이브 모두 통과시
                                    stageNum++;                     //스테이지 번호를 하나 증가하고
                                    canvas.drawColor(Color.BLACK);  //화면 검게 초기화
                                    initStage();
                                }
                            } else {    //웨이브 진행될 때 새로운 땅 생성하는 부분
                                gObject[3] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                                        (int) (tWidth * 5/6), (int) (tHeight * 1/4));
                                gObject[4] = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.earth_base),
                                        (int) (tWidth * 5/6), (int) (tHeight * 3/4));
                            }

                            //계산된 숫자 화면표시 관련 부분 (Init부분과 동일)
                            curNum = preNum;
                            ansArr = CS.selStage(stageNum, curNum);
                            preNum = ansArr[0];
                            calNum = ansArr[1];
                            for (int i = 0; i< 3; i++) {
                                calcNum[i] = ansArr[i+1];
                            }


                            int selRan = mkRan(0, 1);
                            ansNum[selRan].setNum(preNum);
                            ansPos = selRan;
                            if (selRan == 0) {
                                ansNum[1].setNum((preNum + 1)%10);
                            } else {
                                ansNum[0].setNum(Math.abs(preNum - 1));
                            }

                            /*int selRan = mkRan(1, 2);
                            gObject[selRan].setBit(preNum);
                            if (selRan == 1) {
                                gObject[2].setBit((preNum + 1)%10);
                            } else {
                                gObject[1].setBit(Math.abs(preNum - 1));
                            }*/
                            ansPos = selRan + 1;    //답의 위치를 정함
                        } else { //오답시 스테이지 1부터 재시작
                            stageNum = 1;
                            canvas.drawColor(Color.BLACK);
                            initStage();
                        }
                    }
                }
                Num.setNum(curNum);     //기존 수 이미지 업데이트 후 그리기
                //새의 위치정보를 받아와 그 위에 그림
                Num.SetPosition((float) (ball.getX() + ((ball.getG_wid()/16 - Num.getG_wid()*4/9))),
                        (float) (ball.getY() + (ball.getG_hei()/2 - Num.getG_hei()/5)));
                Num.Draw(canvas);

                /*Num2.setNum(calNum);    //메인공에 표시될 계산 수 이미지 업데이트 후 그리기
                Num2.SetPosition(ball.getX(), ball.getY()); //공의 위치정보를 받아와 그 위에 그림
                Num2.Draw(canvas);*/

                //알 위에 표시될 숫자 그리기
                caNum[0].setNum(calNum);
                caNum[0].SetPosition(
                        (float) (caEgg[0].getX() + (caEgg[0].getG_wid()/2 - caNum[0].getG_wid()*6/13)),
                        (float) (caEgg[0].getY() + (caEgg[0].getG_hei()/2 - caNum[0].getG_hei()/2)));
                caNum[0].Draw(canvas);

                if (stageNum >= 5) {    //5스테이지 이상부터 추가 공 위에 표시될 숫자
                    caNum[1].setNum(calcNum[1]);
                    caNum[1].SetPosition(
                            (float) (caEgg[1].getX() + (caEgg[1].getG_wid()/2 - caNum[1].getG_wid()*6/13)),
                            (float) (caEgg[1].getY() + (caEgg[1].getG_hei()/2 - caNum[1].getG_hei()/2)));
                    caNum[1].Draw(canvas);
                }
                if (stageNum >= 7) {    //7스테이지 이상부터 추가 공 위에 표시될 숫자
                    caNum[2].setNum(calcNum[2]);
                    caNum[2].SetPosition(
                            (float) (caEgg[2].getX() + (caEgg[2].getG_wid()/2 - caNum[2].getG_wid()*6/13)),
                            (float) (caEgg[2].getY() + (caEgg[2].getG_hei()/2 - caNum[2].getG_hei()/2)));
                    caNum[2].Draw(canvas);
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    //서피스 뷰 시작시 쓰레드 동작
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_thread.setRunning(true);
        if (pusF == 0) {
            m_thread.start();
        }
    }

    //서피스 뷰에서 포커스가 사라지면 무조건 발동되어 우선 주석처리해놓음
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


    int bgFlag = 0;
    //onDraw외에 지속적으로 쓰레드의 구동과 함께 실행되어야 할 메소드들 포함
    public void Update() {
        //뒷 배경이미지 이동, 두장을 이어붙여 연속성 표현
        BG.SetPosition(BG.getX() - (float)(tWidth * 0.001), BG.getY());
        BG2.SetPosition(BG2.getX() - (float)(tWidth * 0.001), BG2.getY());

        if (BG.getX() <= 0 && bgFlag == 0) {
            BG2.SetPosition((int) tWidth - 10, BG2.getY());
            bgFlag = 1;
        }
        if (BG2.getX() <= 0 && bgFlag == 1) {
            BG.SetPosition((int) tWidth - 10, BG.getY());
            bgFlag = 0;
        }




        /*if (BG.getX() >= tWidth) {
            BG.SetPosition(BG.getX() - (int) tWidth * 2, BG.getY());
        }
        if (BG.getX() >= 0) {
            BG2.SetPosition((int) (tWidth - BG.getX()), BG2.getY());
        } else {
            BG2.SetPosition(-((int) (tWidth + BG.getX())), BG2.getY());
        }*/
        //공이 굴러가는 애니메이션 표현을 위해 현재 게임시간을 넘김
        gameTime = System.currentTimeMillis();
        ball.Update(gameTime);
        /*caEgg[0].Update(gameTime);
        caEgg[1].Update(gameTime);*/

        if (moveBallF == 1) {   //공이 움직여야 하는 상태
           if (moveBall(ballPos) == 1) {    //공을 움직임이 완료되면
               moveBallF = 0;   //공이 움직이지 않아도 되는 상태로 전환
           }
        }
        selEarth(stPos);
    }

    //화면에 터치가 입력되었을 경우 처리하는 메소드
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (stFlag == 0) {  //게임 준비상태(스테이지 정보표시)에서 터치 입력시
                if (bgmStop == 1) { //BGM이 일시정지된 상태에서 재개를 위한 설정
                    try {
                        bgm.prepare();
                        if (stageNum >= 5) {    //5스테이지 이상은 BGM의 뒷부분부터 시작
                            bgm.seekTo(40000);
                        } else {                //4스테이지까지는 BGM의 처음부터 시작
                            bgm.seekTo(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                bgm.start();    //게임 시작과 함께 BGM시작
                stFlag = 1;     //게임 시작
            } else if (stFlag == 1) {   //게임이 실행중인 상태에서 터치 입력시
                SoundManager.getInstance().play(2); //터치효과음 재생
                if (stPos == 1) {   //현재 선택 위치를 반대쪽으로 변경
                    stPos = 2;
                } else if (stPos == 2){
                   stPos = 1;
                }
            }
        }
        return true;
    }
}
