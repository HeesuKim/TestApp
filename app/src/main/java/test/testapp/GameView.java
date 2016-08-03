package test.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
   // private IState m_state;
    private GraphicObject m_Image;
    private SpriteAnimation m_spr;

    public GameView(Context context) {
        super(context);

        setFocusable(true);
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());

        getHolder().addCallback(this);
        m_thread = new GameViewThread(getHolder(),this);
        m_Image =  new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.annn));
       /* m_spr = new SpriteAnimation(BitmapFactory.decodeResource(getResources(),R.drawable.anim321));
        m_spr.InitSpriteData(56, 62, 50, 17);*/
    }

    @Override
    public void onDraw(Canvas canvas) {
        /*Bitmap _scratch = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Bitmap _scratch = AppManager.getInstance().getBitmap(R.mipmap.ic_launcher);*/
        canvas.drawColor(Color.BLACK);
       /* canvas.drawBitmap(_scratch, 50, 50, null);*/
        //m_state.Render(canvas);
        m_Image.Draw(canvas);
        //m_spr.Draw(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

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
        //m_state.Update();
        /*long GameTime = System.currentTimeMillis();
        m_spr.Update(GameTime);*/
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       return m_state.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return m_state.onTouchEvent(event);
    }*/

   /* public void ChangeGameStsate(IState _state) {
        if (m_state != null) {
            m_state.Destroy();
        }
        _state.Init();
        m_state = _state;
    }*/
}
