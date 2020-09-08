package wzp.libs.utils.screen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * 屏幕相关工具类
 */
public final class ScreenUtils {
	
	/**
     * Don't let anyone instantiate this class.
     */
    private ScreenUtils() {
        throw new Error("Do not need instantiate!");
    }

	// == ----------------------------------------- ==

	/**
	 * 获取屏幕宽度（获取手机屏幕分辨率的宽）
	 * @param mContext 上下文
	 * @return
	 */
	public static int getWidth(Context mContext){
		int[] screen = getScreenWidthHeight(mContext);
		if(screen != null){
			return screen[0];
		}
		return 0;
	}

	/**
	 * 获取屏幕高度（获取手机屏幕分辨率的高）
	 * @param mContext 上下文
	 * @return
	 */
	public static int getHeight(Context mContext){
		int[] screen = getScreenWidthHeight(mContext);
		if(screen != null){
			return screen[1];
		}
		return 0;
	}

	/**
	 * 获取屏幕信息(获取手机屏幕的分辨率，与手机实际屏幕大小没关系)
	 * @param mContext 上下文
	 * @return
	 */
	public static int[] getScreenWidthHeight(Context mContext){
		int[] screen = null;
		try {
			WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			Display display = wManager.getDefaultDisplay();
			// 获取宽度高度
			int width = display.getWidth();
			int height = display.getHeight();
			if(width != 0 && height != 0){
				screen = new int[]{width,height};
			}
		} catch (Exception e) {
			screen = null;
		}
		return screen;
	}


	/**
	 * 得到的屏幕的宽度
	 */
	public static int getWidthPx(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.widthPixels;
	}

	/**
	 * 得到的屏幕的高度
	 */
	public static int getHeightPx(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.heightPixels;
	}

	/**
	 * 得到屏幕的dpi
	 * @param activity
	 * @return
	 */
	public static int getDensityDpi(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.densityDpi;
	}


	/**
	 * 返回状态栏/通知栏的高度
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}
}

