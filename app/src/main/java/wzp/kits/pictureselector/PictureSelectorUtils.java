package wzp.kits.pictureselector;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;


public class PictureSelectorUtils {


    /**
     * 打开相册(仅显示图片)
     * @param activity
     * @param selectList 已经选择的相册图片
     * @param maxNum
     */
    public static void showAlbum(Activity activity, List<LocalMedia> selectList, int maxNum){
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(false) //不显示拍照按钮
                .selectionMedia(selectList)
                .maxSelectNum(maxNum) //最大图 片选择数量    这里不设置的话会提示最大九张
                .compress(true) //是否压缩
                .minimumCompressSize(1024) //小于1024KB（1M）的图片不压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    //打开相册(显示图片和视频)
    public static void showAlbumVideo(Activity activity, List<LocalMedia> selectList, int maxNum){
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll())
                .isCamera(false) //不显示拍照按钮
                .selectionMedia(selectList)
                .maxSelectNum(maxNum) //最大图片选择数量
                .compress(true) //是否压缩
                .minimumCompressSize(1024) //小于1024KB（1M）的图片不压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }
}
