package wzp.libs.function.avatar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wzp.libs.utils.FileUtils;
import wzp.libs.utils.LogUtils;
import wzp.libs.utils.PermissionUtils;
import wzp.libs.utils.SDCardUtils;
import wzp.libs.utils.ToastUtils;

/**
 * 拍照从相册中选择
 */
public class TakePicAlbum {

    /**
     * Don't let anyone instantiate this class.
     */
    private TakePicAlbum() {
        throw new Error("Do not need instantiate!");
    }

    // == ----------------------------------------- ==

    /** 用户信息文件路径 */
    public static final String BASE_UDATA_PATH = File.separator + "uData" + File.separator;
    /**  */
    public static final String AP_UHEAD_PATH = BASE_UDATA_PATH + "uHead" + File.separator;
    /**  */
    public static final String AVATAR = "avatar" + ".png"; //这里要加后缀，要不然上传图片不成功

    //拍照
    private static final int CHOOSE_CAMERA = 1;
    //从相册中选择
    private static final int CHOOSE_ALBUM = 2;
    //裁剪，处理结果
    private static final int RESULT_ZOOM = 3;

    /**
     * 相机选择
     * @param isCamera 是否选择拍照  true:拍照   false:从相片中选择
     */
    public static void cameraSwitch(final Context mContext, boolean isCamera){
        // 获取用户头像地址
        //uHeadFile:/storage/emulated/0/Android/data/wzp.kits/cache/uData/uHead/avatar
        //创建了名为uHead文件夹(创建的位置为上面的路径)
        final File uHeadFile = getFileCache(mContext, AP_UHEAD_PATH , AVATAR);
        LogUtils.d("删除前-uHeadFile:" + uHeadFile.toString());
        //删除uHead文件夹下面的头像（名字为avatar的图片）
        uHeadFile.delete();
        LogUtils.d("删除后-uHeadFile:" + uHeadFile.toString());
        //拍照逻辑
        if(isCamera){ // 拍照
            //如果编译版本大于或者等于23（6.0）的话，调用系统的拍照权限是要动态申请权限的
            //判断是否授予权限
            boolean grantedCamera = PermissionUtils.isGranted(mContext, Manifest.permission.CAMERA);
            //如果没有授予权限 ，则去动态申请权限
            if(!grantedCamera){
                PermissionUtils.permission(Manifest.permission.CAMERA).callBack(new PermissionUtils.PermissionCallBack() {
                    @Override
                    public void onGranted(PermissionUtils permissionUtils) {
                        toCamera(mContext,uHeadFile);
                    }
                    @Override
                    public void onDenied(PermissionUtils permissionUtils) {
                        ToastUtils.showToast(mContext,"拒绝授权无法使用该功能,请打开相机权限");
                    }
                }).request(mContext);
            }else{
                toCamera(mContext,uHeadFile);
            }

        } else { //从手机相册选择
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            ((Activity)mContext).startActivityForResult(intent, CHOOSE_ALBUM);
        }
    }

    public static void toCamera(Context mContext,File uHeadFile){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用系统相机拍照
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(uHeadFile));
        if (Build.VERSION.SDK_INT >= 24) { //Android 7.0
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getPackageName() + ".fileProvider", uHeadFile));//对应的就是清单文件中的authorities
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(uHeadFile));
        }
        ((Activity)mContext).startActivityForResult(intent, CHOOSE_CAMERA);
    }


    public static void onActivityResult(Context mContext,int requestCode, int resultCode, @Nullable Intent data) {
        // 判断是否请求成功
        boolean isResultOk = (Activity.RESULT_OK == resultCode);
        // 获取请求code
        switch(requestCode){
            case CHOOSE_CAMERA://拍照
                if (isResultOk) {
                    // 获取用户头像地址
                    File uHeadFile = getFileCache(mContext,AP_UHEAD_PATH, AVATAR);
                    // 保存的路径
                    Uri saveUri = Uri.fromFile(uHeadFile);
                    //startPhotoZoom(saveUri, saveUri);
                    startPhotoZoom(mContext,FileUtils.fileToUri(mContext,uHeadFile), saveUri);
                }
                break;
            case CHOOSE_ALBUM://从相册中选择
                File headFile = getFileCache(mContext,AP_UHEAD_PATH,AVATAR);
                // 保存的路径
                Uri savUri = Uri.fromFile(headFile);
                if(data != null){
                    startPhotoZoom(mContext,data.getData(), savUri); // 读取相册缩放图片
                    LogUtils.d("data:" + data.getData());
                }
                break;
            case RESULT_ZOOM:// 裁剪 处理结果
                // 获取用户头像地址
                File uHeadFile = getFileCache(mContext, AP_UHEAD_PATH,AVATAR);
                if(uHeadFile!=null&&uHeadFile.exists()){
                    if (onGetAvatarListener!=null){
                        onGetAvatarListener.getAvatar(uHeadFile);
                    }
                }
                break;
        }
    }

    /**
     * 收缩图片
     * @param uri
     * @param saveUri 缩略图输出地址
     */
    private static void startPhotoZoom(Context mContext,Uri uri, Uri saveUri) {
        //调用系统裁剪  需要设置图片来源 intent.setDataAndType(uri, "image/*");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //这句很重要，没加的话会提示“无法加载此图片”
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //crop=true:开启裁剪功能
        intent.putExtra("crop", "true");
        // aspectX aspectY :裁剪区的宽高比(1:1为一个正方形，1:2为一个长方形)
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY  输出图片大小 ============
        //这2句的作用：如果MainActivity中ImageView的布局是wrap wrap的,如果没有设置这2句的话，那么在MainActivity中展示的图片可能一张突然大可能一张突然很小
        //设置这2句的话，MainActivity中图片大小的展示就是这里设定的大小（固定的）
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        // 是否保留比例
        intent.putExtra("scale", true);
        // 去黑边
        intent.putExtra("scaleUpIfNeeded", true);
        // 返回图片类型(格式)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 不进行人脸识别
        intent.putExtra("noFaceDetection", true);
        //是否返回bitmap对象(是否将数据保留在Bitmap中返回)
        intent.putExtra("return-data", false); // 设置不返回数据
        //圆形裁剪区域
        //intent.putExtra("circleCrop", true);
        // 设置保存的地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        ((Activity)mContext).startActivityForResult(intent, RESULT_ZOOM);
    }

    /**
     * 获取用户头像缓存地址
     * @param mContext 上下文
     * @param fName 文件名
     * @return
     */
    public static File getFileCache(Context mContext,String path, String fName){
        // 获取头像存储地址
        String vpCache = SDCardUtils.getCacheDirPath(mContext) + path;
        // 防止不存在目录文件，自动创建
        FileUtils.createFolder(vpCache);
        // 返回头像地址
        return new File(vpCache + fName);
    }

    private static OnGetAvatarListener onGetAvatarListener;

    public interface OnGetAvatarListener{
        void getAvatar(File file);
    }

    public static void setOnGetAvatarListener(OnGetAvatarListener getAvatarListener){
        onGetAvatarListener = getAvatarListener;
    }
}
