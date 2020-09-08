package wzp.libs.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


/**
 * Android音频播放之SoundPool
 * SoundPool —— 适合短促且对反应速度比较高的情况（游戏音效或按键声等）
 */
public class SoundUtils {

    /** SoundPool */
    private static SoundPool soundPool;
    /** 创建某个声音对应的音频ID */
    private static int soundID;

    /**
     * 通过资源id进行加载
     * @param mContext
     * @param resId
     */
    public static void startPlay(Context mContext,int resId){
        if (soundPool == null) {
            //maxStream : 同时播放的流的最大数量
            //streamType: 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)
            //srcQuality: 采样率转化质量，当前无效果，使用0作为默认值
            soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);

            //可以通过4种途径来加载一个音频资源，这里是通过资源Id进行加载的
            soundID = soundPool.load(mContext, resId, 1);  //其中的priority参数目前没有效果，建议设置为1
            //成功 - soundID = 非0    失败：soundID = 0

        }
        playSound(soundID,0.5f,0.5f,0,0,1);
    }

    /**
     * 通过资源id进行加载
     * @param mContext
     * @param resId
     */
    public static void startPlay(Context mContext,int resId,float leftVolume, float rightVolume,int priority, int loop, float rate){
        if (soundPool == null) {
            //maxStream : 同时播放的流的最大数量
            //streamType: 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)
            //srcQuality: 采样率转化质量，当前无效果，使用0作为默认值
            soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);

            //可以通过4种途径来加载一个音频资源，这里是通过资源Id进行加载的
            soundID = soundPool.load(mContext, resId, 1);  //其中的priority参数目前没有效果，建议设置为1
            //成功 - soundID = 非0    失败：soundID = 0

        }
        playSound(soundID,leftVolume,rightVolume,priority,loop,rate);
    }


    /**
     * 通过路径进行加载
     * @param path
     */
    public static void startPlay(String path){
        if (soundPool == null) {
            //maxStream : 同时播放的流的最大数量
            //streamType: 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)
            //srcQuality: 采样率转化质量，当前无效果，使用0作为默认值
            soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);

            soundID = soundPool.load(path,1);
        }


        playSound(soundID,0.5f,0.5f,0,0,1);
    }


    /**
     * 通过路径进行加载
     * @param path
     */
    public static void startPlay(String path, float leftVolume, float rightVolume,int priority, int loop, float rate){
        if (soundPool == null) {
            //maxStream : 同时播放的流的最大数量
            //streamType: 流的类型，一般为STREAM_MUSIC(具体在AudioManager类中列出)
            //srcQuality: 采样率转化质量，当前无效果，使用0作为默认值
            soundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);

            soundID = soundPool.load(path,1);
        }
        playSound(soundID,leftVolume,rightVolume,priority,loop,rate);
    }

    /**
     * 开始播放
     */
    public static void playSound(int soundID, float leftVolume, float rightVolume,int priority, int loop, float rate){
        //priority : 流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理；
        //loop : 循环播放的次数，0为只播放一次，-1为无限循环，其他值为播放loop+1次（例如，3为一共播放4次）.
        //rate : 播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
        soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
    }


    /**
     * 暂停指定播放流的音效
     */
    public static void pausePlay(){
        if (soundPool!=null){
            soundPool.pause(soundID);
        }
    }

    /**
     * 继续播放指定播放流的音效
     */
    public static void resumePlay(){
        if (soundPool!=null){
            soundPool.resume(soundID);
        }
    }


    /**
     * 终止指定播放流的音效
     */
    public static void stopPlay() {
        if (soundPool != null) {
            soundPool.stop(soundID);
        }
        soundPool = null;
    }
}
