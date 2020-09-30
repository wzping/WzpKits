package wzp.kits.pictureselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.ArrayList;
import java.util.List;
import wzp.kits.R;
import wzp.libs.utils.screen.ScreenConvertUtils;
import wzp.libs.utils.screen.ScreenUtils;


public class PictureSelectorAdapter extends RecyclerView.Adapter<PictureSelectorAdapter.PictureSelectorViewHolder> {
    /** 当前上下文 */
    private Context mContext;
    private List<LocalMedia> selectList = new ArrayList<>();
    /** 最多显示的图片张数,>imageMax张就不显示添加按钮了 */
    private int imageMax;
    /** 显示默认的添加图片 */
    private static final int TYPE_SHOW_DEFAULT = 1;
    /** 显示相册中选择的图片 */
    private static final int TYPE_SHOW_SELECT = 2;
    /** 每行展示columnCount列图片*/
    private int columnCount;

    public PictureSelectorAdapter(Context mContext,int columnCount){
        this.mContext = mContext;
        this.columnCount = columnCount;
    }

    //-------------- extends RecyclerView.Adapter 就要复写的方法 (3个)-----------------------
    @Override
    public PictureSelectorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PictureSelectorViewHolder mainViewHolder = new PictureSelectorViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_picture_selector,parent,false));
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(final PictureSelectorViewHolder pictureSelectorViewHolder, int position) {

        ViewGroup.LayoutParams layoutParams = pictureSelectorViewHolder.pic_selector_photo.getLayoutParams();
        layoutParams.height = (ScreenUtils.getWidth(mContext)- ScreenConvertUtils.dipConvertPx(mContext,15*(columnCount+1))) / columnCount;
        pictureSelectorViewHolder.pic_selector_photo.setLayoutParams(layoutParams);

        if (getItemViewType(position) == TYPE_SHOW_DEFAULT) {
            pictureSelectorViewHolder.pic_selector_photo.setImageResource(R.drawable.ic_add_pic);
            pictureSelectorViewHolder.pic_selector_photo_delete.setVisibility(View.INVISIBLE); //删除的图片不可见
            if(mPictureSelectorListener!=null){
                pictureSelectorViewHolder.pic_selector_photo.setOnClickListener(view -> mPictureSelectorListener.onPicSelectListener());
            }
        }else{
            LocalMedia localMedia = selectList.get(position);
            String path = localMedia.getPath(); //相册图片路径(这里还有裁剪的图片路径和压缩的图片路径)

            //------------------------------------------------------------------------------------------

            /*int mimeType = localMedia.getMimeType();
            String path1 = "";
            if (localMedia.isCut() && !localMedia.isCompressed()) {
                // 裁剪过
                path1 = localMedia.getCutPath();
            }else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = localMedia.getCompressPath();
            }else {
                // 原图
                path = localMedia.getPath();
            }*/

            //------------------------------------------------------------------------------------------

            Glide.with(mContext).load(path).into(pictureSelectorViewHolder.pic_selector_photo);
            pictureSelectorViewHolder.pic_selector_photo_delete.setVisibility(View.VISIBLE); //删除的图片可见

            if (mPictureSelectorListener!=null){
                //点击删除按钮
                pictureSelectorViewHolder.pic_selector_photo_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = pictureSelectorViewHolder.getAdapterPosition();
                        mPictureSelectorListener.onPicDeleteListener(index);
                    }
                });
                //显示的图片的点击事件
                pictureSelectorViewHolder.itemView.setOnClickListener(v -> {
                    int adapterPosition = pictureSelectorViewHolder.getAdapterPosition();
                    mPictureSelectorListener.onPicClickListener(adapterPosition);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
       // return selectList.size()+1;  //+1  默认的那张添加图片
        //如果没有达到最大显示图片数量，则显示那张加的图片，否则加的图片不显示（直接不给添加了）
        if (selectList.size() < imageMax) {
            return selectList.size() + 1;
        } else {
            return selectList.size();
        }
    }

    //----------------------------------------------------------------------------------

    class PictureSelectorViewHolder extends RecyclerView.ViewHolder{
        //添加的图片展示
        ImageView pic_selector_photo;
        //图片上面的删除按钮
        ImageView pic_selector_photo_delete;
        public PictureSelectorViewHolder(View itemView) {
            super(itemView);
            pic_selector_photo =  itemView.findViewById(R.id.pic_selector_photo);
            pic_selector_photo_delete =  itemView.findViewById(R.id.pic_selector_photo_delete);
        }
    }

    //----------------------------------------------------------------------

    public void setList(List<LocalMedia> list) {
        this.selectList = list;
    }

    public void setImageMax(int imageMax) {
        this.imageMax = imageMax;
    }

    //----------------------------------------------------------------------

    //设置position位置是显示默认的添加图片还是选择相册中选择的图片
    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_SHOW_DEFAULT;
        } else {
            return TYPE_SHOW_SELECT;
        }
    }

    private boolean isShowAddItem(int position) {
        int size = selectList.size() == 0 ? 0 : selectList.size();
        return position == size;
    }

    //----------------------------------------------------------------------

    private PictureSelectorListener mPictureSelectorListener;

    public interface PictureSelectorListener{
        void onPicSelectListener();
        void onPicClickListener(int position);
        void onPicDeleteListener(int position);
    }

    public void setPictureSelectorListener(PictureSelectorListener mPictureSelectorListener){
        this.mPictureSelectorListener = mPictureSelectorListener;
    }
}
