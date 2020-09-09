package wzp.kits.use.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import wzp.kits.R;
import wzp.libs.utils.TimerUtils;

public class TimerUtilsActivity extends Activity {
    /** 日志 */
    private static final String TAG = TimerUtilsActivity.class.getSimpleName();
    /** 显示时间*/
    private TextView time_tv;
    /** 定时器封装工具类 */
    private TimerUtils textTimer;
    private int count = 60;
    /** 定时器触发 */
    private final int NOTIFY_MESSAGE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timerutils);

        time_tv = (TextView) findViewById(R.id.time_tv);
        // 初始化定时器
        textTimer = new TimerUtils(handler);
        // 配置参数 - 意思是 一开始0秒直接触发第一次，然后后面每隔1秒触发一次，通过Handler通知 NOTIFY 常量 (-1表示无限次)
        textTimer.setTriggerLimit(-1).setTime(0, 1000).setNotifyWhat(NOTIFY_MESSAGE);

        // 配置参数 - 一秒钟后进行触发,然后每隔1秒循环触发(但是触发一次 TriggerLimit 限制了次数), 并通过设置的Handler通知 对应传入的What
        //textTimer.setHandler(handler).setTriggerLimit(1).setTime(1000, 1000).setNotifyWhat(NOTIFY_MESSAGE);

        // 配置参数 - 3秒钟后进行触发,然后每隔3秒循环触发(但是触发10次 TriggerLimit 限制了次数), 并通过设置的Handler通知 对应传入的What,并且开始定时器
        //textTimer.setHandler(handler).setTriggerLimit(10).setTime(3000, 3000).setNotifyWhat(NOTIFY_MESSAGE).startTimer();

        // 开始运行定时器
        textTimer.startTimer();

        // 判断是否运行中
        //textTimer.isRunTimer();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NOTIFY_MESSAGE:
                    time_tv.setText(count+"");
                    count--;
                    if(count == 0){
                        // 关闭定时器
                        textTimer.closeTimer();
                    }
                    break;
            }
        }
    };
}
