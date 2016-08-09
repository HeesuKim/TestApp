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