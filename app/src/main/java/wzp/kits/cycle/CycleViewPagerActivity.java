package wzp.kits.cycle;



import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.function.cycle.CycleViewPager;
import wzp.libs.utils.ToastUtils;
import wzp.libs.widget.able.OnItemClickListener;

/**
 * 轮播图
 */
public class CycleViewPagerActivity extends BaseActivity {

    /** 轮播控件 */
    private CycleViewPager cycle_viewpager;

    @Override
    protected int getLayout() {
        return R.layout.activity_cycle_viewpager;
    }

    @Override
    protected void initViews() {
        cycle_viewpager = (CycleViewPager)getSupportFragmentManager().findFragmentById(R.id.cycle_viewpager);
    }

    @Override
    protected void initValues() {
        //要展示的本地轮播图片
        //int[] resPage = {R.drawable.cycle1,R.drawable.cycle2,R.drawable.cycle3,R.drawable.cycle4}; //本地资源图片
//        cycle_viewpager.startCycle(resPage);

        //要展示的网络轮播图
        String[] urlPage = {"http://xyl.txygc.com:80/medical-web-boss/upload/20190620/1561014183479默认标题_横版海报_2019.06.20 (1).jpg","http://xyl.txygc.com:80/medical-web-boss/upload/20190701/1561967224572timg (5)-恢复的.jpg",
        "http://pic.5tu.cn/uploads/allimg/081228/1516200.jpg","http://photocdn.sohu.com/20141212/mp573973_1418353896401_3.jpeg"};
        cycle_viewpager.startCycle(urlPage,onItemClickListener);
    }


    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            ToastUtils.showToast(mContext,"position -- " + position);
        }
    };
}
