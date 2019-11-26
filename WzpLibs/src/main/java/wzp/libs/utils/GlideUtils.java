package wzp.libs.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import wzp.libs.R;

/**
 * Glide图片加载工具类
 */
public class GlideUtils {

	private static GlideUtils glideUtils;


	public static GlideUtils getInstance() {
		if (glideUtils == null) {
			glideUtils = new GlideUtils();
		}
		return glideUtils;
	}


	//---------------------------------

	public void loadPic(Context mContext, String url, ImageView iv){
		loadPic(mContext, url, iv, R.drawable.ic_error1);
	}

	//加载图片，跳过缓存(每次都会重新加载)
	public void loadPic(Context mContext, String url, ImageView iv,int defaultPic){

		RequestOptions requestOptions = new RequestOptions()
				.skipMemoryCache(true)//跳过内存缓存
				.diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存
				.placeholder(defaultPic) //加载成功前显示的图片
				.fallback(defaultPic) //url为空的时候,显示的图片
				.error(defaultPic); //异常时候显示的图片

		Glide.with(mContext).load(url)
				.apply(requestOptions)
				.into(iv); //加载显示图片
	}


	//---------------------------------


	public void loadPicList(Context mContext, String url, ImageView iv){
		loadPicList(mContext, url, iv, R.drawable.ic_error1);
	}

	//加载图片,不跳过缓存（只要加载的链接不变，就不会再重新加载了）
	public void loadPicList(Context mContext, String url, ImageView iv,int defaultPic){

		RequestOptions requestOptions = new RequestOptions()
				.placeholder(defaultPic) //加载成功前显示的图片
				.fallback(defaultPic) //url为空的时候,显示的图片
				.error(defaultPic) ;//异常时候显示的图片

		Glide.with(mContext).load(url)
				.apply(requestOptions)
				.into(iv); //加载显示图片
	}


	//添加加载图片回调监听
	public void loadPicList(Context mContext, String url, ImageView iv,RequestListener<Drawable> requestListener){
		loadPicList(mContext, url, iv, requestListener,R.drawable.ic_error1);
	}

	//加载图片,不跳过缓存（只要加载的链接不变，就不会再重新加载了）
	public void loadPicList(Context mContext, String url, ImageView iv,RequestListener<Drawable> requestListener,int defaultPic){

		RequestOptions requestOptions = new RequestOptions()
				.placeholder(defaultPic) //加载成功前显示的图片
				.fallback(defaultPic) //url为空的时候,显示的图片
				.error(defaultPic) ;//异常时候显示的图片

		Glide.with(mContext).load(url)
				.apply(requestOptions)
				.listener(requestListener)
				.into(iv); //加载显示图片
	}
}
