package wzp.libs.widget.able;

import android.text.TextWatcher;

/**
 * EditText输入监听
 * 使用如下：
 * editText.addTextChangedListener(new MyTextWatcher() {
 *             @Override
 *             public void afterTextChanged(Editable s) {
 *                 if (s.length()>0) {
 *                   	//TODO
 *                 }else{
 *                     //TODO
 *                 }
 *             }
 *         });
 */
public abstract class MyTextWatcher implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

}
