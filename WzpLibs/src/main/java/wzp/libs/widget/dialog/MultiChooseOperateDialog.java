package wzp.libs.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import wzp.libs.R;


/**
 * 多种选择操作Dialog
 * (上面显示要操作的多种选项，下面取消按钮)
 */
public class MultiChooseOperateDialog extends Dialog implements View.OnClickListener {
	private Context mContext;
	//=============  控件  =========
	/** 选择一 */
	private TextView choose_one_tv;
	/** 选择二 */
	private TextView choose_two_tv;
	/** 取消 */
	private TextView choose_cancel;
	//点击选择了第一种（第一个item）
	public static final int CHOICE_ONE = 1;
	//点击选择了第二种（第二个item）
	public static final int CHOICE_TWO = 2;

	/**
	 * 初始化构造函数
	 */
	public MultiChooseOperateDialog(Context mContext) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;

		setLayout(R.layout.dialog_multi_choose_operate);
	}

	public MultiChooseOperateDialog(Context mContext, int layout) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;
		setLayout(layout);
	}


	public void setLayout(int layout){
		// 绑定Layout
		setContentView(layout);

		// 设置宽度,高度以及显示的位置
		Window window = MultiChooseOperateDialog.this.getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lParams = window.getAttributes();
		lParams.width = LinearLayout.LayoutParams.MATCH_PARENT ;
		lParams.alpha = 1.0f;

		choose_one_tv =  this.findViewById(R.id.choose_one_tv);
		choose_two_tv =  this.findViewById(R.id.choose_two_tv);
		choose_cancel =  this.findViewById(R.id.choose_cancel);

		choose_cancel.setOnClickListener(this);
	}

	/**
	 * 显示Dialog
	 */
	public void showDialog(){
		// 显示Dialog
		this.show();
	}


	/**
	 * 默认显示方法
	 */
	@Override
	public void show() {
		// 显示
		super.show();

	}

	/**
	 * 关闭Dialog
	 */
	public void cancelDialog() {
		if (this.isShowing()) {
			this.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		cancelDialog();
	}


	private static class MultiChooseOperateParam{
		//设置第一种选择操作显示文字
		String operateOneStr;
		//设置第二种选择操作显示文字
		String operateTwoStr;
		//文字取消
		String operateCancelStr;
		//点击弹窗外部，是否可以取消弹窗(默认可以取消)
		boolean enableCancel = true;
		//第一种选项和第二种选项的点击事件
		OnChooseClickListener onChooseClickListener;
	}

	public static class Builder{
		private Context mContext;
		private MultiChooseOperateParam multiChooseOperateParam;
		private MultiChooseOperateDialog multiChooseOperateDialog;

		public Builder(Context mContext){
			this.mContext = mContext;
			multiChooseOperateParam = new MultiChooseOperateParam();
		}

		//设置显示文字
		public Builder setShowOperateTvOne(String str){
			multiChooseOperateParam.operateOneStr = str;
			return this;
		}

		public Builder setShowOperateTvTwo(String str){
			multiChooseOperateParam.operateTwoStr = str;
			return this;
		}

		public Builder setShowOperateCancel(String str){
			multiChooseOperateParam.operateCancelStr = str;
			return this;
		}

		public Builder enableCanceledOnTouchOutside(boolean enableCancel) {
			multiChooseOperateParam.enableCancel = enableCancel;
			return this;
		}

		public Builder setOnChooseClickListener(OnChooseClickListener listener) {
			multiChooseOperateParam.onChooseClickListener = listener;
			return this;
		}

		public MultiChooseOperateDialog create(){
			MultiChooseOperateDialog multiChooseOperateDialog = new MultiChooseOperateDialog(mContext);

			multiChooseOperateDialog.setShowOperateTvOne(multiChooseOperateParam.operateOneStr);
			multiChooseOperateDialog.setShowOperateTvTwo(multiChooseOperateParam.operateTwoStr);
			multiChooseOperateDialog.setShowOperateCancel(multiChooseOperateParam.operateCancelStr);
			multiChooseOperateDialog.enableCanceledOnTouchOutside(multiChooseOperateParam.enableCancel);
			multiChooseOperateDialog.setOnChooseClickListener(multiChooseOperateParam.onChooseClickListener);//点击事件处理


			this.multiChooseOperateDialog = multiChooseOperateDialog;
			return multiChooseOperateDialog;
		}

		public MultiChooseOperateDialog create(int layout){
			MultiChooseOperateDialog multiChooseOperateDialog = new MultiChooseOperateDialog(mContext,layout);

			multiChooseOperateDialog.setShowOperateTvOne(multiChooseOperateParam.operateOneStr);
			multiChooseOperateDialog.setShowOperateTvTwo(multiChooseOperateParam.operateTwoStr);
			multiChooseOperateDialog.setShowOperateCancel(multiChooseOperateParam.operateCancelStr);
			multiChooseOperateDialog.enableCanceledOnTouchOutside(multiChooseOperateParam.enableCancel);
			multiChooseOperateDialog.setOnChooseClickListener(multiChooseOperateParam.onChooseClickListener);	//点击事件处理

			this.multiChooseOperateDialog = multiChooseOperateDialog;
			return multiChooseOperateDialog;
		}
	}

	/**
	 * 设置显示文字
	 * @param str
	 */
	private void setShowOperateTvOne(String str){
		if (!TextUtils.isEmpty(str))
			choose_one_tv.setText(str);
	}

	private void setShowOperateTvTwo(String str){
		if (!TextUtils.isEmpty(str))
			choose_two_tv.setText(str);
	}

	private void setShowOperateCancel(String str){
		if (!TextUtils.isEmpty(str))
			choose_cancel.setText(str);
	}

	/**
	 * 点击弹窗外部，是否可以取消弹窗
	 * @param enableCancel true：可以取消(默认可以取消)   false：不可以取消
	 */
	public void enableCanceledOnTouchOutside(boolean enableCancel){
		setCanceledOnTouchOutside(enableCancel);
	}

	public interface OnChooseClickListener {
		void onChooseClick(int choice);
	}

	private void setOnChooseClickListener(final OnChooseClickListener onChooseClickListener) {
		choose_one_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelDialog();
				if (onChooseClickListener != null) {
					onChooseClickListener.onChooseClick(CHOICE_ONE);
				}
			}
		});
		choose_two_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelDialog();
				if (onChooseClickListener != null) {
					onChooseClickListener.onChooseClick(CHOICE_TWO);
				}
			}
		});
	}
}
