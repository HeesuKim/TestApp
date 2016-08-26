package test.testapp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * 게임뷰쓰레드 관련 클래스
 * Created by Administrator on 2016-08-03.
 */
public class GameViewThread extends Thread {
    private SurfaceHolder m_surfaceHolder;
    private GameView m_gameview;
    private boolean m_run = false;  //쓰레드 실행 컨트롤
    private boolean isWait = false; //쓰레드 일시정지 컨트롤

    public GameViewThread(SurfaceHolder surfaceHolder, GameView gameview) {
        m_surfaceHolder = surfaceHolder;
        m_gameview = gameview;
    }

    public void setRunning(boolean run) {
        m_run = run;
    }

    //쓰레드 일시정지, 재개 요청
    public void onPause(boolean isWait) {
        synchronized (this) {
            this.isWait = isWait;
            notify();
        }
    }

    @Override
    public void run() {
        Canvas _canvas;
        while (m_run) {
            _canvas = null;
            try {
                m_gameview.Update(); //게임뷰에서 지속적으로 수행되어야 할 기능을 포함
                _canvas = m_surfaceHolder.lockCanvas(null);
                synchronized (m_surfaceHolder) {
                    m_gameview.onDraw(_canvas); //게임뷰에 이미지를 계속 그림
                }
               /* if (System.currentTimeMillis() - tTimer < 17) {
                    Thread.sleep(17 - System.currentTimeMillis() + tTimer);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (_canvas != null)
                    m_surfaceHolder.unlockCanvasAndPost(_canvas);
            }
            if (isWait) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (Exception e) {}
            }
        }
    }
}
