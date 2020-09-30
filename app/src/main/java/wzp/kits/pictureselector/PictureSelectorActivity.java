package wzp.kits.pictureselector;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.utils.LogUtils;
import wzp.libs.widget.adapter.RvBaseViewHolder;


public class PictureSelectorActivity extends BaseActivity {

    @BindView(R.id.pic_selector_recyclerview)
    RecyclerView pic_selector_recyclerview;
    /** adapter */
    public PictureSelectorAdapter pictureSelectorAdapter;
    /** 选择的图片数据 */
    private List<LocalMedia> selectList = new ArrayList<>();
    /** 相册最大图片选择量 */
    private int maxNum = 10;
    /** 每行展示图片的列数 */
    private int columnCount = 6;

    @Override
    protected int getLayout() {
        return R.layout.activity_picture_selector;
    }

    @Override
    protected void initValues() {
        pictureSelectorAdapter = new PictureSelectorAdapter(mContext,columnCount);
        pictureSelectorAdapter.setImageMax(maxNum);
        pic_selector_recyclerview.setLayoutManager(new GridLayoutManager(mContext,columnCount));
        pic_selector_recyclerview.setAdapter(pictureSelectorAdapter);
    }

    @Override
    protected void initListener() {
        pictureSelectorAdapter.setPictureSelectorListener(new PictureSelectorAdapter.PictureSelectorListener() {
            @Override
            public void onPicSelectListener() {
                //点击去相册选择图片
                PictureSelectorUtils.showAlbum(PictureSelectorActivity.this,selectList,maxNum);
            }

            @Override
            public void onPicClickListener(int position) {
                //点击选择的图片，去放大查看
                PictureSelector.create(PictureSelectorActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
            }

            @Override
            public void onPicDeleteListener(int position) {
                //删除已经选中的图片
                if (position != RecyclerView.NO_POSITION) { // 这里有时会返回-1造成数据下标越界
                    selectList.remove(position); //把数据从list中remove掉
                    pictureSelectorAdapter.notifyItemRemoved(position);//显示动画效果
                    pictureSelectorAdapter.notifyItemRangeChanged(position, selectList.size());//对于被删掉的位置及其后range大小范围内的view进行重新onBindViewHolder
                }
            }
        });
    }

    //相册选择图片后回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    pictureSelectorAdapter.setList(selectList);
                    pictureSelectorAdapter.notifyDataSetChanged();
                    LogUtils.d("回调了:" + selectList.size());
                    break;
                default:
                    break;
            }
        }
    }
}

