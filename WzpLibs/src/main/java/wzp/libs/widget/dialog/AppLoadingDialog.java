package wzp.libs.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import wzp.libs.R;


/**
 * 整个App 加载Dialog
 */
public class AppLoadingDialog extends Dialog {
	private Context mContext;
	/** 加载圈图片*/
	private ImageView app_iv_loading;
	/** 动画 */
	private AnimationDrawable animationDrawable;

	/**
	 * 初始化构造函数(默认dialog_app_loading布局)
	 */
	public AppLoadingDialog(Context mContext) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.mContext = mContext;

		// 绑定Layout
		setLayout(R.layout.dialog_app_loading);
	}

	/**
	 * 初始化构造函数 可以自定义布局（但是自定义布局中的id要与这里设置的一致）
	 */
	public AppLoadingDialog(Context mContext,int layout) {
		super(mContext, R.style.Theme_Light_FullScreenDialogAct);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.mContext = mContext;

		// 绑定Layout
		setLayout(layout);

		setParams();

	}

	public void setLayout(int layout){
		setContentView(layout);

		app_iv_loading = (ImageView) this.findViewById(R.id.app_iv_loading);

		setParams();
	}

	private void setParams(){
		// 设置宽度,高度以及显示的位置
		Window window = AppLoadingDialog.this.getWindow();
		WindowManager.LayoutParams lParams = window.getAttributes();
		try {
			// 设置透明度
			lParams.dimAmount = 0.2f;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 禁止点击其他地方自动关闭
		this.setCanceledOnTouchOutside(false);
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
		// 初始化加载参数
		initLoading();
	}

	/**
	 * 初始化加载参数
	 */
	public void initLoading(){
		app_iv_loading.setBackgroundResource(R.drawable.app_loading);
		animationDrawable = (AnimationDrawable) app_iv_loading.getBackground();
		animationDrawable.start();
	}

	/**
	 * 关闭Dialog
	 */
	public void cancelDialog() {
		if(animationDrawable!=null){
			animationDrawable.stop();
		}
		// 关闭Dialog(防止线程继续跑)
		if (this.isShowing()) {
			this.cancel();
		}
	}
}
