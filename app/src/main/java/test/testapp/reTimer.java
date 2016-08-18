package test.testapp;

import java.util.TimerTask;

/**
 * Created by Administrator on 2016-08-08.
 */
public class reTimer extends TimerTask {
    int inTime = 5000;
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
                    m_GO[i].SetPosition(m_GO[i].getX() - (float)(m_gameview.tWidth * 0.00036),
                            m_GO[i].getY());
                }
            }
            inTime -= 5;
        }
    }

    public int getTime() {
        return inTime;
    }
}
