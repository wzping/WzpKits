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


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (scrollable) {
			try {
				return super.onTouchEvent(ev);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
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

	/**
	 * 设置是否可以滑动
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		scrollable = enable;
	}


	/**
	 * 获取是否允许滑动
	 * @return
	 */
	public boolean isScrollable() {
		return scrollable;
	}


	/**
	 * 切换滑动状态
	 */
	public void toggleScrollable() {
		this.scrollable = !this.scrollable;
	}
}