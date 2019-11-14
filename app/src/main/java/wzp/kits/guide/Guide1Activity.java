package wzp.kits.guide;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ViewFlipper;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.utils.ImageUtils;
import wzp.libs.widget.adapter.ViewPagerAdapter;


public class Guide1Activity extends BaseActivity {
    /** ViewPager */
    @BindView(R.id.ag_viewpager)
    ViewPager ag_viewpager;
    /** 第二个页面的背景图  */
    private ViewFlipper viewFlipper2;
    /** 第三个页面的背景图 */
    private ViewFlipper viewFlipper3;


    @Override
    protected int getLayout() {
        return R.layout.activity_guide1;
    }


    @Override
    protected void initViews() {
        //设置activity全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initValues() {

        // 初始化四个小布局操作
        List<View> viewLists = new ArrayList<>();
        // 加载View
        viewLists.add(LayoutInflater.from(mContext).inflate(R.layout.item_guide_1, null));
        viewLists.add(LayoutInflater.from(mContext).inflate(R.layout.item_guide_2, null));
        viewLists.add(LayoutInflater.from(mContext).inflate(R.layout.item_guide_3, null));
        viewLists.add(LayoutInflater.from(mContext).inflate(R.layout.item_guide_4, null));

        viewFlipper2 =  viewLists.get(1).findViewById(R.id.viewFlipper2);//第二个页面
        int[] resPage2 = {R.drawable.guide_2_01, R.drawable.guide_2_02};
        for (int i = 0; i < resPage2.length; i++){
            viewFlipper2.addView(ImageUtils.getImageView(mContext,resPage2[i]));
        }

        viewFlipper3 =  viewLists.get(2).findViewById(R.id.viewFlipper3);//第3个页面
        int[] resPage3 = {R.drawable.guide_3_01, R.drawable.guide_3_02, R.drawable.guide_3_03};
        for (int i = 0; i < resPage3.length; i++){
            viewFlipper3.addView(ImageUtils.getImageView(mContext,resPage3[i]));
        }

        ag_viewpager.setAdapter(new ViewPagerAdapter(viewLists));
    }

    @Override
    protected void initListener(){
        ag_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 1: //第二个页面的时候
                        viewFlipper2.setFlipInterval(500);//设置View之间切换的时间间隔
                        viewFlipper2.setDisplayedChild(0);//每次进入该页面都是默认先展示第一张
                        viewFlipper2.setInAnimation(getAlphaAnim());//设置View进入屏幕时候使用的动画
                        viewFlipper2.startFlipping();//使用上面设置的时间间隔来开始切换所有的View，切换会循环进行
                        new Handler(){
                            @Override
                            public void handleMessage(Message msg){
                                viewFlipper2.stopFlipping();//停止View切换
                            }
                        }.sendEmptyMessageDelayed(0, 500);
                        break;
                    case 2: //第三个页面的时候
                        viewFlipper3.setFlipInterval(1000);
                        viewFlipper3.setDisplayedChild(0);
                        viewFlipper3.setInAnimation(getAlphaAnim());
                        viewFlipper3.startFlipping();
                        new Handler(){
                            @Override
                            public void handleMessage(Message msg){
                                viewFlipper3.stopFlipping();
                            }
                        }.sendEmptyMessageDelayed(0,3000);//展示3张图片所需时间
                        break;
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /** 渐变动画 */
    private AlphaAnimation alphaAnim = null;

    /**
     * 获取渐变动画对象
     * @return
     */
    private AlphaAnimation getAlphaAnim(){
        if(alphaAnim == null){
            alphaAnim = new AlphaAnimation(0.6f, 1.0f); //透明度从0.6 - 1.0(完全不透明)
            alphaAnim.setDuration(1000);
            //停留在最后一个动画效果的位置，不回到原来的位置(下面两句一起的)
            alphaAnim.setFillAfter(true);
            alphaAnim.setFillBefore(false);
        }
        return alphaAnim;
    }


}
