package wzp.libs.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import wzp.libs.R;


/**
 * 展示操作Dialog
 * (上面显示要展示的内容，下面确认按钮)
 */
public class ShowOperateDialog extends Dialog implements View.OnClickListener {
	private Context mContext;
	//=============  控件  =========
	/** 操作提示 */
	private TextView sure_dialog_text;
	/** 提示文字 */
	private TextView sure_dialog_notice;
	/** 中间的那根横线 */
	private ImageView sure_dialog_line;
	/** 具体操作 */
	private TextView sure_dialog_sure;
	//操作提示
	private String text;
	//提示文字
	private String notice;
	//自定义的布局文件
	private int layout;

	/**
	 * 初始化构造函数
	 */
	public ShowOperateDialog(Context mContext) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;

		setLayout(R.layout.dialog_show_operate);
	}

	public ShowOperateDialog(Context mContext, int layout) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		this.mContext = mContext;
		this.layout = layout;
		setLayout(layout);
	}


	public void setLayout(int layout){
		// 绑定Layout
		setContentView(layout);

		// 设置宽度,高度以及显示的位置
		Window window = ShowOperateDialog.this.getWindow();
		window.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lParams = window.getAttributes();
		lParams.width = (int)(((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth() * 0.7) ;

		// ==------------------------------------------==

		sure_dialog_text =  this.findViewById(R.id.sure_dialog_text);
		sure_dialog_notice =  this.findViewById(R.id.sure_dialog_notice);
		sure_dialog_line =  this.findViewById(R.id.sure_dialog_line);
		sure_dialog_sure =  this.findViewById(R.id.sure_dialog_sure);

		text = sure_dialog_text.getText().toString();

		sure_dialog_sure.setOnClickListener(this);
	}

	/**
	 * 显示Dialog
	 */
	public void showDialog(String notice){
		this.notice = notice;
		// 显示Dialog
		this.show();
	}

	/**
	 * 显示Dialog
	 */
	public void showDialog(String text,String notice){
		this.text = text;
		this.notice = notice;
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

		sure_dialog_text.setText(text);
		sure_dialog_notice.setText(notice);
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
		int id = v.getId();
		if (id==R.id.sure_dialog_sure){
			cancelDialog();
			if(mOnSureClickListener!=null){
				mOnSureClickListener.onSureClick();
			}
		}
	}


	private OnSureClickListener mOnSureClickListener;


	public interface OnSureClickListener {
		void onSureClick();
	}

	public void setOnSureClickListener(OnSureClickListener mOnSureClickListener) {
		this.mOnSureClickListener = mOnSureClickListener;
	}

	/**
	 * 设置"操作提醒"是否可见
	 * @param visible
	 */
	public void setTextVisible(int visible){
		sure_dialog_text.setVisibility(visible);
	}


	/**
	 * 点击弹窗外部，是否可以取消弹窗
	 * @param enableCancel true：可以取消(默认可以取消)   false：不可以取消
	 */
	public void enableCanceledOnTouchOutside(boolean enableCancel){
		setCanceledOnTouchOutside(enableCancel);
	}
}
