package wzp.kits.use.widget;


import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.utils.ScreenUtils;
import wzp.libs.utils.ToastUtils;
import wzp.libs.utils.image.ImageConvertUtils;
import wzp.libs.widget.CalendarView;
import wzp.libs.widget.CircleImageView;
import wzp.libs.widget.HexagonView;
import wzp.libs.widget.LineTextView;
import wzp.libs.widget.RoundProgressBar;
import wzp.libs.widget.dialog.AppLoadingDialog;
import wzp.libs.widget.dialog.MultiChooseOperateDialog;
import wzp.libs.widget.dialog.ShowOperateDialog;
import wzp.libs.widget.dialog.SwitchOperateDialog;
import wzp.libs.widget.time.CountDownTimerView;
import wzp.libs.widget.time.CountUpTimerView;


/**
 * Widget库的使用示例
 */
public class WidgetUseActivity extends BaseActivity {
    /** app加载圈 */
    @BindView(R.id.loading_dialog)
    TextView loading_dialog;
    private AppLoadingDialog appLoadingDialog;

    /** 第一种弹窗样式 */
    @BindView(R.id.style_dialog_tv1)
    TextView style_dialog_tv1;
    private ShowOperateDialog showOperateDialog;
    /** 第二种弹窗样式 */
    @BindView(R.id.style_dialog_tv2)
    TextView style_dialog_tv2;
    /** 第三种弹窗样式 */
    @BindView(R.id.style_dialog_tv3)
    TextView style_dialog_tv3;

    /** 单行显示TextView，可切换收起展开状态 */
    @BindView(R.id.line_text_view)
    LineTextView line_text_view;
    @BindView(R.id.iv_switch_line)
    ImageView iv_switch_line;

    /** 自定义圆形进度环*/
    @BindView(R.id.round_progress_bar)
    RoundProgressBar round_progress_bar;

    /** 自定义日历控件*/
    @BindView(R.id.calendar_left_arrow)
    ImageView calendar_left_arrow;
    @BindView(R.id.calendar_right_arrow)
    ImageView calendar_right_arrow;
    @BindView(R.id.calendar_center_tv)
    TextView calendar_center_tv;
    @BindView(R.id.calendar_view)
    CalendarView calendar_view;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //格式化日期

    /** 自定义倒计时控件 */
    @BindView(R.id.countdown_timer_view)
    CountDownTimerView countdown_timer_view;

    /** 自定义累计时控件 */
    @BindView(R.id.countup_timer_view)
    CountUpTimerView countup_timer_view;

    /** 圆形控件 */
    @BindView(R.id.circle_imageview)
    CircleImageView circle_imageview;

    @Override
    protected int getLayout() {
        return R.layout.activity_widget_use;
    }

    @Override
    protected void initValues() {
        appLoadingDialog = new AppLoadingDialog(mContext);
        showOperateDialog = new ShowOperateDialog(mContext,R.layout.dialog_show_operate_style); //布局中的元素个数和控件id不能更改

        String name = "张三,李四,王五,赵六,钱七,二麻子,狗柱子,牛蛋,翠花，西湾子，狗剩，还有啥，哈哈,赖皮蛤蟆,哇哇兔，莉莉，也不知道够不够两行了";
        line_text_view.setText(name);

        round_progress_bar.setProgress(25);

        calendar_center_tv.setText(calendar_view.getCurrentYear()+"年"+calendar_view.getCurrentMonth()+"月");
        calendar_view.setCalendarWidth(getResources().getDisplayMetrics().widthPixels - ScreenUtils.dipConvertPx(mContext,30));
        calendar_view.setCalendarHeight(getResources().getDisplayMetrics().heightPixels*3/10);
        calendar_view.setCircleTextColor(Color.WHITE);
        calendar_view.setCircleCellColor(getResources().getColor(R.color.colorPrimaryDark));
        calendar_view.setCircleCellStyle(Paint.Style.FILL);
        ArrayList<Integer> datas = new ArrayList<>();
        datas.add(2);
        datas.add(8);
        datas.add(18);
        datas.add(21);
        calendar_view.setData(datas); //设置需要圈出的日期

        countdown_timer_view.setLayout(R.layout.view_counttimer_style); //设置显示的布局文件，注意：布局中的id不能更改
        countdown_timer_view.setTime(20,10,50); //设置开始倒计时的时间节点
        countdown_timer_view.start();

        countup_timer_view.setLayout(R.layout.view_counttimer_style);//设置显示的布局文件，注意：布局中的id不能更改
        countup_timer_view.setTime(00,00,00);
        countup_timer_view.start();

    }


    @Override
    protected void initListener() {
        loading_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appLoadingDialog.showDialog();
            }
        });

        //展示第一种弹窗样式
        style_dialog_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOperateDialog.showDialog("温馨提示","如需保存现有答题进度，" +"\n" +  "请点击保存后再关闭答卷");
            }
        });
        showOperateDialog.setOnSureClickListener(new ShowOperateDialog.OnSureClickListener() {
            @Override
            public void onSureClick() {
                ToastUtils.showToast(mContext,"点击了确定");
            }
        });
        style_dialog_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiChooseOperateDialog.Builder(mContext)
                        .setOnChooseClickListener(new MultiChooseOperateDialog.OnChooseClickListener() {
                            @Override
                            public void onChooseClick(int choice) {
                                if (choice == MultiChooseOperateDialog.CHOICE_ONE){
                                    ToastUtils.showToast(mContext,"拍照");
                                }else if (choice == MultiChooseOperateDialog.CHOICE_TWO){
                                    ToastUtils.showToast(mContext,"从相册中选择");
                                }
                            }
                        })
                        .create(R.layout.dialog_multi_choose_operate_style).showDialog();  //R.layout.dialog_multi_choose_operate_style 可不填，用默认的
            }
        });
        style_dialog_tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SwitchOperateDialog.Builder(mContext)
                        .setOnOperateClickListener(new SwitchOperateDialog.OnOperateClickListener() {
                            @Override
                            public void onOperateClick(int operate) {
                                if (operate == SwitchOperateDialog.SWITCH_OPERATE_CANCLE){
                                    ToastUtils.showToast(mContext,"点击了取消");
                                }else if (operate == SwitchOperateDialog.SWITCH_OPERATE_SURE){
                                    ToastUtils.showToast(mContext,"点击了确定");
                                }
                            }
                        })
                        .create(R.layout.dialog_switch_operate_style).showDialog(); //R.layout.dialog_switch_operate_style  可不填 用默认的
            }
        });

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

        calendar_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] split = calendar_view.getLastYearMonthDay().split("-");
                calendar_center_tv.setText(split[0] + "年" + split[1] + "月");
            }
        });
        calendar_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] split = calendar_view.getNextYearMonthDay().split("-");
                calendar_center_tv.setText(split[0] + "年" + split[1] + "月");
            }
        });
        calendar_view.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                if(calendar_view.isSelectMore()){//多选
                    ToastUtils.showToast(mContext,simpleDateFormat.format(selectedStartDate)+"到"+simpleDateFormat.format(selectedEndDate));
                }else{ //单选，点击只选择一个
                    ToastUtils.showToast(mContext,simpleDateFormat.format(downDate));
                }
            }
        });

        circle_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle_imageview.setImageResource(R.drawable.cycle4);
            }
        });
    }
}
