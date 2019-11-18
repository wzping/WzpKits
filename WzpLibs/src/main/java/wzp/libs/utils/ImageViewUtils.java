package wzp.libs.utils;


import android.content.Context;
import android.widget.ImageView;

import wzp.libs.R;


/**
 * ImageView 相关的工具类
 */
public class ImageViewUtils {
	//日志
	private static final String TAG = ImageViewUtils.class.getSimpleName();

	/**
	 * 获取ImageView控件
	 * @param mContext 上下文
	 * @param resId 要展示的本地资源图片
	 * @return ImageView控件
	 */
	public static ImageView getImageView(Context mContext, int resId){
		return getImageView(mContext,resId, ImageView.ScaleType.FIT_XY);
	}


	public static ImageView getImageView(Context mContext, int resId, ImageView.ScaleType scaleType){
		ImageView image = new ImageView(mContext);
		image.setScaleType(scaleType);
		image.setBackgroundResource(resId);
		return image;
	}

	/**
	 * 获取ImageView控件
	 * @param mContext 上下文
	 * @param url 要展示的网络资源图片
	 * @return ImageView控件
	 */
	public static ImageView getImageView(Context mContext,String url){
		return getImageView(mContext,url,ImageView.ScaleType.FIT_XY);
	}


	public static ImageView getImageView(Context mContext,String url,ImageView.ScaleType scaleType){
		return getImageView(mContext,url,scaleType, R.drawable.ic_error1);
	}


	public static ImageView getImageView(Context mContext,String url,ImageView.ScaleType scaleType,int defaultPic){
		ImageView image = new ImageView(mContext);
		image.setScaleType(scaleType);
		GlideUtils.getInstance().loadPicList(mContext,url,image,defaultPic);
		return image;
	}
}