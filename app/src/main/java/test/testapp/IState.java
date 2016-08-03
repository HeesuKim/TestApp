package test.testapp;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016-08-03.
 */
public interface IState {
    public void Init();
    public void Destroy();
    public void Update();
    public void Render(Canvas canvas);
    public boolean onKeyDown(int keyCode, KeyEvent event);
    public boolean onTouchEvent(MotionEvent event);
}
