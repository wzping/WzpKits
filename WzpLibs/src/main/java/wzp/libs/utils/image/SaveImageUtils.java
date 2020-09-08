package wzp.libs.utils.image;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import wzp.libs.utils.LogUtils;


/**
 * 保存图片
 */
public class SaveImageUtils {

	private SaveImageUtils() {
	}

	// 日志Tag
	private static final String TAG = SaveImageUtils.class.getSimpleName();

	/**
	 * 保存图片 - PNG
	 * @param bitmap 需要保存的数据
	 * @param path 保存路径
	 * @param quality 压缩比例
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path, int quality) {
		return saveBitmap(bitmap, path, Bitmap.CompressFormat.PNG, quality);
	}

	/**
	 * 保存图片
	 * @param bitmap 图片资源
	 * @param path 保存路径
	 * @param format 如 Bitmap.CompressFormat.PNG
	 * @param quality 保存的图片质量， 100 则完整质量不压缩保存
	 * @return 保存结果
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path, Bitmap.CompressFormat format, int quality) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			if (fos != null) {
				bitmap.compress(format, quality, fos);
				fos.close();
			}
		} catch (Exception e) {
			LogUtils.e(TAG, "异常：" + e.getMessage());
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LogUtils.e(TAG, "异常：" + e.getMessage());
				}
			}
		}
		return true;
	}

	/**
	 * 下载(网络图片)保存到本地（不改变原(图片)大小,原来是多大,下载下来后也是多大）
	 * @param mContext
	 * @param picPath 网络(图片)下载地址
	 * @param file  下载保存的文件   new File("/storage/emulated/0/kid.jpg")  或者  new File("/storage/emulated/0","kid.jpg") 都可以  --> 下载的图片命名为kid.jpg,保存到/storage/emulated/0路径下，也可以不写后缀
	 * @return
	 */
	public static File downloadToLocal(Context mContext, String picPath, File file){
		try {
			// 统一资源
			URL url = new URL(picPath);
			// 连接类的父类，抽象类
			URLConnection urlConnection = url.openConnection();
			// http的连接类
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			// 设定请求的方法，默认是GET
			httpURLConnection.setRequestMethod("GET");
			// 设置字符编码
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
			httpURLConnection.connect();

			BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());  //读取
			OutputStream out = new FileOutputStream(file); //写入到文件内
			int size = 0;
			int len = 0;
			byte[] buf = new byte[1024];
			while ((size = bin.read(buf)) != -1) {
				len += size;
				out.write(buf, 0, size);
				// 打印下载百分比
				//System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
			}
			bin.close();
			out.close();
		} catch (Exception e) {   // 这里要开线程运行，否则会报异常android.os.NetworkOnMainThreadException
			e.printStackTrace();
		} finally {
			return file;
		}
	}
}
