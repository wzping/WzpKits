package wzp.kits.use.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import wzp.libs.utils.screen.ScreenSensorUtils;

public class ScreenSensorUtilsActivity extends Activity {
    private static final String TAG = ScreenSensorUtilsActivity.class.getSimpleName();
    private Context mContext;
    private ScreenSensorUtils screenSensorUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.xxx);

        mContext = ScreenSensorUtilsActivity.this;
        screenSensorUtils = new ScreenSensorUtils();
        //开启重力传感器监听
        screenSensorUtils.start(mContext,vHandler);


        //=============另外一种判断的方法 ，跟工具类无关
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
//            LogUtils.d(TAG,"横屏------");
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
//            LogUtils.d(TAG,"竖屏------");
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
        }

    }

    /**
     * 专门修改View 的Handler
     */
    private Handler vHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ScreenSensorUtils.CHANGE_ORIENTATION_WHAT: //触发屏幕方向改变回调
                    if(!screenSensorUtils.isAllowChange()){ // 如果不允许切屏,则不显示
                        return;
                    } else if(isFinishing()){ // 如果页面关闭了
                        return;
                    }
                    if( screenSensorUtils.isPortrait()){
//                        LogUtils.d(TAG,"竖屏");
                    }else{
//                        LogUtils.d(TAG,"横屏");
                        //当前时间-切屏的时间，大于1.5秒间隔才进行跳转
//                        if(System.currentTimeMillis()-orientationTime>=1500){
//                            LogUtils.d(TAG,"横屏");
//                            //重置时间，防止多次触发
//                            orientationTime = System.currentTimeMillis();
//                        }
                    }
            	    break;
                default:
            	    break;
            }
        }
    };
}
