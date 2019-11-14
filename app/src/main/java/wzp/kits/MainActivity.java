package wzp.kits;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import wzp.kits.cycle.CycleViewPagerActivity;
import wzp.kits.gif.LoadingGifActivity;
import wzp.kits.guide.Guide1Activity;
import wzp.kits.guide.Guide2Activity;

public class MainActivity extends BaseActivity {

    /** 引导页 */
    @BindView(R.id.am_tv_guide1)
    TextView am_tv_guide1;
    @BindView(R.id.am_tv_guide2)
    TextView am_tv_guide2;
    /** 加载Gif图 */
    @BindView(R.id.loading_gif)
    TextView loading_gif;
    /** 轮播图 */
    @BindView(R.id.am_tv_cycle)
    TextView am_tv_cycle;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initListener() {
        //引导页
        am_tv_guide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Guide1Activity.class));
            }
        });
        am_tv_guide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Guide2Activity.class));
            }
        });
        //加载gif
        loading_gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoadingGifActivity.class));
            }
        });
        //轮播图
        am_tv_cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CycleViewPagerActivity.class));
            }
        });
    }
}

