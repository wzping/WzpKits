package wzp.kits.use.widget;


import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.widget.LineTextView;
import wzp.libs.widget.RoundProgressBar;


/**
 * Widget库的使用示例
 */
public class WidgetUseActivity extends BaseActivity {

    /** 单行显示TextView，可切换收起展开状态 */
    @BindView(R.id.line_text_view)
    LineTextView line_text_view;
    @BindView(R.id.iv_switch_line)
    ImageView iv_switch_line;

    @BindView(R.id.round_progress_bar)
    RoundProgressBar round_progress_bar;

    @Override
    protected int getLayout() {
        return R.layout.activity_widget_use;
    }

    @Override
    protected void initValues() {
        String name = "张三,李四,王五,赵六,钱七,二麻子,狗柱子,牛蛋,翠花，西湾子，狗剩，还有啥，哈哈,赖皮蛤蟆,哇哇兔，莉莉，也不知道够不够两行了";
        line_text_view.setText(name);

        round_progress_bar.setProgress(25);
    }

    @Override
    protected void initListener() {
        line_text_view.setNewLineEvent(new LineTextView.OnNewLineCallBack() {
            @Override
            public void onNewLine() {
                // 是否存在换行
                boolean isNewLine = line_text_view.isNewLine();
                // 如果存在,则进行显示
                if(isNewLine){
                    iv_switch_line.setVisibility(View.VISIBLE);
                    iv_switch_line.setEnabled(true);
                } else {
                    iv_switch_line.setVisibility(View.INVISIBLE);
                    iv_switch_line.setEnabled(false);
                }
            }
        });
        //切换展开与收起状态
        iv_switch_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断当前是否允许换行
                boolean isAllowNewLine = line_text_view.isAllowNewLine();
                // 允许换行,则设置为不允许(取反)
                line_text_view.setAllowNewLine(!isAllowNewLine);
            }
        });
    }
}
