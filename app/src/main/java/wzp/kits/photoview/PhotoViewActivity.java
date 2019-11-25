package wzp.kits.photoview;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.utils.GlideUtils;
import wzp.libs.widget.adapter.RvBaseAdapter;
import wzp.libs.widget.adapter.RvBaseViewHolder;
import wzp.libs.widget.adapter.ViewPagerAdapter;

public class PhotoViewActivity extends BaseActivity {

    @BindView(R.id.photoview_back)
    ImageView photoview_back;
    @BindView(R.id.photoview_index)
    TextView photoview_index;
    @BindView(R.id.photoview_viewpager)
    ViewPager photoview_viewpager;
    //加载的图片张数
    private int size;

    @Override
    protected int getLayout() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initValues() {
        //获取传递过来的数据
        Intent intent = getIntent();
        ArrayList<String> arrayList = intent.getStringArrayListExtra("arraylist");
        size = arrayList.size();
        int position = intent.getIntExtra("position", 0);

        photoview_index.setText((position+1) + "/" + size);

        List<View> imageLists = new ArrayList<>();
        imageLists.clear();
        for (int i=0;i<size;i++){
            imageLists.add(getImageView(arrayList.get(i)));
        }
        photoview_viewpager.setOffscreenPageLimit(size); //预加载，加载过后不会再重新加载了
        photoview_viewpager.setAdapter(new ViewPagerAdapter(imageLists));
        photoview_viewpager.setCurrentItem(position);
    }

    @Override
    protected void initListener() {
        photoview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        photoview_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float percent, int location) {
                // pos :当前页面，及你点击滑动的页面
                // percent:当前页面偏移的百分比
                // location:当前页面偏移的像素位置
            }

            @Override
            public void onPageSelected(int pos) {
                photoview_index.setText((pos+1) + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 0表示滑动完毕
                // 1表示按下状态
                // 2表示手指抬起状态
            }
        });
    }


    public PhotoView getImageView(String url){
        PhotoView photoView = new PhotoView(mContext);
        GlideUtils.getInstance().loadPicList(mContext,url,photoView);
        return photoView;
    }
}

