package wzp.kits.avatar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.kits.cycle.CycleViewPagerActivity;
import wzp.kits.gif.LoadingGifActivity;
import wzp.kits.guide.Guide1Activity;
import wzp.kits.guide.Guide2Activity;
import wzp.kits.photoview.PhotoActivity;
import wzp.kits.scan.RelatedCodeActivity;
import wzp.kits.use.widget.WidgetUseActivity;
import wzp.libs.function.avatar.TakePicAlbum;
import wzp.libs.utils.FileUtils;
import wzp.libs.utils.GlideUtils;
import wzp.libs.utils.SDCardUtils;
import wzp.libs.utils.ToastUtils;
import wzp.libs.widget.CircleImageView;
import wzp.libs.widget.dialog.MultiChooseOperateDialog;

/**
 * 拍照从相册中选择
 */
public class ChangeAvatarActivity extends BaseActivity {

    @BindView(R.id.change_avatar)
    CircleImageView change_avatar;

    /** 用户信息文件路径 */
    public static final String BASE_UDATA_PATH = File.separator + "uData" + File.separator;
    /** 用户头像文件地址 */
    public static final String AP_UHEAD_PATH = BASE_UDATA_PATH + "uHead" + File.separator;
    /** 用户头像文件名 */
    public static final String AVATAR = "avatar" + ".png"; //这里要加后缀，要不然上传图片不成功

    @Override
    protected int getLayout() {
        return R.layout.activity_change_avatar;
    }

    @Override
    protected void initListener() {
        change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiChooseOperateDialog.Builder(mContext)
                        .setOnChooseClickListener(new MultiChooseOperateDialog.OnChooseClickListener() {
                            @Override
                            public void onChooseClick(int choice) {
                                if (choice == MultiChooseOperateDialog.CHOICE_ONE){
                                    TakePicAlbum.cameraSwitch(mContext,true,getFileCache(mContext,AP_UHEAD_PATH,AVATAR));
                                }else{
                                    TakePicAlbum.cameraSwitch(mContext,false,getFileCache(mContext,AP_UHEAD_PATH,AVATAR));
                                }
                            }
                        })
                        .create().showDialog();
            }
        });

        TakePicAlbum.setOnGetAvatarListener(new TakePicAlbum.OnGetAvatarListener() {
            @Override
            public void getAvatar(File file) {
                //展示头像
                GlideUtils.getInstance().loadPic(mContext,file.getAbsolutePath(),change_avatar,R.drawable.ic_default_avatar);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TakePicAlbum.onActivityResult(mContext,requestCode,resultCode,data);
    }


    /**
     * 获取用户头像缓存地址
     * @param mContext 上下文
     * @param fName 文件名
     * @return  /storage/emulated/0/Android/data/wzp.kits/cache/uData/uHead/avatar   uHead为文件夹  avatar为头像图片
     */
    public static File getFileCache(Context mContext, String path, String fName){
        // 获取头像存储地址
        String vpCache = SDCardUtils.getCacheDirPath(mContext) + path;
        // 防止不存在目录文件，自动创建
        FileUtils.createFolder(vpCache);
        // 返回头像地址
        return new File(vpCache + fName);
    }
}

