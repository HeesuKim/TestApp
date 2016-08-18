package test.testapp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GameViewThread extends Thread {
    private SurfaceHolder m_surfaceHolder;
    private GameView m_gameview;
    private boolean m_run = false;
    private boolean isWait = false;

    public GameViewThread(SurfaceHolder surfaceHolder, GameView gameview) {
        m_surfaceHolder = surfaceHolder;
        m_gameview = gameview;
    }

    public void setRunning(boolean run) {
        m_run = run;
    }

    public void onPause(boolean isWait) {
        synchronized (this) {
            this.isWait = isWait;
            notify();
        }
    }


    long tTimer = 0;
    long afTime = 0;
    @Override
    public void run() {
        Canvas _canvas;
        System.out.println("NONOT : ThreadRun");
        while (m_run) {
            //System.out.println("NONOT : ERWThreadRun!!");
            _canvas = null;
            try {
                //tTimer = System.currentTimeMillis();
                //afTime = System.currentTimeMillis();
                m_gameview.Update();
                _canvas = m_surfaceHolder.lockCanvas(null);
                synchronized (m_surfaceHolder) {
                    m_gameview.onDraw(_canvas);
                }
                //System.out.println("수행시간 - > " + (System.currentTimeMillis() - tTimer));
               /* if (System.currentTimeMillis() - tTimer < 17) {
                    Thread.sleep(17 - System.currentTimeMillis() + tTimer);
                }*/
                //System.out.println("sleep -> " + (System.currentTimeMillis() - afTime));
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
