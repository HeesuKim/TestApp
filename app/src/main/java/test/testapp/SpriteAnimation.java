package test.testapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Administrator on 2016-08-03.
 */
public class SpriteAnimation extends GraphicObject {
    private Rect mSRectangle;
    private int mFPS;
    private int mNoOfFrames;
    private int mCurrentFrame;
    private long mFrameTimer;
    private int mSpriteHeight;
    private int mSpriteWidth;

    public SpriteAnimation(Bitmap bitmap) {
        super(bitmap);
        mSRectangle = new Rect(0,0,0,0);
        mFrameTimer = 0;
        mCurrentFrame = 0;
        g_wid = bitmap.getWidth();
        g_hei = bitmap.getHeight();
    }

    public void InitSpriteData(int Height, int Width, int theFPS, int theFrameCount) {
        mSpriteHeight = Height;
        mSpriteWidth = Width;
        mSRectangle.top = 0;
        mSRectangle.bottom = mSpriteHeight;
        mSRectangle.left = 0;
        mSRectangle.right = mSpriteWidth;
        mFPS = 1000 / theFPS;
        mNoOfFrames = theFrameCount;
    }

    @Override
    public void Draw(Canvas canvas) {
        Rect dest = new Rect((int)m_x, (int)m_y, (int) m_x + mSpriteWidth, (int) m_y + mSpriteHeight);
        canvas.drawBitmap(m_bitmap, mSRectangle, dest, null);
    }

    //게임시간을 받아 이미지프레임을 하나씩 옮기며 연속성표현
    public void Update(long GameTime) {
        if (GameTime > (mFrameTimer + mFPS)) {
            mFrameTimer = GameTime;
            mCurrentFrame += 1;
            if (mCurrentFrame >= mNoOfFrames) {
                mCurrentFrame = 0;
            }
        }
        mSRectangle.left = mCurrentFrame * mSpriteWidth;
        mSRectangle.right = mSRectangle.left + mSpriteWidth;
    }
}
