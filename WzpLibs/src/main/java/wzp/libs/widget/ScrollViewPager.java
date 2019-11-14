package wzp.libs.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 是否可以滑动的viewpager
 */
public class ScrollViewPager extends ViewPager {

	//默认可以滑动
	private boolean scrollable = true;

	public ScrollViewPager(Context context) {
		super(context);
	}


	public ScrollViewPager(Context context, AttributeSet attrs) {
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
			return super.onInterceptTouchEvent(event);
		} else {
			return false;
		}
	}
}