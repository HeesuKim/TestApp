package test.testapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2016-08-03.
 */
public class AppManager {
    private static AppManager s_instance;

    //Singleton
    public static AppManager getInstance() {
        if (s_instance == null) {
            s_instance = new AppManager();
        }
        return s_instance;
    }

    private GameView m_gameview;
    private Resources m_resources;
    void setGameView(GameView _gameview) {
        m_gameview = _gameview;
    }
    void setResources(Resources _resources) {
        m_resources = _resources;
    }
    public GameView getGameView() {
        return m_gameview;
    }
    public Resources getResources() {
        return m_resources;
    }

    //이미지 Bitmap 불러오기
    public Bitmap getBitmap(int r) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball_ani, options);
        return BitmapFactory.decodeResource(m_resources,r);
    }




}
