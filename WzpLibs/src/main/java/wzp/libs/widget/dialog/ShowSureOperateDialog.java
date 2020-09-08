package wzp.libs.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import wzp.libs.R;


/**
 * 展示操作Dialog
 * (上面显示要展示的内容，下面确认按钮)
 */
public class ShowSureOperateDialog extends Dialog{
	private Context mContext;
	//=============  控件  =========
	/** 操作提示 */
	private TextView show_operate_notice;
	/** 提示文字 */
	private TextView show_operate_content;
	/** 具体操作 - 确定 */
	private TextView show_operate_sure;

	/**
	 * 初始化构造函数
	 */
	public ShowSureOperateDialog(Context mContext) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;

		setLayout(R.layout.dialog_show_sure_operate);
	}

	public ShowSureOperateDialog(Context mContext, int layout) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;
		setLayout(layout);
	}


	public void setLayout(int layout){
		// 绑定Layout
		setContentView(layout);

		// 设置宽度,高度以及显示的位置
		Window window = ShowSureOperateDialog.this.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lParams = window.getAttributes();
		lParams.width = (int)(((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth() * 0.7) ;
		lParams.alpha = 1.0f;

		// ==------------------------------------------==

		show_operate_notice =  this.findViewById(R.id.show_operate_notice);
		show_operate_content =  this.findViewById(R.id.show_operate_content);
		show_operate_sure =  this.findViewById(R.id.show_operate_sure);

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

	private static class ShowOperateParam{
		//温馨提示 文字
		String noticeStr;
		//提示的具体内容
		String contentStr;
		//文字确定
		String sureStr;
		//点击弹窗外部，是否可以取消弹窗(默认可以取消)
		boolean enableCancel = true;
		//点击确定
		OnSureClickListener onSureClickListener;
	}

	public static class Builder{
		private Context mContext;
		private ShowOperateParam showOperateParam;
		private ShowSureOperateDialog showOperateDialog;

		public Builder(Context mContext){
			this.mContext = mContext;
			showOperateParam = new ShowOperateParam();
		}

		public Builder setNoticeStr(String str){
			showOperateParam.noticeStr = str;
			return this;
		}

		public Builder setContentStr(String str){
			showOperateParam.contentStr = str;
			return this;
		}

		public Builder setSureStr(String str){
			showOperateParam.sureStr = str;
			return this;
		}

		public Builder enableCanceledOnTouchOutside(boolean enableCancel) {
			showOperateParam.enableCancel = enableCancel;
			return this;
		}

		public Builder setOnSureClickListener(OnSureClickListener listener) {
			showOperateParam.onSureClickListener = listener;
			return this;
		}

		public ShowSureOperateDialog create(){
			ShowSureOperateDialog showOperateDialog = new ShowSureOperateDialog(mContext);

			showOperateDialog.setNoticeStr(showOperateParam.noticeStr);
			showOperateDialog.setContentStr(showOperateParam.contentStr);
			showOperateDialog.setSureStr(showOperateParam.sureStr);
			showOperateDialog.enableCanceledOnTouchOutside(showOperateParam.enableCancel);
			showOperateDialog.setOnSureClickListener(showOperateParam.onSureClickListener);//点击事件处理


			this.showOperateDialog = showOperateDialog;
			return showOperateDialog;
		}

		public ShowSureOperateDialog create(int layout){
			ShowSureOperateDialog showOperateDialog = new ShowSureOperateDialog(mContext,layout);

			showOperateDialog.setNoticeStr(showOperateParam.noticeStr);
			showOperateDialog.setContentStr(showOperateParam.contentStr);
			showOperateDialog.setSureStr(showOperateParam.sureStr);
			showOperateDialog.enableCanceledOnTouchOutside(showOperateParam.enableCancel);
			showOperateDialog.setOnSureClickListener(showOperateParam.onSureClickListener);	//点击事件处理

			this.showOperateDialog = showOperateDialog;
			return showOperateDialog;
		}
	}

	/**
	 * 设置显示文字
	 * @param str
	 */
	private void setNoticeStr(String str){
		if (!TextUtils.isEmpty(str))
			show_operate_notice.setText(str);
	}

	private void setContentStr(String str){
		if (!TextUtils.isEmpty(str))
			show_operate_content.setText(str);
	}

	private void setSureStr(String str){
		if (!TextUtils.isEmpty(str))
			show_operate_sure.setText(str);
	}

	/**
	 * 点击弹窗外部，是否可以取消弹窗
	 * @param enableCancel true：可以取消(默认可以取消)   false：不可以取消
	 */
	public void enableCanceledOnTouchOutside(boolean enableCancel){
		setCanceledOnTouchOutside(enableCancel);
	}

	public interface OnSureClickListener {
		void onSureClick();
	}

	private void setOnSureClickListener(final OnSureClickListener onSureClickListener) {
		show_operate_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelDialog();
				if (onSureClickListener != null) {
					onSureClickListener.onSureClick();
				}
			}
		});
	}
}
