package wzp.libs.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * SD卡相关辅助类
 */
public final class SDCardUtils {
	
	/**
     * Don't let anyone instantiate this class.
     */
    private SDCardUtils() {
        throw new Error("Do not need instantiate!");
    }
    
	// == ----------------------------------------- ==

	/**
	 * 手机的内置内存卡是SD卡，外置的是TF卡
	 * 内置的，就好比你买手机，就已经有的大小如64G
	 * 这个是内置的，已经弄在主板了
	 * 这个判断就是判断你这个内置卡，是否挂载  一般都是正常挂载的
	 */
	/**
	 * 判断SDCard是否正常挂载
	 * @return
	 */
	public static boolean isSDCardMounted() {
		// android.os.Environment
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡路径（File对象）
	 * @return
	 */
	public static File getSDCartPathFile(){
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * 获取SD卡路径
	 * @return  /storage/emulated/0
	 */
	public static String getSDCartPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取缓存地址的路径
	 * @param mContext
	 * @return
	 */
	public static String getCacheDirPath(Context mContext){
		String cachePath;
		if (isSDCardMounted()) {
			cachePath = mContext.getExternalCacheDir().getPath();  //   /storage/emulated/0/Android/data/wzp.utilkits/cache
		} else {
			cachePath = mContext.getCacheDir().getPath();  //  /data/data/wzp.utilkits/cache
		}
		// 防止不存在目录文件，自动创建
		FileUtils.createFolder(cachePath);
		// 返回文件存储地址
		return cachePath;
	}
}

