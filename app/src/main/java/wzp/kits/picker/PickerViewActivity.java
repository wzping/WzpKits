package wzp.kits.picker;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;


public class PickerViewActivity extends BaseActivity {

    @BindView(R.id.picker_area)
    TextView picker_area;
    @BindView(R.id.picker_time)
    TextView picker_time;

    @Override
    protected int getLayout() {
        return R.layout.activity_pickerview;
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void initListener() {
        picker_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        picker_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

