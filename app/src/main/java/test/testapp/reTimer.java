package test.testapp;

import java.util.TimerTask;

/**
 * Created by Administrator on 2016-08-08.
 */
public class reTimer extends TimerTask {
    int inTime = 3000;
    int eSpeed = 1;
    private GameView m_gameview;
    private GraphicObject [] m_GO;

    public reTimer(GameView gameview, GraphicObject [] GO) {
        m_gameview = gameview;
        m_GO = GO;
    }

    boolean isPause = false;
    public void onPause(boolean isPause) {
        this.isPause = isPause;
    }

    @Override
    public void run() {
        if (!isPause) {
            for (int i = 1; i <= 4; i++) {
                if (m_GO[i] != null) {
                    m_GO[i].SetPosition(m_GO[i].getX() - (float)(m_gameview.tWidth * 0.00053 * eSpeed),
                            m_GO[i].getY());
                }
            }
            if (m_GO[5] != null) {
                m_GO[5].SetPosition(m_GO[5].getX() - (float)(m_gameview.tWidth * 0.0002 * eSpeed),
                        m_GO[5].getY());
            }
            inTime -= 5;
        }
    }

    public void setSpeed(int mSpeed) {
        this.eSpeed = mSpeed;
        this.inTime = inTime / eSpeed;
    }

    public int getTime() {
        return inTime;
    }
}
