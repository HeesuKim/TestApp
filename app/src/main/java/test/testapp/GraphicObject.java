package test.testapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**게임 내 이미지 관련 클래스
 * Created by Administrator on 2016-08-03.
 */
public class GraphicObject {
    protected Bitmap m_bitmap;
    protected float m_x;        //가로좌표
    protected float m_y;        //세로좌표
    protected double g_wid;     //이미지의 적용 가로길이(단말기의 해상도에 따라 다름)
    protected double g_hei;     //이미지의 적용 세로길이(단말기의 해상도에 따라 다름)


    public GraphicObject(Bitmap bitmap) {
        m_bitmap = bitmap;
        m_x = 0;
        m_y = 0;
    }

    public GraphicObject(Bitmap bitmap, int x, int y) {
        m_bitmap = bitmap;
        m_x = x;
        m_y = y;
        //이미지의 단말기별 적용 길이
        g_wid = bitmap.getWidth();
        g_hei = bitmap.getHeight();
    }

    //좌표 재조정
    public void SetPosition(float x, float y) {
        m_x = x;
        m_y = y;
    }

    //들어온 수에 따라 땅 숫자 이미지 변환
    public void setBit(int swNum) {
        if (swNum == 1) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_1); }
        if (swNum == 2) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_2); }
        if (swNum == 3) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_3); }
        if (swNum == 4) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_4); }
        if (swNum == 5) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_5); }
        if (swNum == 6) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_6); }
        if (swNum == 7) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_7); }
        if (swNum == 8) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_8); }
        if (swNum == 9) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_9); }
        if (swNum == 0) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_0); }
        if (swNum == 10) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.earth_base); }
    }

    //들어온 수에 따라 정보관련 숫자 이미지 변환
    public void setNum(int swNum) {
        if (swNum == 1) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_1); }
        if (swNum == 2) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_2); }
        if (swNum == 3) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_3); }
        if (swNum == 4) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_4); }
        if (swNum == 5) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_5); }
        if (swNum == 6) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_6); }
        if (swNum == 7) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_7); }
        if (swNum == 8) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_8); }
        if (swNum == 9) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_9); }
        if (swNum == 0) { m_bitmap = AppManager.getInstance().getBitmap(R.drawable.num_0); }
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

    public double getG_wid() { return g_wid; }

    public double getG_hei() { return g_hei; }
}