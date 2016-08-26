package test.testapp;

import java.util.TimerTask;

/**
 * 땅의 움직임 시간 동기화 관련 클래스
 * Created by Administrator on 2016-08-08.
 */
public class reTimer extends TimerTask {
    int inTime = 3000;  //기본 타이머 3초
    int eSpeed = 1;     //기본 속도
    private GameView m_gameview;
    private GraphicObject [] m_GO;  //땅 관련 이미지배열

    public reTimer(GameView gameview, GraphicObject [] GO) {
        m_gameview = gameview;
        m_GO = GO;
    }

    boolean isPause = false;    //일시정지 플래그
    public void onPause(boolean isPause) {
        this.isPause = isPause;
    }

    @Override
    public void run() {
        if (!isPause) { //일시정지상태가 아닐시 1~4번땅 이동
            for (int i = 1; i <= 4; i++) {
                if (m_GO[i] != null) {
                    m_GO[i].SetPosition(m_GO[i].getX() - (float)(m_gameview.tWidth * 0.00053 * eSpeed),
                            m_GO[i].getY());
                }
            }
            if (m_GO[5] != null) { //5번땅(가장 왼쪽에 표시되는 땅)은 더 느리게 이동
                m_GO[5].SetPosition(m_GO[5].getX() - (float)(m_gameview.tWidth * 0.0002 * eSpeed),
                        m_GO[5].getY());
            }
            inTime -= 5;
        }
    }

    //땅의 이동속도와 제한시간을 조절하기 위해 속도변수 설정
    public void setSpeed(int mSpeed) {
        this.eSpeed = mSpeed;
        this.inTime = inTime / eSpeed;
    }

    public int getTime() {
        return inTime;
    }
}
