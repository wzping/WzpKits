package wzp.libs.widget.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

import wzp.libs.R;

/**
 * 设定一个时间(00:00:00),然后在设定的时间上累加计时
 */
@SuppressLint("HandlerLeak")
public class CountUpTimerView extends LinearLayout {

	/** 小时，十位 */
	private TextView tv_hour_decade;
	/** 小时，个位 */
	private TextView tv_hour_unit;
	/** 分钟，十位 */
	private TextView tv_min_decade;
	/**分钟，个位 */
	private TextView tv_min_unit;
	/** 秒，十位 */
	private TextView tv_sec_decade;
	/** 秒，个位 */
	private TextView tv_sec_unit;
	private int hour_decade;
	private int hour_unit;
	private int min_decade;
	private int min_unit;
	private int sec_decade;
	private int sec_unit;
	private LayoutInflater inflater;
	private View view;
	private Timer timer; // 计时器


	public CountUpTimerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//默认布局文件
		setLayout(R.layout.view_counttimer);

	}


	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			countUp();
		}
	};


	/**
	 * 累计时
	 */
	private void countUp() {
		if (isCarry4Unit(tv_sec_unit)) {
			if (isCarry4Decade(tv_sec_decade)) {
				if (isCarry4Unit(tv_min_unit)) {
					if (isCarry4Decade(tv_min_decade)) {
						if (isCarry4Unit(tv_hour_unit)) {
							if (isCarry4Decade(tv_hour_decade)) {
//								Toast.makeText(context, "时间到了", Toast.LENGTH_SHORT).show();
//								stop();
							}
						}
					}
				}
			}
		}
	}


	/**
	 * 变化个位，并判断是否需要进位
	 * @param tv
	 * @return
	 */
	private boolean isCarry4Unit(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time + 1;
		if (time > 9) {
			time = 0;
			tv.setText(time + "");
			return true;
		} else {
			tv.setText(time + "");
			return false;
		}
	}

	/**
	 * 变化十位，并判断是否需要进位
	 * @param tv
	 * @return
	 */
	private boolean isCarry4Decade(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time + 1;
		if (time > 5) {
			time = 0;
			tv.setText(time + "");
			return true;
		} else {
			tv.setText(time + "");
			return false;
		}
	}

	//------------------------------  对外公开方法 --------------------------------

	/**
	 * 设置要显示的布局文件（不设置的话，就展示的默认布局）
	 * @param layout
	 */
	public void setLayout(int layout){

		if (view==null){
			view = inflater.inflate(layout, null);
			this.addView(view);
		}else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent!=null){
				parent.removeView(view);
			}
			view = inflater.inflate(layout, null);
			this.addView(view);
		}

		tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
		tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
		tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
		tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
		tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
		tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);
	}


	/**
	 * 设置开始计时的时间节点
	 * @param hour
	 * @param min
	 * @param sec
	 */
	public void setTime(int hour, int min, int sec) {

		if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0 || sec < 0) {
			return;
		}

		hour_decade = hour / 10;
		hour_unit = hour - hour_decade * 10;

		min_decade = min / 10;
		min_unit = min - min_decade * 10;

		sec_decade = sec / 10;
		sec_unit = sec - sec_decade * 10;

		tv_hour_decade.setText(hour_decade + "");
		tv_hour_unit.setText(hour_unit + "");
		tv_min_decade.setText(min_decade + "");
		tv_min_unit.setText(min_unit + "");
		tv_sec_decade.setText(sec_decade + "");
		tv_sec_unit.setText(sec_unit + "");
	}

	/**
	 * 开始累计时
	 */
	public void start() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			}, 0, 1000);
		}
	}


	/**
	 * 停止累计时
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}


	/**
	 * 获取停止时候的时间
	 * @return 返回格式：20:10:40
	 */
	public String getTime(){
		return tv_hour_decade.getText().toString() + tv_hour_unit.getText().toString() + ":" +
				tv_min_decade.getText().toString() + tv_min_unit.getText().toString() + ":" +
				tv_sec_decade.getText().toString() + tv_sec_unit.getText().toString();
	}
}
