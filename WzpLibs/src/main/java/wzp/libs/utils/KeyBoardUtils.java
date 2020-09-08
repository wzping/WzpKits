package wzp.libs.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * 软键盘相关辅助类
 */
public final class KeyBoardUtils {

	// 日志TAG
	private static final String TAG = KeyBoardUtils.class.getSimpleName();

	/**
	 * Don't let anyone instantiate this class.
	 */
	private KeyBoardUtils() {
		throw new Error("Do not need instantiate!");
	}

	// == ----------------------------------------- ==

	/**
	 * 自动切换键盘状态，如果键盘显示了则隐藏，隐藏了则显示
	 * @param mContext
	 */
	public static void toggleKeyboard(Context mContext) {
		// 程序启动后，自动弹出软键盘，可以通过设置一个时间函数来实现，不能在onCreate里写
		try {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭软键盘
	 * @param mEditText 输入框
	 * @param mContext 上下文
	 */
	public static void closeKeybord(Context mContext,EditText mEditText) {
		try {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 关闭软键盘
	 * @param activity 当前页面
	 */
	public static void closeKeybord(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 打开软键盘
	 * @param mEditText 输入框
	 * @param mContext 上下文
	 */
	public static void openKeybord(Context mContext,EditText mEditText) {
		try {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 判断软键盘是否可见
	 * @param activity
	 * @return true : 可见, false : 不可见
	 */
	public static boolean isSoftInputVisible(Activity activity) {
		return isSoftInputVisible(activity, 100);
	}

	/**
	 * 判断软键盘是否可见
	 * @param activity
	 * @param minHeightOfSoftInput 软键盘最小高度
	 * @return true : 可见, false : 不可见
	 */
	public static boolean isSoftInputVisible(Activity activity,int minHeightOfSoftInput) {
		View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
		DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
		return getContentViewInvisibleHeight(activity) > minHeightOfSoftInput * dm.density;
	}


	/**
	 * 计算View的宽度高度
	 * @param activity
	 * @return
	 */
	private static int getContentViewInvisibleHeight(Activity activity) {
		try {
			View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			Rect rect = new Rect();
			rootView.getWindowVisibleDisplayFrame(rect);
			int heightDiff = rootView.getBottom() - rect.bottom;
			return heightDiff;
		} catch (Exception e) {
			LogUtils.e(TAG, e.getMessage());
			return 0;
		}
	}

	/**
	 * 注册软键盘改变监听器
	 * @param activity
	 * @param listener listener
	 */
	public static void registerSoftInputChangedListener(final Activity activity, final OnSoftInputChangedListener listener) {
		try {
			// 获取根View
			final View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			// 添加事件
			rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (listener != null) {
						// 获取高度
						int height = getContentViewInvisibleHeight(activity);
						DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
						// 判断是否相同
						listener.onSoftInputChanged(height > 100*dm.density, height);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.e(TAG, e.getMessage());
		}
	}

	/** 输入法弹出、隐藏改变事件 */
	public interface OnSoftInputChangedListener {
		/**
		 * 输入法弹出、隐藏改变通知
		 * @param visible
		 * @param height
		 */
		void onSoftInputChanged(boolean visible, int height);
	}

	/**
	 * 注销软键盘改变监听（不过这里，页面销毁，他自己也会跟着销毁的）
	 * @param activity
	 */
	public static void unRegisterSoftInputChangedListener(Activity activity){
		try {
			// 获取根View
			View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			rootView.getViewTreeObserver().addOnGlobalLayoutListener(null);
		} catch (Exception e) {
			LogUtils.e(TAG, e.getMessage());
		}
	}
}
