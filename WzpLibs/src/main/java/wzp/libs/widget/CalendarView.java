package wzp.libs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import wzp.libs.R;
import wzp.libs.utils.ScreenUtils;


/**
 * 自定义日历View
 * 以下注释均以 2017年11月19（当前系统时间）为准的
 * invalidate
 */
public class CalendarView extends View implements View.OnTouchListener {
	private final static String TAG = "CalendarView";
	private Context mContext;
	private Surface surface;
	/**存储月份中具体的日期数值 */
	private int[] date = new int[42];
	/** Calendar类(操作日期) */
	private Calendar calendar;
	/**当前时间（切换后就不是当前系统时间了）*/
	private Date curDate;
	/** 系统时间 */
	private Date actualDate;
	/**当前显示的日历起始的索引*/
	private int curStartIndex;
	/**当前显示的日历结束的索引*/
	private int curEndIndex;
	/**日历显示的第一个日期*/
	private Date showFirstDate;
	/**日历显示的最后一个日期*/
	private Date showLastDate;
	/**按下的格子索引*/
	private int downIndex;
	/** 手指按下状态时的临时日期*/
	private Date downDate;
	/** 日历选择的开始日期*/
	private Date selectedStartDate;
	/** 日历选择的结束日期*/
	private Date selectedEndDate;
	/**为false表示只选择了开始日期，true表示结束日期也选择了*/
	public boolean completed = false;
	//------------------------代码中可以设置的属性-------------------
	/**给控件设置监听事件*/
	private OnItemClickListener onItemClickListener;
	/** 日历控件是否可以点击（是否有点击效果，默认是可以点击的）*/
	private boolean enableClick = true;
	/** 设置单选还是多选(默认单选) */
	private boolean isSelectMore = false;
	/** 日历控件的宽度（默认为屏幕宽度） */
	private int calendarWidth = getResources().getDisplayMetrics().widthPixels;
	/** 日历控件的高度(默认为屏幕高度的2/5) */
	private int calendarHeight = getResources().getDisplayMetrics().heightPixels*2/5;
	/** 背景圈半径 */
	public float cellRadius = 0.25f;
	/** 按下的日期的背景圈样式，空心圆（默认）还是实心圆 */
	private Paint.Style downCellStyle = Paint.Style.STROKE;
	/** 今日日期的背景圈样式 */
	private Paint.Style todayCellStyle = Paint.Style.STROKE;
	/** 需要圈出的日期的背景圈样式 */
	private Paint.Style circleCellStyle = Paint.Style.STROKE;
	/** 需要圈出的日期集合 */
	private ArrayList<Integer> datas = new ArrayList<>();
	//----------------------布局中可以设置的属性-------------------
    /** 周上下两根线的颜色,当不需要显示的时候可以设置成透明 */
    private int lineColor;
	/** 不是当月日的文字颜色(上月或下月的日子),当不需要显示的时候可以设置成透明 */
	private int otherDateColor;
	/** 本月有效日的颜色 */
	private int validTextColor;
	/** 今日日期的背景圈颜色,当不需要显示的时候可以设置成透明 */
	private int todayNumberCellColor;
	/** 点击选择日期时的背景圈颜色 */
	private int cellDownColor;
	/** 所有显示的日期的背景圈宽度 */
	private float numberCellWidth;
	/** 周的文字大小 */
	private float weekTextSize;
	/** 周的文字颜色 */
	private int weekTextColor;
	/** 整个日历日期的文字大小 */
	private float calendarTextSize;
	/** 今日日期是直接展示日期还是展示"今天" */
	private boolean showTodayNumber;
	//------------------ 代码和布局中均可以设置的属性----------------------
	/** 今日的文字颜色 */
	private int todayNumberColor;
	/** 需要圈出的日期集合的文字颜色 */
	/** 比如现在是2019年12月，需要在该月份日历中圈出2号、8号、18号、21号 ,那么2、8、18、21显示为该颜色*/
	private int circleTextColor;
	/** 需要圈出的日期集合的文字的背景圈颜色 */
	private int circleCellColor;


	//一般在直接New一个View的时候调用。
	public CalendarView(Context context) {
		super(context);
	}


