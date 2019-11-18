package wzp.kits.guide;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.utils.ImageViewUtils;
import wzp.libs.widget.adapter.ViewPagerAdapter;


public class Guide2Activity extends BaseActivity {

    /** ViewPager */
    @BindView(R.id.guide_viewpager)
    ViewPager guide_viewpager;
    /** 添加静态点的容器 */
    @BindView(R.id.ag_point_linear)
    LinearLayout ag_point_linear;
    /** 动态移动的点 */
    @BindView(R.id.ag_point_move)
    ImageView ag_point_move;
    /** 立即体验 */
    @BindView(R.id.guide_experience)
    TextView guide_experience;
    // 两个点的距离
    private int mPointSpace;
    //要展示的图片的张数
    private int size;

    @Override
    protected int getLayout() {
        return R.layout.activity_guide2;
    }


    @Override
    protected void initViews() {
        //设置activity全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void initValues() {
        int[] localImages = new int[]{R.drawable.guide_1, R.drawable.guide_2_01, R.drawable.guide_3_01,R.drawable.guide_4};
        size = localImages.length;
        List<View> viewLists = new ArrayList<>();
        for(int i=0; i<size; i++){
            viewLists.add(ImageViewUtils.getImageView(mContext,localImages[i]));
        }
        guide_viewpager.setAdapter(new ViewPagerAdapter(viewLists));

        //添加静态的点
        operatePoint(size);
    }


    /**
     * 添加静态的点
     * @param size  页面个数（有几个页面添加几个点）
     */
    private void operatePoint(int size){
        for(int i = 0; i < size; i++){
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_white);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) { // 不是第一个点的话，就设置
                params.leftMargin = 15; // 这里的数字都是指像素
            }
            ag_point_linear.addView(point, params);
        }

        //ag_point_linear添加完后再执行监听，否则ag_point_linear会报空指针
        ag_point_move.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //布局发生改变时的回调
            @Override
            public void onGlobalLayout() {
                //两个点间的距离
                mPointSpace = ag_point_linear.getChildAt(1).getLeft()- ag_point_linear.getChildAt(0).getLeft();
                //只监听一次变化
                ag_point_move.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


    @Override
    protected void initListener(){
        guide_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                guide_experience.setVisibility(position==(size-1)?View.VISIBLE:View.GONE);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 通过动态 去设置点的marginLeft ，达到动态点滑动的效果
                // marginLeft = 两个点间的距离 * 滑动的比值positionOffset
                int marginLeft = (int) (mPointSpace * positionOffset + position * mPointSpace + 0.5f); // 做一个四舍五入   变成int类型
                // 动态去设置点的marginLeft
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ag_point_move.getLayoutParams();
                params.leftMargin = marginLeft;
                ag_point_move.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
