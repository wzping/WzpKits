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

	// == ----------------------------------------- ==


	/**
	 * 切割字符串
	 * @param str 要切割的字符串
	 * @param regex 切割的规则（按什么进行切割）
	 * @return
	 */
	public static String[] split(String str,String regex){
		if(!isEmpty(str)){
			String[] split = str.split(regex);
			return split;
		}
		return null;
	}

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

	// == ----------------------------------------- ==

	/**
	 * HTML颜色标记
	 * @param content 给定的字符串
	 * @param color 要设定的颜色
	 * @return 返回的结果最后放在 Html.fromHtml();内
	 */
	public static String addHtmlColor(String content,String color) {
		return "<font color=\""+ color +"\">" + content + "</font>";
	}

	/**
	 * 加粗标记
	 * @param content 给定的字符串
	 * @return 最后放在 Html.fromHtml();内
	 */
	public static String addHtmlBold(String content) {
		return "<b>" + content + "</b>";
	}

	/**
	 * 颜色标记,以及加粗
	 * @param content 给定的字符串
	 * @param color 要设定的颜色
	 * @return 最后放在 Html.fromHtml();内
	 */
	public static String addHtmlColorAndBlod(String content,String color) {
		return "<b><font color=\""+ color +"\">" + content + "</font></b>";
	}

	/**
	 * 下划线
	 * @param content 给定的字符串
	 * @return 最后放在 Html.fromHtml();内
	 */
	public static String addHtmlUnderline(String content) {
		return "<u>" + content + "</u>";
	}

	/**
	 * 字体倾斜
	 * @param content 给定的字符串
	 * @return 最后放在 Html.fromHtml();内
	 */
	public static String addHtmlIncline(String content) {
		return "<i>" + content + "</i>";
	}

	/**
	 * 将给定的字符串中所有给定的关键字标色
	 * @param source 给定的字符串
	 * @param keyword 给定的关键字
	 * @param color 需要标记的颜色
	 * @return
	 */
	public static String keywordTinting(String source, String keyword, String color) {
		return keywordReplaceAll(source, keyword, ("<font color=\"" + color + "\">" + keyword + "</font>"));
	}


	/**
	 * 将给定的字符串中所有给定的关键字进行替换内容
	 * @param source 给定的字符串
	 * @param keyword 给定的关键字
	 * @param replacement 替换的内容
	 * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml() 转换为Spanned对象再传递给TextView对象
	 */
	public static String keywordReplaceAll(String source, String keyword, String replacement) {
		try {
			if(source != null && source.trim().length() != 0){
				if (keyword != null && keyword.trim().length() != 0) {
					if(replacement != null && replacement.trim().length() != 0){
						return source.replaceAll(keyword , replacement);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}
}
