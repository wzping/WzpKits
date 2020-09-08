package wzp.libs.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * wifi工具类
 * 需要添加权限： android.permission.ACCESS_WIFI_STATE
 */
@SuppressLint("MissingPermission")
public class WifiUtils {
    /** 日志Tag */
	protected static final String TAG = WifiUtils.class.getSimpleName();

	/**
	 * 通过上下文获取当前连接的ssid
	 * @param mContext 上下文
	 */
	public static String getSSID(Context mContext){
		try {
			// 初始化WifiManager对象
			WifiManager mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			// 获取当前连接的wifi
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			// 获取wifi - SSID
			return wifiInfo.getSSID();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 判断当前wifi是否5G
	 * @param mContext
	 * @param ssid
	 * @return
	 */
	public static boolean is5GWifi(Context mContext,String ssid){
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		 List<ScanResult> scanResults = wifiManager.getScanResults();
		for(ScanResult scanResult:scanResults){
			if(scanResult.SSID.equals(ssid)){
				//返回4个数字，2开头的话就是2.4G,5开头的话就是5G
				int frequency = scanResult.frequency;
				if((frequency+"").startsWith("2")){
					return false;
				}else if((frequency+"").startsWith("5")){
					return true;
				}
				break;
			}
		}
		return false;
	}
}
