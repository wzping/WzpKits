package wzp.libs.utils;


import java.util.ArrayList;
import java.util.List;


/**
 *  分割工具类
 */
public class SplitUtils {

	/**
	 * 切割字符串
	 * @param str 要切割的字符串
	 * @param regex 切割的规则（按什么进行切割）
	 * @return
	 */
	public static String[] splitString(String str,String regex){
		if(!StringUtils.isEmpty(str)){
			String[] split = str.split(regex);
			return split;
		}
		return null;
	}


	/**
	 * 按指定大小，分割集合(将集合按规定个数分为n个部分)
	 * @param list   将要分割的集合
	 * @param len    按len（长度）等比进行分割
	 * @return
	 */
	public static <T> List<List<T>> splitList(List<T> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<T>> result = new ArrayList<>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}

}
