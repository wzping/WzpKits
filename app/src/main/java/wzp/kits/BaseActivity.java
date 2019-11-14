package wzp.kits;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import butterknife.ButterKnife;


public abstract class BaseActivity extends Activity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        this.mContext = this;
        // 通过注解绑定控件
        ButterKnife.bind(this);
        initViews();
        initValues();
        initListener();
    }

    // = 对外要求实现方法 =
    protected abstract int getLayout();

    protected void initViews(){}

    protected void initValues(){}

    protected void initListener(){}
}
