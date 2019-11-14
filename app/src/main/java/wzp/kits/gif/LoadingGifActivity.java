package wzp.kits.gif;

import android.view.WindowManager;
import wzp.kits.BaseActivity;
import wzp.kits.R;


public class LoadingGifActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_loading_gif;
    }

    @Override
    protected void initViews() {
        //设置activity全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
