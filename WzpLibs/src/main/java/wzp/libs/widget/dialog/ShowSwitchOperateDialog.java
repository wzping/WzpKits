package wzp.libs.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import wzp.libs.R;
import wzp.libs.utils.SpanUtils;


/**
 * 选择操作Dialog
 * (上面显示要展示的内容，下面一个取消按钮，一个确认按钮)
 */
public class ShowSwitchOperateDialog extends Dialog{
	private Context mContext;
	//=============  控件  =========
	/** 操作的提示 */
	private TextView operate_notice;
	/** 操作的提示内容 */
	private TextView operate_content;
	/** 取消 */
	private TextView operate_cancel;
	/** 确定 */
	private TextView operate_sure;
	//点击选择了 - 取消
	public static final int SWITCH_OPERATE_CANCLE = 1;
	//点击选择了 - 确定
	public static final int SWITCH_OPERATE_SURE = 2;

	/**
	 * 初始化构造函数
	 */
	public ShowSwitchOperateDialog(Context mContext) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;
		setLayout(R.layout.dialog_show_switch_operate);
	}

	public ShowSwitchOperateDialog(Context mContext, int layout) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;
		setLayout(layout);
	}

	public ShowSwitchOperateDialog(Context mContext, int layout,int dialogWidth) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;
		setLayout(layout,dialogWidth);
	}


	public void setLayout(int layout){
		// 绑定Layout
		setContentView(layout);

		// 设置宽度,高度以及显示的位置
		Window window = ShowSwitchOperateDialog.this.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lParams = window.getAttributes();
		lParams.width = (int)(((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth() * 0.7);
		lParams.alpha = 1.0f;

		operate_notice =  this.findViewById(R.id.operate_notice);
		operate_content =  this.findViewById(R.id.operate_content);
		operate_cancel =  this.findViewById(R.id.operate_cancel);
		operate_sure =  this.findViewById(R.id.operate_sure);

	}

	public void setLayout(int layout,int dialogWidth){
		// 绑定Layout
		setContentView(layout);

		// 设置宽度,高度以及显示的位置
		Window window = ShowSwitchOperateDialog.this.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lParams = window.getAttributes();
		lParams.width = dialogWidth;
		lParams.alpha = 1.0f;

		operate_notice =  this.findViewById(R.id.operate_notice);
		operate_content =  this.findViewById(R.id.operate_content);
		operate_cancel =  this.findViewById(R.id.operate_cancel);
		operate_sure =  this.findViewById(R.id.operate_sure);

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

	private static class SwitchOperateParam{
		//设置操作提示
		String operateNoticeStr;
		//设置操作提示文字显示
		String operateContentStr;
		//取消文字
		String operateCancelStr;
		//确定文字
		String operateSureStr;
		//点击弹窗外围，是否能够取消弹窗(默认是可以取消的)
		boolean canceledOnTouchOutside = true;
		//取消和确定的点击事件
		OnOperateClickListener onOperateClickListener;
		//系统监听（当弹窗显示的时候，点击系统返回键）
		OnBackListener onBackListener;
	}

	public static class Builder{
		private Context mContext;
		private SwitchOperateParam switchOperateParam;
		private ShowSwitchOperateDialog switchOperateDialog;

		public Builder(Context mContext){
			this.mContext = mContext;
			switchOperateParam = new SwitchOperateParam();
		}

		//设置显示文字
		public Builder setOperateNoticeStr(String str){
			switchOperateParam.operateNoticeStr = str;
			return this;
		}

		public Builder setOperateContentStr(String str){
			switchOperateParam.operateContentStr = str;
			return this;
		}

		public Builder setOperateCancelStr(String str){
			switchOperateParam.operateCancelStr = str;
			return this;
		}

		public Builder setOperateSureStr(String str){
			switchOperateParam.operateSureStr = str;
			return this;
		}

		public Builder setCancelOnTouchOutside(boolean b){
			switchOperateParam.canceledOnTouchOutside = b;
			return this;
		}

		public Builder setOnOperateClickListener(OnOperateClickListener listener) {
			switchOperateParam.onOperateClickListener = listener;
			return this;
		}

		public Builder setOnBackListener(OnBackListener listener) {
			switchOperateParam.onBackListener = listener;
			return this;
		}

		public ShowSwitchOperateDialog create(){
			ShowSwitchOperateDialog switchOperateDialog = new ShowSwitchOperateDialog(mContext);

			switchOperateDialog.setOperateNoticeStr(switchOperateParam.operateNoticeStr);
			switchOperateDialog.setOperateContentStr(switchOperateParam.operateContentStr);
			switchOperateDialog.setOperateCancelStr(switchOperateParam.operateCancelStr);
			switchOperateDialog.setOperateSureStr(switchOperateParam.operateSureStr);
			switchOperateDialog.setCancelOnTouchOutside(switchOperateParam.canceledOnTouchOutside);
			switchOperateDialog.setOnOperateClickListener(switchOperateParam.onOperateClickListener);
			switchOperateDialog.setOnBackListener(switchOperateParam.onBackListener);

			this.switchOperateDialog = switchOperateDialog;
			return switchOperateDialog;
		}

		public ShowSwitchOperateDialog create(int layout){
			ShowSwitchOperateDialog switchOperateDialog = new ShowSwitchOperateDialog(mContext,layout);

			switchOperateDialog.setOperateNoticeStr(switchOperateParam.operateNoticeStr);
			switchOperateDialog.setOperateContentStr(switchOperateParam.operateContentStr);
			switchOperateDialog.setOperateCancelStr(switchOperateParam.operateCancelStr);
			switchOperateDialog.setOperateSureStr(switchOperateParam.operateSureStr);
			switchOperateDialog.setCancelOnTouchOutside(switchOperateParam.canceledOnTouchOutside);
			switchOperateDialog.setOnOperateClickListener(switchOperateParam.onOperateClickListener);
			switchOperateDialog.setOnBackListener(switchOperateParam.onBackListener);

			this.switchOperateDialog = switchOperateDialog;
			return switchOperateDialog;
		}

		public ShowSwitchOperateDialog create(int layout,int dialogWidth){
			ShowSwitchOperateDialog switchOperateDialog = new ShowSwitchOperateDialog(mContext,layout,dialogWidth);

			switchOperateDialog.setOperateNoticeStr(switchOperateParam.operateNoticeStr);
			switchOperateDialog.setOperateContentStr(switchOperateParam.operateContentStr);
			switchOperateDialog.setOperateCancelStr(switchOperateParam.operateCancelStr);
			switchOperateDialog.setOperateSureStr(switchOperateParam.operateSureStr);
			switchOperateDialog.setCancelOnTouchOutside(switchOperateParam.canceledOnTouchOutside);
			switchOperateDialog.setOnOperateClickListener(switchOperateParam.onOperateClickListener);
			switchOperateDialog.setOnBackListener(switchOperateParam.onBackListener);

			this.switchOperateDialog = switchOperateDialog;
			return switchOperateDialog;
		}
	}

	/**
	 * 设置显示文字
	 * @param str
	 */
	private void setOperateNoticeStr(String str){
		if (!TextUtils.isEmpty(str))
			operate_notice.setText(str);
	}


	private void setOperateContentStr(String str){
		if (!TextUtils.isEmpty(str))
			operate_content.setText(str);
	}

	public SpanUtils getOperateContentSpan(){
		return SpanUtils.with(operate_content);
	}

	private void setOperateCancelStr(String str){
		if (!TextUtils.isEmpty(str))
			operate_cancel.setText(str);
	}

	private void setOperateSureStr(String str){
		if (!TextUtils.isEmpty(str))
			operate_sure.setText(str);
	}

	private void setCancelOnTouchOutside(boolean b){
		setCanceledOnTouchOutside(b);
	}

	public interface OnOperateClickListener {
		void onOperateClick(int operate);
	}

	private void setOnOperateClickListener(final OnOperateClickListener onOperateClickListener) {
		operate_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelDialog();
				if (onOperateClickListener != null) {
					onOperateClickListener.onOperateClick(SWITCH_OPERATE_CANCLE);
				}
			}
		});
		operate_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelDialog();
				if (onOperateClickListener != null) {
					onOperateClickListener.onOperateClick(SWITCH_OPERATE_SURE);
				}
			}
		});
	}


	public interface OnBackListener {
		void onBack();
	}

	private void setOnBackListener(final OnBackListener onBackListener){
		//setOnKeyListener - 系统监听（当弹窗显示的时候，点击系统返回键）
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
					cancelDialog();
					if (onBackListener!=null){
						onBackListener.onBack();
					}
				}
				return false;
			}
		});
	}

}
