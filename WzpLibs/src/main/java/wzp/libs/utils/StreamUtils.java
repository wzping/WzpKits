package wzp.libs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流相关工具类
 */
public class StreamUtils {

	/**
	 * 读取数据流返回一个字符串
	 * @param is 获取的流
	 * @return 字符串 解析失败返回空;
	 */
	public static String streamToString(InputStream is){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while(( len = is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
			}
			is.close();
			String result = baos.toString();
			//智能识别网页编码
			if(result.contains("gb2312")){
				return baos.toString("gb2312");
			}else{
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 读取数据流返回Bitmap
	 * @param is 获取的流
	 * @return Bitmap
	 */
	public static Bitmap streamToBitmap(InputStream is){
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * 读取数据流转换成 byte[]
	 * @param inputStream InputStream
	 * @return Byte数组
	 */
	public static byte[] streamToByteArray(InputStream inputStream) {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toByteArray();
	}
}
