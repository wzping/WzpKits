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
        String[] urlPage = {"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1311805132,2077013194&fm=26&gp=0.jpg","https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2171722984,1420867509&fm=26&gp=0.jpg",
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
