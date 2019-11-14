package wzp.libs.utils;

import android.util.Log;


/**
 * 日志工具类
 * Android_Log输出的优先级: Verbose,Debug,Info,Warn,Error
 */
public class LogUtils {

	/** 记录log 信息开关，默认为true */
	public static boolean isLog = true;


	public static void setTag(boolean tag){
		isLog = tag;
	}


	//-------------Verbose: 开发调试过程中一些详细信息，不应该编译进产品中，只在开发阶段使用-------------

	public static void v(String msg) {
		if (isLog) {
			Log.v("apiapi", msg);
		}
	}


	public static void v(String TAG,String msg) {
		if (isLog) {
			Log.v(TAG, msg);
		}
	}

	//-----------------Debug: 用于调试的信息，编译进产品，但可以在运行时关闭------------------

	public static void d(String msg) {
		if (isLog) {
			Log.d("apiapi", msg);
		}
	}


	public static void d(String TAG,String msg) {
		if (isLog) {
			Log.d(TAG, msg);
		}
	}

	//-----------------Info: 例如一些运行时的状态信息，这些状态信息在出现问题的时候能提供帮助------------------

	public static void i(String msg) {
		if (isLog) {
			Log.i("apiapi", msg);
		}
	}


	public static void i(String TAG,String msg) {
		if (isLog) {
			Log.i(TAG, msg);
		}
	}

	//-----------------Warn: 警告系统出现了异常，即将出现错误。------------------

	public static void w(String msg) {
		if (isLog) {
			Log.w("apiapi", msg);
		}
	}


	public static void w(String TAG,String msg) {
		if (isLog) {
			Log.w(TAG, msg);
		}
	}

	//-----------------Error: 系统已经出现了错误------------------

	public static void e(String msg) {
		if (isLog) {
			Log.e("apiapi", msg);
		}
	}


	public static void e(String TAG,String msg) {
		if (isLog) {
			Log.e(TAG, msg);
		}
	}
}
