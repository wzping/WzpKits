package wzp.libs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import wzp.libs.R;


/**
 * 自定义圆环进度
 */
public class RoundProgressBar extends View {
	//画笔
	private Paint paint;
	
	//环形默认背景颜色
	private int roundColor;
	//环形进度背景颜色
	private int roundProgressColor;
	//圆环默认宽度
	private float roundWidth;
	//圆环进度宽度
	private float roundProgressWidth;
	//进度最大值，默认100
	private int max;
	//进度样式，默认为环形样式
	private int style;
	//环形样式 默认
	private static final int STROKE = 0;
	//饼状样式
	private static final int FILL = 1;
	//进度文字显示颜色
	private int textColor;
	//进度文字显示大小
	private float textSize;
	//进度文字显示的标识，默认为true
	private boolean textIsDisplayable;
	//是否展示% ，默认展示true
	private boolean percentIsDisplayable;

	//进度值
	private int progress;

	
	public RoundProgressBar(Context context) {
		this(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();
		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
		//圆环默认显示颜色
		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		//圆环进度条颜色
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		//圆环默认宽度
		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		//圆环进度宽度
		roundProgressWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundProgressWidth, 5);
		//进度最大值
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		//进度展示样式（圆环形还是饼状形）
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		//字体颜色
		textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_tvColor, Color.GREEN);
		//字体大小
		textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_tvSize, 15);
		//进度文字是否显示
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
		//%是否展示
		percentIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_percentIsDisplayable, true);

		mTypedArray.recycle();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/** 画最外层的大圆环 */
		int centre = getWidth() / 2;  						//获取圆心的x坐标
		//绘制内圆
		int radius = (int) (centre - roundProgressWidth / 2);   	//圆环的半径     【 注意：设置的时候，圆环进度的宽度roundProgressWidth不能小于圆环的宽度roundWidth,否则显示会有问题】
		paint.setColor(roundColor); 						//设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); 				//设置空心
		paint.setStrokeWidth(roundWidth); 					//设置圆环的宽度
		paint.setAntiAlias(true);  							//消除锯齿
		//参数说明 - drawCircle (float cx, float cy, float radius, Paint paint)
		//cx：圆心的x坐标。 cy：圆心的y坐标。radius：圆的半径。paint：绘制时所使用的画笔。
		canvas.drawCircle(centre, centre, radius, paint);   //画出圆环
		
		//绘制进度值文字
		paint.setStrokeWidth(0); 
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent = progress * 100 / max;
		float textWidth;
		if (percentIsDisplayable){
			textWidth = paint.measureText(percent + "%");
		}else{
			textWidth = paint.measureText(percent + "");
		}

		if(textIsDisplayable && style == STROKE){
			if (percentIsDisplayable){
				canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize / 2, paint);
			}else{
				canvas.drawText(percent+"", centre - textWidth / 2, centre + textSize / 2, paint);
			}

		}
		

//		paint.setStrokeWidth(roundWidth);  //设置圆环的宽度
		paint.setStrokeWidth(roundProgressWidth);
		paint.setColor(roundProgressColor); //设置进度的颜色

		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); //用于定义的圆弧的形状和大小的界限

		switch (style) {
		case STROKE:
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270, 360 * progress / max, false, paint);   //根据进度画圆弧
			break;
		case FILL:
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, 270, 360 * progress / max, true, paint);
			break;

			// 0 从右边开始
			// 270 从上边开始
		}
		
	}


	//========================== 对外公开方法 =============================
	
	
	public synchronized int getMax() {
		return max;
	}


	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}


	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}


	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
	}


	/**
	 * 圆环默认显示颜色
	 * @return
	 */
	public int getRoundColor() {
		return roundColor;
	}


	public void setRoundColor(int roundColor) {
		this.roundColor = roundColor;
	}

	/**
	 * 圆环进度条颜色
	 * @return
	 */
	public int getRoundProgressColor() {
		return roundProgressColor;
	}


	public void setRoundProgressColor(int roundProgressColor) {
		this.roundProgressColor = roundProgressColor;
	}


	/**
	 * 进度文字显示颜色
	 * @return
	 */
	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}


	/**
	 * 进度文字显示大小
	 * @return
	 */
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}


	/**
	 * 圆环默认宽度
	 * @return
	 */
	public float getRoundWidth() {
		return roundWidth;
	}


	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}

}
