package test.testapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GraphicObject {
    protected Bitmap m_bitmap;
    protected float m_x;
    protected float m_y;
    protected int m_ans;
    protected int m_pos;
    protected double g_wid;
    protected double g_hei;


    public GraphicObject(Bitmap bitmap) {
        m_bitmap = bitmap;
        m_x = 0;
        m_y = 0;
    }

    public GraphicObject(Bitmap bitmap, int x, int y, int ans, int pos) {
        m_bitmap = bitmap;
        m_x = x;
        m_y = y;
        m_ans = ans;
        m_pos = pos;
        g_wid = bitmap.getWidth();
        g_hei = bitmap.getHeight();
    }

    public void SetPosition(float x, float y) {
        m_x = x;
        m_y = y;
    }

    public void setAns(int ans) {
        m_ans = ans;
    }

    public void setPos(int pos) {
        m_pos = pos;
    }

    public void setBit(int swNum) {
        if (swNum == 1) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en1); }
        if (swNum == 2) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en2); }
        if (swNum == 3) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en3); }
        if (swNum == 4) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en4); }
        if (swNum == 5) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en5); }
        if (swNum == 6) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en6); }
        if (swNum == 7) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en7); }
        if (swNum == 8) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en8); }
        if (swNum == 9) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en9); }
        if (swNum == 0) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.en0); }

    }

    public void Draw(Canvas canvas) {
        canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }

    public float getX() {
        return m_x;
    }

    public float getY() {
        return m_y;
    }

    public int getAns() {
        return m_ans;
    }

    public int getPos() {
        return m_pos;
    }

    public double getG_wid() { return g_wid; }
    public double getG_hei() { return g_hei; }

}