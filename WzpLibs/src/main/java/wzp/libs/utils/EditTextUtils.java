package wzp.libs.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditText相关工具类
 */
public class EditTextUtils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private EditTextUtils() {
		throw new Error("Do not need instantiate!");
	}


	/**
	 * EditText中可输入的长度 时时改变 长度剩余0的时候不能再输入(布局代码中设置  android:maxLength="xx")
	 * @param et  输入文字的EditText
	 * @param num 可以输入的文字总个数
	 * @param tv 展示个数的TextView
	 * @param hasStyle 字数展示的样式 true: 1/20    false:20
	 */
	public static void setInputLength(final EditText et, final int num, final TextView tv,final boolean hasStyle) {
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int number = num - s.length();// 剩余可输入的长度
				if(tv!=null) {
					if (hasStyle){
							tv.setText(s.length() + "/" + num);
					}else {
						tv.setText(number + "");
					}
				}
			}
		});
	}
}
