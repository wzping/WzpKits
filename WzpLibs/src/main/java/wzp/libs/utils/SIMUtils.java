package wzp.libs.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;


/**
 * SIM卡相关工具类
 */
public class SIMUtils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private SIMUtils() {
		throw new Error("Do not need instantiate!");
	}


	// == ----------------------------------------- ==

	/**
	 * 判断是否装载sim卡
	 * @param mContext
	 * @return
	 */
	public static boolean isSimReady(Context mContext){
		try {
			// 获取电话管理类
			TelephonyManager tpManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			// 是否准备完毕
			if(tpManager != null && tpManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取Sim卡所属地区，非国内地区暂不支持
	 * @param mContext 上下文
	 * @return 返回SIM的国家码
	 */
	public static String getUserCountry(Context mContext) {
		try {
			TelephonyManager tpManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String simCountry = tpManager.getSimCountryIso();
			//LogUtils.d(TAG,"simCountry:" + simCountry);
			if (simCountry != null && simCountry.length() == 2) {
				// SIM country code is available
				return simCountry.toLowerCase(Locale.CHINA);
			} else if (tpManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
				// device is not 3G (would be unreliable)
				String networkCountry = tpManager.getNetworkCountryIso();
				//LogUtils.d(TAG, "networkCountry:" + networkCountry);
				if (networkCountry != null && networkCountry.length() == 2) {
					// network country code is available
					return networkCountry.toLowerCase(Locale.CHINESE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 判断地区，是否属于国内
	 * @param mContext 上下文
	 * @return 状态码 1 属于国内（中国），2 属于 国外，3 属于无SIM卡
	 */
	public static int judgeArea(Context mContext) {
		// 默认属于无sim卡
		int state = 3;
		try {
			String countryCode = getUserCountry(mContext);
			//LogUtils.d(TAG, "countryCode:" + countryCode);
			// 不等于null,表示属于存在SIM卡
			if (countryCode != null) {
				// zh_CN Locale.SIMPLIFIED_CHINESE
				// 截取前面两位属于zh表示属于中国
				String country = countryCode.substring(0, 2);
				// 如果属于ch开头表示属于中国
				if (country.toLowerCase().equals("cn")) {
					state = 1;
				} else {
					state = 2;
				}
			} else { // 不存在sim卡
				String localCountry = Locale.getDefault().getCountry();
				// 如果属于ch开头表示属于中国
				if (localCountry.toLowerCase().equals("cn")) {
					return 1;
				} else {
					return 2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}
}
