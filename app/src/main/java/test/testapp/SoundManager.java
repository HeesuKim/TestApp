package test.testapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by Administrator on 2016-08-03.
 */
public class SoundManager {
    private static SoundManager s_instance;
    private int playingFlag = 0;

    public static SoundManager getInstance() {
        if (s_instance == null) {
           s_instance = new SoundManager();
        }
        return s_instance;
    }

    private SoundPool       m_SoundPool;
    private HashMap         m_SoundPoolMap;
    private AudioManager    m_AudioManager;
    private Context         m_Activity;

    //SoundPool 초기화
    public void Init(Context _context) {
        m_SoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        m_SoundPoolMap = new HashMap();
        m_AudioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
        m_Activity = _context;
    }

    //SoundPool 요소 추가
    public void addSound(int _index, int _soundID) {
        int id = m_SoundPool.load(m_Activity, _soundID, 1);
        m_SoundPoolMap.put(_index, id);
    }


    //Sound 재생
    public void play(int _index) {
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume /
                (m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume,
                streamVolume, 1, 0, 1f);
        playingFlag = 1;
    }

    public void stop(int _index) {
        if (playingFlag == 1) {
            m_SoundPool.stop((Integer) m_SoundPoolMap.get(_index));
            playingFlag = 0;
        }
    }

    //Sound 반복재생시
    public void playLooped(int _index) {
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume /
                (m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume,
                streamVolume, 1, -1, 1f);
    }
}
