package wzp.libs.utils;

import java.util.HashMap;


/**
 * 视频相关工具类
 */
public class VideoUtils {

	/**
	 * 获取视频总时长
	 * @param url  视频url地址
	 * @return  视频总时长
	 */
	public static String getVideoDuration(String url) {
		String duration = null;
		android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
		try {
			if (url != null) {
				HashMap<String, String> headers = null;
				if (headers == null) {
					headers = new HashMap<String, String>();
				}
				mmr.setDataSource(url, headers);
			}
			duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mmr.release();
		}
		return duration;
	}
}
