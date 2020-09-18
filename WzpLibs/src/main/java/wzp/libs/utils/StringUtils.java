package wzp.libs.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具类
 */
public class StringUtils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private StringUtils() {
		throw new Error("Do not need instantiate!");
	}


	/**
	 * 判断字符串是否为空 ( null, "", "null" )
	 * @param str 需要判断的字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null && !"".equalsIgnoreCase(str) && !"null".equalsIgnoreCase(str) && !" ".equals(str)) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 判断字符串是不是全是中文（中文字、中文标点符号）
	 * 4E00-9FA5:不包含中文标点符号
	 * 0391-FFE5：包含中文标点符号
	 * @param data 要判断的字符串
	 * @return
	 */
	public static boolean isOnlyChinese(String data) {
		if(data != null){
			String expr = "^[\u0391-\uFFE5]+$";
			return data.matches(expr);
		}
		return false;
	}

	/**
	 * 判断字符串中是否包含中文(中文字，中文标点)
	 * 4E00-9FA5:不包含中文标点符号
	 * 0391-FFE5：包含中文标点符号
	 * @param data 要判断的字符串
	 * @return
	 */
	public static boolean isContainChinese(String data) {
		String chinese = "[\u0391-\uFFE5]";
		int length;
		if(data != null && (length = data.length()) != 0){
			char[] dChar = data.toCharArray();
			for (int i = 0; i < length; i++) {
				boolean flag = String.valueOf(dChar[i]).matches(chinese);
				if (flag) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断字符串是不是全是字母
	 * +：匹配前面的子表达式一次或多次（大于等于1次）
	 * $：匹配输入字符串的结束位置
	 * @param data
	 * @return
	 */
	public static boolean isOnlyLetter(String data) {
		if(data != null){
			String expr = "^[A-Za-z]+$";
			return data.matches(expr);
		}
		return false;
	}

	/**
	 * 判断字符串是不是全是数字
	 * @param data
	 * @return
	 */
	public static boolean isOnlyNumber(String data) {
		if(data != null){
			String expr = "^[0-9]+$";
			return data.matches(expr);
		}
		return false;
	}

	/**
	 * 判断字符串是不是只含字母和数字
	 * @param data
	 * @return
	 */
	public static boolean isOnlyNumberLetter(String data) {
		if(data != null){
			String expr = "^[A-Za-z0-9]+$";
			return data.matches(expr);
		}
		return false;
	}

	/**
	 * 字符串仅限于数字和字母 (从多少位到多少位)
	 * @param str
	 * @return
	 */
	public static boolean isRangeNumberLetter(String str, int min, int max) {
		return Pattern.matches(String.format("[a-zA-Z0-9]{%s,%s}", min, max), str);
	}

	/**
	 * 字符串同时包含数字和字母(同时含有数字和字母 xxx-xxx位)
	 */
	public static boolean isContainRangeNumberLetter(String str,int min, int max){
		return Pattern.matches(String.format("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{%s,%s}$",min,max),str);
	}

	// == ----------------------------------------- ==

	/**
	 * 正则：手机号（精确）
	 * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
	 * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
	 * <p>电信：133、153、173、177、180、181、189</p>
	 * <p>全球星：1349</p>
	 * <p>虚拟运营商：170</p>
	 */
	public static final String TEL_EXACT_REGEX = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(19[0-9])|(147))\\d{8}$";


	/**
	 * 判断是否是手机的格式,是手机格式 返回true 否则返回false
	 */
	public static boolean isTelFormat(String str) {
		if (!str.matches(TEL_EXACT_REGEX)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是邮箱的格式,是邮箱格式 返回true 否则返回false
	 */
	public static boolean isEmailFormat(String str) {
		String emailRegex = "^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
		if (!str.matches(emailRegex)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是手机或邮箱的格式,是手机或邮箱格式 返回true 否则返回false
	 */
	public static boolean isTelEmailFormat(String str) {
		String telRegex = "[1]{1}[3,4,5,8]{1}[0-9]{9}";
		String emailRegex = "^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
		if (!str.matches(telRegex) && !str.matches(emailRegex)) {
			return false;
		}
		return true;
	}


	/**
	 * 验证输入的身份证号是否合法
	 * 由15位纯数字或18位数字（17位数字加“x”）组成，18位的话，可以是18位纯数字，或者17位数字加“x”
	 */
	public static boolean isLegalIdentity(String identity){
		if (identity.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")){
			return true;
		}else {
			return false;
		}
	}

	// == ----------------------------------------- ==

	/**
	 * 去掉字符串中的空格
	 * @param str    String  a = "00 12 4b 00 07 50 62 33 71 01"；
	 * @return   String  a = "0124b00075062337101"
	 */
	public static String removeBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*");  // Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * java去除字符串中的空格、回车、换行符、制表符:换成这句 --》Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	 * \s 空格(\u0008)
	 * \n 回车(\u000a)
	 * \r 换行(\u000d)
	 * \t 水平制表符(\u0009)
	 */
	public static String removeBlank(String str,String regex) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile(regex);  // Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}


}
