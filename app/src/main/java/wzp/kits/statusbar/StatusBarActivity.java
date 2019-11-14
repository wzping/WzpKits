package wzp.kits.statusbar;

import android.view.WindowManager;
import wzp.kits.BaseActivity;


public class StatusBarActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return -1;
    }

    @Override
    protected void initViews() {
        //设置activity全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
