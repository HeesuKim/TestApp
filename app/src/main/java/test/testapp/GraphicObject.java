package test.testapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Administrator on 2016-08-03.
 */
public class GraphicObject {
    protected Bitmap m_bitmap;
    protected int m_x;
    protected int m_y;
    protected int m_k;

    protected int m_pos;

    public GraphicObject(Bitmap bitmap) {
        m_bitmap = bitmap;
        m_x = 0;
        m_y = 0;
    }

    public void SetPosition(int x, int y) {
        m_x = x;
        m_y = y;
    }

    public void SetAns(int k) {
        m_k = k;
    }

    public void SetAnspos(int p) {
        m_pos = p;
    }

    public void Draw(Canvas canvas) {
        canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }

    public int GetX() {
        return m_x;
    }

    public int GetY() {
        return m_y;
    }

    public int GetK() {
        return m_k;
    }

    public int GetP() {
        return m_pos;
    }

}