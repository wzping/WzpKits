package wzp.libs.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 沉浸式状态栏工具类
 */
public class StatusBarUtils {

	/**
	 * 沉浸式状态栏 全图片效果
	 * @param activity
	 */
	public static void setFullPicStyle(Activity activity){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			// 透明状态栏 —> 1.设置全图片样式，这样设置后状态栏有一个半透明的灰色条
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//2.去掉状态栏中半透明的灰色条
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			//另外，这句是去掉手机底部的导航栏（返回键，Home键 那栏黑色的）
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
	}

}
