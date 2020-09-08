package wzp.libs.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络管理工具类
 */
@SuppressLint("MissingPermission")
public final class NetWorkUtils {
	private static final String TAG  = NetWorkUtils.class.getSimpleName();
	
	/**
     * Don't let anyone instantiate this class.
     */
    private NetWorkUtils() {
        throw new Error("Do not need instantiate!");
    }
    
    // --------------------------------------------

	/**
	 * 判断是否开启移动网络 (默认属于打开)
	 * @param mContext 上下文
	 * @return
	 */
	public static boolean getMobileDataEnabled(Context mContext) {
		try {
			// 获取网络连接状态
			ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 反射获取方法
			Method method = cManager.getClass().getMethod("getMobileDataEnabled");
			// 调用方法,获取状态
			boolean mState = (Boolean) method.invoke(cManager);
			// 返回移动网络开关状态
			return mState;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 判断是否开启移动网络
	 * @param cManager 网络管理类
	 * @param arg 反射参数
	 * @return
	 * @throws Exception 抛出异常
	 */
	public static boolean getMobileDataEnabled(ConnectivityManager cManager, Object[] arg) throws Exception {
		// 获取类
		Class iClass = cManager.getClass();
		// 获取类参数
		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}
		// 反射的方法
		Method method = iClass.getMethod("getMobileDataEnabled", argsClass);
		// 获取结果
		Boolean isOpen = (Boolean) method.invoke(cManager, arg);
		return isOpen;
	}


	/**
	 * 设置移动网络开、关(无判断是否已开启移动网络)
	 * @param mContext 上下文
	 * @param isOpen 是否打开移动网络
	 * @return
	 */
	public static void setMobileDataEnabled(Context mContext, boolean isOpen){
		try {
			// 获取网络连接状态
			ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 通过反射开启移动网络
			Method mMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			// 打开移动网络
			mMethod.invoke(cManager, isOpen);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置移动网络开、关(无判断是否已开启移动网络)
	 * @param cManager 网络管理类
	 * @param isOpen 是否打开
	 * @return
	 * @throws Exception 抛出异常
	 */
	public static Object setMobileDataEnabled(ConnectivityManager cManager, boolean isOpen) throws Exception {
		// 获取类
		Class iClass = cManager.getClass();
		// 获取类参数
		Class[] argsClass = new Class[1];
		// 参数类型
		argsClass[0] = boolean.class;
		// 设置准备调用的方法
		Method method = iClass.getMethod("setMobileDataEnabled", argsClass);
		// 调用反射方法
		return method.invoke(cManager, isOpen);
	}

	/**
	 * 开启移动网络(判断是否开启了移动网络)
	 * @param mContext 上下文
	 */
	public static final void openMobileDataEnabled(Context mContext) {
		ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Object[] arg = null;
		try {
			// 判断是否打开移动网络
			boolean isMobileDataEnable = getMobileDataEnabled(cManager, arg);
			// 如果没打开则进行打开
			if (!isMobileDataEnable) {
				setMobileDataEnabled(cManager, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭移动网络(判断是否开启了移动网络)
	 * @param mContext
	 */
	public static final void closeMobileDataEnabled(Context mContext){
		ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Object[] arg = null;
		try {
			// 判断是否打开移动网络
			boolean isMobileDataEnable = getMobileDataEnabled(cManager, arg);
			// 如果打开了则关闭
			if (isMobileDataEnable) {
				setMobileDataEnabled(cManager, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否连接了网络
	 * @param mContext 上下文
	 */
	public static boolean isConnect(Context mContext) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cManager != null) {
				// 获取网络连接管理的对象
				NetworkInfo nInfo = cManager.getActiveNetworkInfo();
				if (nInfo != null && nInfo.isConnected()) {
					// 判断当前网络是否已经连接
					if (nInfo.getState() == State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取连接的网络类型
	 * @param mContext 上下文
	 * @return -1 = 等于未知 , 1 = Wifi, 2 = 移动网络
	 */
	public static int getConnectType(Context mContext) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			// 获取网络连接状态
			ConnectivityManager cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 判断连接的是否wifi
			State wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			// 判断是否连接上
			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				return 1;
			} else {
				// 判断连接的是否移动网络
				State mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
				// 判断移动网络是否连接上
				if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
					return 2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}


	/**
	 * 获取当前的网络状态
	 * @param context
	 * @return   -1：没有网络 1：WIFI网络 2：wap网络 3：net网络
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {  //移动网络
			if (networkInfo.getExtraInfo() == null) {
				return netType;
			}
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;  //CMNET  NET主要服务于移动端
			} else {
				netType = 2; //CMWAP WAP主要服务于PC端
			}
		}else if (nType == ConnectivityManager.TYPE_WIFI) { //Wifi
			netType = 1;
		}
		return netType;
	}


	/**
	 * 检查网络状况(检查是否可以连接网络)
	 * @return
	 */
	private static boolean pingNetWork(){
		// 得到连接对象
		HttpURLConnection connection = null;
		try {
			// 请求路径
			URL url = new URL("http://www.baidu.com");
			// 得到连接对象
			connection = (HttpURLConnection) url.openConnection();
			// 单位是毫秒
			connection.setConnectTimeout(1500);//设置连接超时
			connection.setReadTimeout(1500);//设置读取超时
			// HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
			connection.setRequestMethod("GET");
			// 获取请求状态码
			int responseCode = connection.getResponseCode();
			// 打印请求状态
			LogUtils.d(TAG, "ping responseCode : " + responseCode);
			return true;
		} catch(Exception e) {
			return false;
		} finally {
			try {
				if (connection != null) {
					// 关闭底层连接Socket
					connection.disconnect();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
