package wzp.libs.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;


/**
 * 语言相关工具类
 */
public class LanguageUtils {

	/**
	 * 判断当前系统语言类型是否是中文类型  (zh-CN、zh-TW、zh-HK)
	 * @return 默认英文
	 */
	public static String getLocale(){
		try {
			// 获取本地语言
			Locale locale = Locale.getDefault();
			// 获取语言类型
			String language = locale.getLanguage();
			// 判断是否属于中文
			if(language.indexOf("zh") != -1){     // !=-1  说明language字段中包含zh
				return "zh";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "en";
	}


	/**
	 * 设置指定语言 (app 多语言,单独改变app语言)
	 * @param mContext
	 * @param locale  跟随系统：Locale.getDefault()  | 简体中文：Locale.SIMPLIFIED_CHINESE | English：Locale.ENGLISH
	 */
	public static void setLanguage(Context mContext,Locale locale) {
		try {
			// 获得res资源对象
			Resources resources = mContext.getResources();
			// 获得设置对象
			Configuration config = resources.getConfiguration();
			// 获得屏幕参数：主要是分辨率，像素等。
			DisplayMetrics dm = resources.getDisplayMetrics();
			// 语言
			config.locale = locale;
			// 更新语言
			resources.updateConfiguration(config, dm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
