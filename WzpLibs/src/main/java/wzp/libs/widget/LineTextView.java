package wzp.libs.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * 单行显示TextView，可切换收起展开状态
 */
@SuppressLint("AppCompatCustomView")
public class LineTextView extends TextView {

	private String namespace = "wzp.libs";
	/** Tag */
	private final String TAG = "LineTextView";
	/** 上下文 */
	//private Context mContext;
	/** 字体大小 */
	private float textSize;
	/** 字体颜色 */
	private int textColor;
	/** 获取边距 */
	private float paddingLeft;
	private float paddingRight;
	/** View 宽度 */
	private float vWidth = 0;
	/** 屏幕宽度 */
	//private int sWidth = 0;
	// ========================
	/** 上一次绘制的行数 */
	private int drawLine = 0;
	/** 上次绘制的高度 */
	private int drawHeight = 0;
	/** 是否改变画笔 */
	private boolean isChangePaint = true;
	/** 换行标记 */
	private boolean isNewLineMark = false;
	/** 是否换行 */
	private boolean isNewLine = false;
	/** 是否允许换行 */
	private boolean isAllowNewLine = false;
	/** Text 画笔 */
	private Paint tvPaint = new Paint();

	public LineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.mContext = context;
		// 获取字体大小
		textSize = attrs.getAttributeIntValue(namespace, "textSize", 15);
		// 获取字体颜色
		textColor = attrs.getAttributeIntValue(namespace, "textColor", Color.BLACK);
		// 获取边距
		paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
		// 获取屏幕宽度
		//sWidth = ScreenUtils.getScreenWidth(context);
		// 消锯齿
		tvPaint.setAntiAlias(true);
		// 设置剧中
		tvPaint.setTextAlign(Paint.Align.LEFT);
		// 修改画笔
		isChangePaint = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//Log.d(TAG, "进入了 onMeasure  " + System.currentTimeMillis());
		// 获取模式
		//int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        // 获取高度
        //int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
		// ----
		// 获取模式
        // int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        // 获取高度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // ------------------------------------------------------------
        // 计算宽度
        vWidth = (widthSize - paddingLeft - paddingRight);
        // ------------------------------------------------------------
        // 设置高度
        //int maxHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) vHeight, widthMode);
        //super.onMeasure((int) vWidth, heightSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//Log.d(TAG, "进入了 onDraw     " + System.currentTimeMillis());
		// ----------------------------
		if(isChangePaint){
			textSize = getTextSize(); // 获取字体大小
			textColor = getTextColors().getDefaultColor(); // 获取字体颜色
			// 设置TextView 画笔
			tvPaint.setTextSize(textSize);
			tvPaint.setColor(textColor);
			isChangePaint = false;
		}
		// ----------------------------
		int lineCount = 0; // 当前行数
		String text = this.getText().toString();// .replaceAll("\n", "\r\n");
		if (text == null)
			return;
		
//		// 获取字体高度
//		FontMetricsInt fontMetrics = tvPaint.getFontMetricsInt();
//		int baseLine = 0;
//		// 防止行数 等于0
//		if(drawLine != 0){
//			// 获取每行高度 - 字体高度 /2 = 居中
//			baseLine = (drawHeight / drawLine - fontMetrics.bottom - fontMetrics.top) / 2;
//			if(baseLine <= 0){
//				baseLine = 0;
//			}
//		}
		isNewLineMark = false;
		char[] textCharArray = text.toCharArray(); // 拆分字体
		float drawedWidth = 0; // 已绘的宽度
		float charWidth; // 字体宽度
		float pointWidth = tvPaint.measureText(".", 0, 1); // 省略号的宽度
		// 遍历数据,计算宽度
		for (int i = 0, c = textCharArray.length; i < c; i++) {
			// 计算字体宽度
			charWidth = tvPaint.measureText(textCharArray, i, 1);
			// 判断内容是否存在换行
			if (textCharArray[i] == '\n') {
				lineCount++;
				drawedWidth = 0;
				continue;
			}
			// 如果超出空间宽度,则进行计算
			if (vWidth - drawedWidth < charWidth) {
				if(isAllowNewLine){ // 如果允许换行
					lineCount++;
					drawedWidth = 0;
				} else { // 允许换行
					lineCount = 1;
					break;
				}
				isNewLineMark = true;
			}
			// 如果不能换行,则判断是否快到底部
			if(!isAllowNewLine){
				boolean isEnd = false;
				int n = i + 1;
				// 判断字体长度
				if(i + 2 >= c){ // i + 1 > c - 1 => 索引从0开始
					//n = i - 1;
					n = c - 1;
					isEnd = true;
				}
				// 下一个字体的宽度
				float nTextWidth = tvPaint.measureText(textCharArray, n, 1);
				// 剩余可绘制的宽度
				float surplus = vWidth - drawedWidth;
				// 减去宽度
				float mWidth = surplus - charWidth - nTextWidth;
				// 如果下个字体的宽度 + 准备绘制的高度 大于 省略号的宽度 pointWidth，则不绘制下一个字体,直接进行绘制省略号
				if(mWidth < pointWidth * 3){
					// 判断base是否等于0
					//boolean isCheckLine = (baseLine == 0);
					// y轴位置
					//float y = isCheckLine ? ((lineCount + 1) * textSize) : baseLine;
					float y = (lineCount + 1) * textSize;
					// 绘制字体
					canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth, y, tvPaint);
					if(!isEnd){ // 不属于最后一位
						// 临时宽度(边距 + 已经累积宽度 + 上一个字体的宽度)
						float tempWidth = paddingLeft + drawedWidth + charWidth;
						// 绘制省略号
						canvas.drawText("...", 0, 3, tempWidth, (lineCount + 1) * textSize, tvPaint);
						// 表示换行了
						lineCount = 1;
						isNewLineMark = true;
					} else {
						if(n != i){ // 防止多次绘制最结尾
							// 绘制字体
							canvas.drawText(textCharArray, n, 1, paddingLeft + drawedWidth + charWidth, y, tvPaint);
						}
						// 表示换行了
						lineCount = 0;
						isNewLineMark = true;
					}
					break;
				} else {
					if(!isEnd){
						isNewLineMark = true;
					} else {
						isNewLineMark = false;
					}
				}
			}
			// 判断base是否等于0 - 使用BaseLine 则是居中，不使用则是靠底部
			//boolean isCheckLine = (baseLine == 0);
			// y轴位置
			//float y = isCheckLine ? ((lineCount + 1) * textSize) : baseLine;
			float y = (lineCount + 1) * textSize;
			// 绘制字体
			canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth, y, tvPaint);
			// 累积高度
			drawedWidth += charWidth;
		}
		// 判断是否换行（不等于0,则表示大于1行= 多行）
		isNewLine = (lineCount != 0);
		isNewLine = isNewLineMark;
		// 换行状态
		//Log.i(TAG, "isNewLine =>" + isNewLine + ", lineCount =>" + lineCount);
		// 判断数据行数
		int dLine = (lineCount + 1);
		// 不允许换行,则默认为一行
		if(!isAllowNewLine){
			dLine = 1;
			lineCount = 0;
		}
		// 计算行高
		int dHeight = ((lineCount + 1) * (int) textSize + 5);
		// 是否运行绘制
		boolean isAllowDraw = false;
		if(this.drawLine != dLine || this.drawHeight != dHeight){
			this.drawLine = dLine;
			this.drawHeight = dHeight;
			isAllowDraw = true;
		}
		// 防止多次绘制，onDraw => onMeasure => onDraw 来回调用
		if(isAllowDraw){
			// 重新设置高度
			setHeight((lineCount + 1) * (int) textSize + 5);
		}
		if(isNewLine){
			if(newLineCallBack != null){
				newLineCallBack.onNewLine();
			}
		}
	}


	@Override
	public void setTextColor(ColorStateList colors) {
		isChangePaint = true;
		super.setTextColor(colors);
	}

	@Override
	public void setTextColor(int color) {
		isChangePaint = true;
		super.setTextColor(color);
	}

	@Override
	public void setTextSize(float size) {
		isChangePaint = true;
		super.setTextSize(size);
	}

	@Override
	public void setTextSize(int unit, float size) {
		isChangePaint = true;
		super.setTextSize(unit, size);
	}


	// ===================================================


	/** 换行事件 */
	private OnNewLineCallBack newLineCallBack;

	/**
	 * 设置换行事件
	 * @param newLineCallBack
	 */
	public void setNewLineEvent(OnNewLineCallBack newLineCallBack) {
		this.newLineCallBack = newLineCallBack;
	}


	/**
	 * 换行回调事件
	 */
	public interface OnNewLineCallBack {
		/**
		 * 换行触发
		 */
		public void onNewLine();
	}

	public boolean isNewLine() {
		return isNewLine;
	}

	public boolean isAllowNewLine() {
		return isAllowNewLine;
	}

	public void setAllowNewLine(boolean isAllowNewLine) {
		this.isAllowNewLine = isAllowNewLine;
		postInvalidate();
	}

}
