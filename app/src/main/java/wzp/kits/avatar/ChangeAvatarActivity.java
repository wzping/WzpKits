package wzp.kits.avatar;

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
import wzp.libs.utils.GlideUtils;
import wzp.libs.utils.ToastUtils;
import wzp.libs.widget.CircleImageView;
import wzp.libs.widget.dialog.MultiChooseOperateDialog;

/**
 * 拍照从相册中选择
 */
public class ChangeAvatarActivity extends BaseActivity {

    @BindView(R.id.change_avatar)
    CircleImageView change_avatar;

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
                                    TakePicAlbum.cameraSwitch(mContext,true);
                                }else{
                                    TakePicAlbum.cameraSwitch(mContext,false);
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
}

