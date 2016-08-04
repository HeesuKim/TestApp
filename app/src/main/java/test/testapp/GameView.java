package test.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
   // private IState m_state;
    private GraphicObject m_Image, n_Image;
    private SpriteAnimation m_spr;

    private int stFlag = 0;
    private int stPos = 0;


    public GameView(Context context) {
        super(context);
        setFocusable(true);
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());
        getHolder().addCallback(this);

        m_thread = new GameViewThread(getHolder(),this);
        m_Image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ba));
        n_Image = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.ea));
        m_Image.SetPosition(20, 20);
        n_Image.SetPosition(300, 120);
       /* m_spr = new SpriteAnimation(BitmapFactory.decodeResource(getResources(),R.drawable.anim321));
        m_spr.InitSpriteData(56, 62, 50, 17);*/
    }

    @Override
    public void onDraw(Canvas canvas) {
        /*Bitmap _scratch = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);*/
        //Bitmap _scratch = AppManager.getInstance().getBitmap(R.mipmap.ic_launcher);
        canvas.drawColor(Color.BLACK);
       // canvas.drawBitmap(_scratch, 50, 50, null);
        //m_state.Render(canvas);
        if (stFlag == 1) {
            m_Image.Draw(canvas);
            n_Image.Draw(canvas);
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
        if (stFlag != 0) {
            if (m_Image.GetX() > 1200) {
                m_Image.SetPosition(10, 0);
            } else {
                m_Image.SetPosition(m_Image.GetX() + 3, m_Image.GetY());
            }
        }

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
                if (stPos == 0) {
                    m_Image.SetPosition(100, 300);
                    stPos = 1;
                } else if (stPos == 1){
                    m_Image.SetPosition(100, 100);
                    stPos = 0;
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