	//一般在layout文件中使用的时候会调用，关于它的所有属性(包括自定义属性)都会包含在attrs中传递进来
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG,"CalendarView");
        mContext = context;

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
		//周上下两根线的颜色
		lineColor = mTypedArray.getColor(R.styleable.CalendarView_lineColor, Color.RED);
		//不是当月日的文字颜色
		otherDateColor = mTypedArray.getColor(R.styleable.CalendarView_otherDateColor, Color.GRAY);
		//本月有效日的颜色
		validTextColor = mTypedArray.getColor(R.styleable.CalendarView_validTextColor, Color.BLACK);
		//今日的文字颜色
		todayNumberColor = mTypedArray.getColor(R.styleable.CalendarView_todayNumberColor, Color.RED);
		//今日的文字背景圈颜色
		todayNumberCellColor = mTypedArray.getColor(R.styleable.CalendarView_todayNumberCellColor, Color.GREEN);
		//所有显示的日期的背景圈宽度
		numberCellWidth = mTypedArray.getDimension(R.styleable.CalendarView_numberCellWidth, 4);
		//点击选择日子时的背景圈颜色
		cellDownColor = mTypedArray.getColor(R.styleable.CalendarView_cellDownColor, Color.RED);
		//周的文字大小
		weekTextSize = mTypedArray.getDimension(R.styleable.CalendarView_weekTextSize, ScreenUtils.spConvertPx(mContext,16));
		//周的文字颜色
		weekTextColor = mTypedArray.getColor(R.styleable.CalendarView_weekTextColor, Color.RED);
		//整个日历日期的文字大小
		calendarTextSize = mTypedArray.getDimension(R.styleable.CalendarView_calendarTextSize, ScreenUtils.spConvertPx(mContext,14));
		//今日日期是直接展示日期还是展示"今天"
		showTodayNumber = mTypedArray.getBoolean(R.styleable.CalendarView_showTodayNumber,true);
		//需要圈出的日期集合的文字颜色
		circleTextColor = mTypedArray.getColor(R.styleable.CalendarView_circleTextColor,Color.WHITE);
		//需要圈出的日期集合的文字的背景圈颜色
		circleCellColor = mTypedArray.getColor(R.styleable.CalendarView_circleCellColor,Color.RED);
		init();
	}

	private void init() {
		surface = new Surface();
		Log.d(TAG,"surface");
		surface.density = getResources().getDisplayMetrics().density;

		//Sun Nov 19 21:07:41 GMT+08:00 2017     获取系统当前时间
		curDate = selectedStartDate = selectedEndDate = actualDate = new Date();
		calendar = Calendar.getInstance();
		//calendar.setTime(curDate);

		setOnTouchListener(this);

	}

	//widthMeasureSpec heightMeasureSpec,他们是和宽高相关的，
	// 但它们其实不是宽和高， 而是由宽、高和各自方向上对应的测量模式来合成的一个值
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG,"onMeasure");
		//屏幕宽度
		surface.width = calendarWidth ;
		//屏幕高度的2/5
		surface.height = calendarHeight;

		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width, View.MeasureSpec.EXACTLY);
		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height, View.MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Log.d(TAG,"onLayout:" + changed);
		if (changed) {
			surface.init();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG,"onDraw");
		//画周文字的上下2条线
		canvas.drawPath(surface.boxPath, surface.borderPaint);

		//画周文字（日，一，二，...）
		float weekTextY = surface.weekHeight * 2 / 3f;
		for (int i = 0; i < surface.weekText.length; i++) { //长度为7
			/*float weekTextX = i * surface.cellWidth
					+ (surface.cellWidth - surface.weekPaint.measureText(surface.weekText[i])) / 2f;*/
			float weekTextX = i * surface.cellWidth + surface.cellWidth/2 - surface.weekPaint.measureText(surface.weekText[i])/2;
			//drawText(String text, float x, float y, Paint paint)
			//text:要绘制的文字 x：绘制原点x坐标 y：绘制原点y坐标 paint:用来做画的画笔
			Log.d("weekTextX","weekTextX:" + weekTextX);
			canvas.drawText(surface.weekText[i], weekTextX, weekTextY, surface.weekPaint);
		}

		// 计算日期
		calculateDate();

		//要放在这里（成员的话会出问题）
		int todayIndex = -1;

		//设置为系统当月时间
		calendar.setTime(curDate);
		String curYearAndMonth = calendar.get(Calendar.YEAR) + ""  + calendar.get(Calendar.MONTH);//随着左右键的切换而实时改变的
		calendar.setTime(actualDate);
		String actualYearAndMonth = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH);//固定当年当月不变的
		//当切换月份后curYearAndMonth就相应的设置成了切换的月份
		if (curYearAndMonth.equals(actualYearAndMonth)) {
			//上面设置的是几号，这里获取的数字就是几
			int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
			Log.d("todayNumber","todayNumber:" + todayNumber);//19
			todayIndex = curStartIndex + todayNumber - 1;//3+19-1=32
			Log.d("todayIndex","todayIndex:" + todayIndex);//21
		}

		//一行7个，总共要6行
		for (int i = 0; i < 42; i++) {
			//默认黑色
			int color = validTextColor;
			//如果是上个月或者是下个月的日子，则设置成灰色
			if (isLastMonth(i)) {
				color = otherDateColor;
			} else if (isNextMonth(i)) {
				color = otherDateColor;
			}else{ //属于本月有效日期
				for (int j=0;j<datas.size();j++){
					if (date[i]==datas.get(j)){
						color = circleTextColor;
						drawCellBg(canvas, i , circleCellColor,circleCellStyle);
					}
				}
			}

			if (todayIndex != -1 && i == todayIndex) {
				color = todayNumberColor;
				//绘制背景图（一个空心圆圈）
				drawCellBg(canvas, todayIndex , todayNumberCellColor,todayCellStyle);
				if (showTodayNumber){
					drawCellText(canvas, i, date[i] + "",color);
				}else {
					drawCellText(canvas, i, "今天" + "",color);
				}
			}else{
				drawCellText(canvas, i, date[i] + "",color);
			}

		}

		// 按下状态，选择状态背景色
		drawDownOrSelectedBg(canvas);

		super.onDraw(canvas);
	}

	private void calculateDate() {
		//设置日历的时间为当前时间
		calendar.setTime(curDate);//Sun Nov 19 21:07:41 GMT+08:00 2017
		Log.d("curDate","curDate:" + calendar.getTime());
		//将日历设定为本月的1号
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Log.d("curDate","curDate:" + calendar.getTime());//Wed Nov 01 21:13:40 GMT+08:00 2017
		//本月的1号在日历上是第四个（角标从1开始）
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Log.d("dayInWeek", "dayInWeek:" + dayInWeek);//4
		int monthStart = dayInWeek;//4
		//如果本月的1号是第一个，则从第二行第一个开始
		//if (monthStart == 1) {
		//	monthStart = 8;
		//}
		//这里以日为开头（以日为开头-1）
		monthStart -= 1;  //3
		//其实索引为3
		curStartIndex = monthStart;
		//date[3]=1
		date[monthStart] = 1;
		//last month
		if (monthStart > 0) {
			//将日历设定为上个月的最后一号
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			Log.d("128","curDate:" + calendar.getTime());//Tue Oct 31 21:37:57 GMT+08:00 2017
			//上句中设置为哪号，这里获取的天数即为多少天
			int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
			Log.d("128","dayInmonth:" + dayInmonth);//31
			for (int i = monthStart - 1; i >= 0; i--) {
				date[i] = dayInmonth; //data[2]=31  data[1]=30  data[0]=29
				dayInmonth--;
			}
			//将日历设定为该月的具体某一天（这里data[0]为29号）
			calendar.set(Calendar.DAY_OF_MONTH, date[0]);
			Log.d("138","curDate:" + calendar.getTime());//Sun Oct 29 21:46:16 GMT+08:00 2017
		}
		//系统当月日历显示的第一个日期（10月29号）
		showFirstDate = calendar.getTime();//Sun Oct 29 21:46:16 GMT+08:00 2017
		// this month
		//设置日历的时间为当前时间 Sun Nov 19 21:51:26 GMT+08:00 2017
		calendar.setTime(curDate);
		//将日历设定为  当月日期加上一个月 即Tue Dec 19 21:51:26 GMT+08:00 2017
		calendar.add(Calendar.MONTH, 1);
		Log.d("149","curDate:" + calendar.getTime());//Tue Dec 19 21:51:26 GMT+08:00 2017
		//将日历设定为上个月的最后一号
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		Log.d("149","curDate:" + calendar.getTime());//Thu Nov 30 21:54:51 GMT+08:00 2017
		//上句中设置为哪号，这里获取的天数即为多少天
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		Log.d("149","monthDay:" + monthDay);//30
		for (int i = 1; i < monthDay; i++) {
			date[monthStart + i] = i + 1; //date[4]=2 date[5]=3 ... date[32]=30
		}
		//当前系统月份结束索引 3+30 = 33
		curEndIndex = monthStart + monthDay;
		// next month
		for (int i = monthStart + monthDay; i < 42; i++) {
			date[i] = i - (monthStart + monthDay) + 1; //date[33]=1 date[34]=2 ... date[41]=9
		}
		if (curEndIndex < 42) {
			// 当前日期加上1天
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Log.d("171","curDate:" + calendar.getTime());//Fri Dec 01 22:06:09 GMT+08:00 2017
		}
		//将日期设定为本月（Dec）的9号（date[41]）
		calendar.set(Calendar.DAY_OF_MONTH, date[41]);
		Log.d("171","curDate:" + calendar.getTime());
		//系统当月日历显示的最后一个日期(12月9号)
		showLastDate = calendar.getTime();//Sat Dec 09 22:13:05 GMT+08:00 2017
	}

	private void drawCellText(Canvas canvas, int i, String text, int color) {
		//设置画笔颜色
		surface.datePaint.setColor(color);
		int x = getXByIndex(i);//1 2 3 4 5 6 7
		int y = getYByIndex(i);//1 2 3 4 5 6
		//参考上面周的计算
		float cellX = ((x - 1) * surface.cellWidth)
				+ (surface.cellWidth - surface.datePaint.measureText(text)) / 2f;
		float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 2 / 3f;
		//drawText(String text, float x, float y, Paint paint)
		//text:要绘制的文字 x：绘制原点x坐标 y：绘制原点y坐标 paint:用来做画的画笔
		canvas.drawText(text, cellX, cellY, surface.datePaint);
	}

	private int getXByIndex(int i) {
		return i % 7 + 1; // 1 2 3 4 5 6 7
	}

	private int getYByIndex(int i) {
		return i / 7 + 1; // 1 2 3 4 5 6
	}

	/**
	 * 绘制日期的背景圈
	 * @param canvas
	 * @param index 当前日子在日历中的索引 21
	 * @param color 背景圆圈颜色
     */
	private void drawCellBg(Canvas canvas, int index, int color, Paint.Style style) {
		int x = getXByIndex(index); //1
		int y = getYByIndex(index); //4
		surface.cellBgPaint.setColor(color);
		surface.cellBgPaint.setStyle(style);

		float left = surface.cellWidth/2 + (x-1)*surface.cellWidth;
		float top = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight  / 2f;

		//圆心坐标 圆半径 画笔
		canvas.drawCircle(left, top, surface.cellWidth*cellRadius, surface.cellBgPaint); //canvas.drawCircle(float cx, float cy, float radius, Paint paint);
	}

	private void drawDownOrSelectedBg(Canvas canvas) {
		if (downDate != null) {
			drawCellBg(canvas, downIndex, cellDownColor,downCellStyle);
		}
	}

	//专门用于绘制的Surface
	private class Surface {
		/**整个日历控件的宽度*/
		public int width;
		/**整个日历控件的高度*/
		public int height;

		/**日历中的头部信息*/
		public String[] weekText = { "日","一", "二", "三", "四", "五", "六"};
		/**周的横线路径*/
		public Path boxPath;
		/**周的横线画笔*/
		public Paint borderPaint;
		/**周的横线画笔的宽度*/
		public float borderWidth;
		public float density;
		public float weekHeight;
		/**周的文字的画笔*/
		public Paint weekPaint;
		/**周的文字的单元格宽度*/
		public float cellWidth;


		/** 本月日期的画笔*/
		public Paint datePaint;
		/** 本月具体日期单元格的高度*/
		public float cellHeight;

		/** 日期背景圈的画笔 */
		public Paint cellBgPaint;


		public void init() {
			float temp = height / 7f;
			weekHeight = (float) ((temp + temp * 0.3f) * 0.9);

			boxPath = new Path();
			boxPath.rLineTo(width, 0);
			//移动起点 移动下一次操作的起点位置
			boxPath.moveTo(0, weekHeight);
			boxPath.rLineTo(width, 0);

			borderWidth = (float) (0.5 * density);
			borderWidth = borderWidth < 1 ? 1 : borderWidth;
			borderPaint = new Paint();
			borderPaint.setColor(lineColor);  //周上下两根线的颜色
			//让画出的图形是空心的
			borderPaint.setStyle(Paint.Style.STROKE);//Paint.Style.FILL实心
			//设置画出的线的粗细程度
			borderPaint.setStrokeWidth(borderWidth);
			Log.d("borderWidth","borderWidth:" + borderWidth);

			weekPaint = new Paint();
			//上面一周的文字颜色
			weekPaint.setColor(weekTextColor);
			//抗锯齿
			weekPaint.setAntiAlias(true);
			//周的字体大小
			weekPaint.setTextSize(weekTextSize);
			//设置字体样式
			weekPaint.setTypeface(Typeface.DEFAULT);

			cellWidth = width / 7f;
			cellHeight = (height - weekHeight) / 6f;

			//本月具体日期的画笔
			datePaint = new Paint();
			datePaint.setColor(validTextColor); //本月有效日的文字颜色
			datePaint.setAntiAlias(true);
			datePaint.setTextSize(calendarTextSize);
			datePaint.setTypeface(Typeface.DEFAULT);//DEFAULT_BOLD

			cellBgPaint = new Paint();
			cellBgPaint.setColor(todayNumberCellColor);
			cellBgPaint.setAntiAlias(true);
			cellBgPaint.setStrokeCap(Paint.Cap.ROUND);
			cellBgPaint.setStrokeWidth(numberCellWidth);

		}
	}

	private boolean isLastMonth(int i) {
		if (i < curStartIndex) { //3
			return true;
		}
		return false;
	}

	private boolean isNextMonth(int i) {
		if (i >= curEndIndex) {//curEndIndex 33
			return true;
		}
		return false;
	}


	//设置日历时间
	/*public void setCalendarData(Date date){
		calendar.setTime(date);
		invalidate();
	}*/

	//获取日历时间
	/*public void getCalendatData(){
		calendar.getTime();
	}*/


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if (enableClick){
					setSelectedDateByCoor(event.getX(), event.getY());
				}
				break;
			case MotionEvent.ACTION_UP:
				//获取到了点击的这一天的日期
				if (downDate != null) {
					//为true状态
					if(isSelectMore){
						//选择了开始日期
						if (!completed) {
							//如果按下的日期在当前日期之前
							/*if (downDate.before(selectedStartDate)) {
							} else {
							}*/
							selectedStartDate = downDate;
							completed = true;
						} else { //选择了结束日期
							selectedEndDate = downDate;
							completed = false;
							//响应监听事件
							if (onItemClickListener!=null)
								onItemClickListener.OnItemClick(selectedStartDate,selectedEndDate,downDate);
						}
					}else{ //false状态  默认为false
						selectedStartDate = selectedEndDate = downDate;
						//响应监听事件
						if (onItemClickListener!=null)
							onItemClickListener.OnItemClick(selectedStartDate,selectedEndDate,downDate);
					}
					invalidate();
				}
				break;
		}
		return true;
	}


    private void setSelectedDateByCoor(float x, float y) {
        // cell click down
        //说明是按在日历中的
        if (y > surface.weekHeight) {
            //在日历中的列数
            //Math.floor(0.60)  -- 0 求一个最接近它的整数，这个整数的值小于或等于这个浮点数。
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            //在日历中的行数（不包括周（头）的那一行）  Float.valueOf 精度问题
            int n = (int) (Math.floor((y - surface.weekHeight) / Float.valueOf(surface.cellHeight)) + 1);
            Log.d("setSelectedDateByCoor","m:" + m + ",n:" + n);
            //当前点击的日子在日历中的索引
            downIndex = (n - 1) * 7 + m - 1;

            calendar.setTime(curDate);
            Log.d("setSelectedDateByCoor","curDate:" + calendar.getTime());
            if (isLastMonth(downIndex)) {
                //当前日期 减去一个月
                calendar.add(Calendar.MONTH, -1);//例如现在是11月份，那么减去一个月就是10月份了
                Log.d("setSelectedDateByCoor","isLastMonth:" + calendar.getTime());
            } else if (isNextMonth(downIndex)) {
                //当前日期加上一个月
                calendar.add(Calendar.MONTH, 1);//例如现在是11月份，那么加上一个月就是12月份了
                Log.d("setSelectedDateByCoor","isNextMonth:" + calendar.getTime());
            }
            //将日子设定为该月的具体的某一天（date[downIndex]）
            calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);
            downDate = calendar.getTime();//获取点击的这一天的日期
            Log.d("setSelectedDateByCoor","downDate:" + downDate);
        }
        invalidate();
    }


	//===========================  对外公开方法 ================================================


	/**
	 * 获取当前年
	 * @return  比如今天是2019年，则返回2019
	 */
	public int getCurrentYear(){
		calendar.setTime(curDate);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取当前月
	 * @return 比如今天是12月，那么返回12
	 */
	public int getCurrentMonth(){
		calendar.setTime(curDate);
		return calendar.get(Calendar.MONTH)+1;
	}


	/**
	 * 返回当前日期
	 * @return 比如今天是24号，那么返回24
	 */
	public int getCurrentDay(){
		calendar.setTime(curDate);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取当前年、月、日
	 * @return 比如今天2019.12.24，那么返回2019-12-24
	 */
	public String getCurrentYearMonthDay() {
		calendar.setTime(curDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		return year + "-" + month + "-" + day;
	}


	/**
	 * 获取上个月所属的年、月
	 * @return 比如现在是2019.12,那么返回2019-11
	 *         现在是2019.1,那么返回2018-12
	 */
	public String getLastYearMonthDay(){
		calendar.setTime(curDate);
		//将日历设定为  当月日期减去一个月
		calendar.add(Calendar.MONTH, -1);
		curDate = calendar.getTime(); //格式：Thur Oct 19 22:55:58 GMT+08:00 2017
		invalidate();
		return getCurrentYearMonthDay();
	}


	/**
	 * 获取下个月所属的年、月
	 * @return 比如现在是2019.11,那么返回2019-12
	 *         现在是2019.12,那么返回2020-1
	 */
	public String getNextYearMonthDay(){
		calendar.setTime(curDate);
		//将日历设定为  当月日期加上一个月
		calendar.add(Calendar.MONTH, 1);
		curDate = calendar.getTime();
		invalidate();
		return getCurrentYearMonthDay();
	}


	/**
	 * 获取选中状态（单选还是多选）
	 * @return
	 */
	public boolean isSelectMore() {
		return isSelectMore;
	}

	/**
	 * 设置单选还是双选（点击日历后，是只给选中一个日期，还是给选中2个日期）
	 * @param isSelectMore
	 */
	public void setSelectMore(boolean isSelectMore) {
		this.isSelectMore = isSelectMore;
	}


	public interface OnItemClickListener {
		void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.onItemClickListener =  onItemClickListener;
	}

	/**
	 * 获取是否可以点击
	 * @return
	 */
	public boolean isEnableClick(){
		return enableClick;
	}

	/**
	 * 设置日历控件是否可以点击
	 * @param enableClick
	 */
	public void setEnableClick(boolean enableClick){
		this.enableClick = enableClick;
	}


	/**
	 * 设置日历控件的宽度
	 * @param width
	 */
	public void setCalendarWidth(int width){
		this.calendarWidth = width;
	}

	/**
	 * 设置日历控件的高度
	 * @param height
	 */
	public void setCalendarHeight(int height){
		this.calendarHeight = height;
	}

	/**
	 * 设置背景圈的半径
	 * @param radius
	 */
	public void setCellRadius(float radius){
		this.cellRadius = radius;
	}

	/**
	 * 按下的日期的背景圈是空心圆还是实心圆
	 * @param style  Paint.Style.STROKE:空心圆   Paint.Style.FILL：实心圆
	 */
	public void setDownCellStyle(Paint.Style style){
		this.downCellStyle = style;
	}

	/**
	 * 今日日期的背景圈是空心圆还是实心圆
	 * @param todayCellStyle
	 */
	public void setTodayCellStyle(Paint.Style todayCellStyle){
		this.todayCellStyle = todayCellStyle;
	}


	/**
	 * 需要圈出的日期的背景圈是空心圆还是实心圆
	 * @param circleCellStyle
	 */
	public void setCircleCellStyle(Paint.Style circleCellStyle){
		this.circleCellStyle = circleCellStyle;
	}

	/**
	 * 设置今日日期的文字颜色
	 * @param color
	 */
	public void setTodayNumberColor(int color){
		this.todayNumberColor = color;
	}


	/**
	 * 需要圈出的日期集合
	 * @param data  比如现在是2019年12月，需要在该月份日历中圈出2号、8号、18号、21号，那么传入[2,8,18,21]
	 */
	public void setData(ArrayList<Integer> data){
		datas.clear();
		this.datas.addAll(data);
	}

	/**
	 * 需要圈出的日期集合的文字颜色
	 * 比如现在是2019年12月，需要在该月份日历中圈出2号、8号、18号、21号 ,那么2、8、18、21显示为该颜色
	 * @param circleTextColor
	 */
	public void setCircleTextColor(int circleTextColor){
		this.circleTextColor = circleTextColor;
	}


	/**
	 * 需要圈出的日期集合的文字的背景圈颜色
	 * @param circleCellColor
	 */
	public void setCircleCellColor(int circleCellColor){
		this.circleCellColor = circleCellColor;
	}
}
