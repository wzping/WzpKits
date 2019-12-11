package wzp.libs.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 是否可以滑动(左右滑动)的viewpager
 */
public class ControlScrollViewPager extends ViewPager {

	//默认可以滑动
	private boolean scrollable = true;

	public ControlScrollViewPager(Context context) {
		super(context);
	}


	public ControlScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置是否可以滑动
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		scrollable = enable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (scrollable) {
			try {
				return super.onInterceptTouchEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}
}