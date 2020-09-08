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
public final class ScreenConvertUtils {
	
	/**
     * Don't let anyone instantiate this class.
     */
    private ScreenConvertUtils() {
        throw new Error("Do not need instantiate!");
    }

	// == ----------------------------------------- ==

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param mContext
	 * @param dpValue
	 */
	public static int dipConvertPx(Context mContext, float dpValue) {
		try {
			float scale = mContext.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 第二种
	 * @param mContext
	 * @param dpValue
	 */
	public static int dipConvertPx2(Context mContext, float dpValue) {
		try {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param mContext
	 * @param pxValue
	 */
	public static int pxConvertDip(Context mContext, float pxValue) {
		try {
			float scale = mContext.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 * @param mContext
	 * @param pxValue
	 */
	public static int pxConvertSp(Context mContext, float pxValue) {
		try {
			float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
			return (int) (pxValue / scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px
	 * @param mContext
	 * @param spValue
	 */
	public static int spConvertPx(Context mContext, float spValue) {
		try {
			float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
			return (int) (spValue * scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px 第二种
	 * @param mContext
	 * @param spValue
	 */
	public static int spConvertPx2(Context mContext, float spValue) {
		try {
			// android.util.TypedValue
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mContext.getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}

