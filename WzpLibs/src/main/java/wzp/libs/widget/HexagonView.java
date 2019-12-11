package wzp.libs.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import wzp.libs.R;


/**
 * 自定义六边形控件
 */
@SuppressLint({"DrawAllocation", "AppCompatCustomView"})
public class HexagonView extends ImageView {

    private final String TAG = this.getClass().getSimpleName();
    boolean islasso = false;//是否点中多边形

    // 六边形属性
    private int vWidth, vHeight, vLenght;
    private float a, b, c;

    private Paint paint, paint1;// 画笔,文字
    private String text = "";// 文字
    private int textSize = -1;// 字体大小
    private int textColor;// 字体颜色
    private Drawable src;// 图片
    private int backColor;// 背景色

    // 监听
    // 判断点是否在多边形内
    HexagonLassoUtils lasso;// 核心判断类
    List<PointF> list;
    ShapeDrawable mDrawable;
    Animation scaleAnimation,endAnimation;
	private Canvas canvas;
	private Bitmap bitmap;


    public HexagonView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     */
    public HexagonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeA = context.obtainStyledAttributes(attrs, R.styleable.HexagonView);
        // 获取控件属性值
        text = typeA.getString(R.styleable.HexagonView_text);
        textSize = typeA.getDimensionPixelSize(R.styleable.HexagonView_textSize, -1);
        textColor = typeA.getColor(R.styleable.HexagonView_textColor, Color.BLUE);
        src = typeA.getDrawable(R.styleable.HexagonView_src);
        backColor = typeA.getColor(R.styleable.HexagonView_backColor, Color.WHITE);
        // 回收资源，一定要调用
        typeA.recycle();
        //-------
        //缩放动画
        float start = 1.0f;
        float end = 0.9f;
        scaleAnimation = new ScaleAnimation(start, end, start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(30);
        scaleAnimation.setFillAfter(true);
        endAnimation = new ScaleAnimation(end, start, end, start,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        endAnimation.setDuration(30);
        endAnimation.setFillAfter(true);
    }

    /**
     * 绘画
     */
    @Override
    public void onDraw(Canvas canvas) {
        this.canvas = canvas;
		super.onDraw(canvas);

        vWidth = getWidth()-30;
        vHeight = getHeight();
        // 中心
        vLenght = vWidth / 2;
        // 弧度
        double randian30 = 30 * Math.PI / 180;// Math.PI 圆周长和直径比值
        a = (float) (vLenght * Math.sin(randian30));
        b = (float) (vLenght * Math.cos(randian30));
        c = (vHeight - 2 * b) / 2;

        // 画笔初始化
        if (paint == null) {
            paint = new Paint();
            // 去锯齿
            paint.setAntiAlias(true);
            paint.setStyle(Style.FILL_AND_STROKE);
            paint.setStrokeCap(Cap.ROUND);
            paint.setColor(backColor);
            paint.setAlpha(100);
        } else {
            paint.setStyle(Style.FILL_AND_STROKE);
            paint.setStrokeCap(Cap.ROUND);
            paint.setColor(backColor);
            paint.setAlpha(100);
        }

        if (paint1 == null) {
            paint1 = new Paint();
            // 去锯齿
            paint1.setAntiAlias(true);
            // 重设画笔颜色
            paint1.setColor(textColor);
            // 根据视图大小自动控制字体大小
            if (textSize == -1) {
                if (src == null) {
                    textSize = vWidth >= vHeight ? vHeight - 120 : vWidth - 120;
                    if (textSize < 5) {
                        textSize = 20;
                    }
                } else {
                    textSize = 24;
                }
            }
            paint1.setTextSize(textSize);
            // 画笔水平居中
            paint1.setTextAlign(Align.CENTER);
        }
        // 画六边形
        Path path = new Path();
//        path.moveTo(vWidth, vHeight / 2);
//        path.lineTo(vWidth - a, vHeight - c);
//        path.lineTo(vWidth - a - vLenght, vHeight - c);
//        path.lineTo(0, vHeight / 2);
//        path.lineTo(a, c);
//        path.lineTo(vWidth - a, c);
//        path.close();

        path.moveTo(vWidth / 2+12, 15);
        path.lineTo(vWidth / 2 + b +14, vHeight / 4);
        path.lineTo(vWidth / 2 + b +14, vHeight * 3 / 4);
        path.lineTo(vWidth/2 +14,vHeight-15);
        path.lineTo(vWidth-(vWidth / 2 + b) +14,vHeight*3/4);
        path.lineTo(vWidth-(vWidth / 2 + b) +14,vHeight/4);
        path.close();
//        path.lineTo();

        paint.setStrokeWidth(0);
        paint.setColor(backColor);
//        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Join.ROUND);
        // 画图形
        canvas.drawPath(path, paint);

        // 画图片
        if (src != null) {
            // paint.setAlpha(255);
            // 从Drawable中获取Bitmap
            
            if (bitmap==null) {
            	BitmapDrawable bitmapD = (BitmapDrawable) src;
            	bitmap = bitmapD.getBitmap();
			}
           

            // 生成正六边形
            mDrawable = new ShapeDrawable(new PathShape(path, vWidth, vHeight));
            Shader aShader = new BitmapShader(zoomBitmap(bitmap, vWidth+29,vHeight),
            		Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

            mDrawable.getPaint().setShader(aShader); // 填充位图
            mDrawable.setBounds(0, 0, vWidth, vHeight); // 设置边界尺寸
            mDrawable.draw(canvas);

            // 绘制图片
            // canvas.drawBitmap(bitmap, matrix, paint);
        }

        if (text != null) {
            // 计算文字高度,处理垂直居中
            FontMetrics fontMetrics = paint1.getFontMetrics();
            // 计算文字高度
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            // 画字体
            if (src != null) {// 如果有图片文字在底部
                canvas.drawText(text, vWidth / 2, 2 * b + c - 3, paint1);
            } else {// 没有图片时文字居于视图中央
                float textBaseY = vHeight - (vHeight - fontHeight) / 2 - fontMetrics.bottom;
                canvas.drawText(text, vWidth / 2, textBaseY, paint1);
            }
        }
        //六边形顶点位置
        if (list == null) {
            list = new ArrayList<PointF>();
        } else {
            list.clear();
        }
        PointF pf = new PointF();
        pf.set(vWidth, vHeight / 2);
        list.add(pf);
        PointF pf1 = new PointF();
        pf1.set(vWidth - a, vHeight - c);
        list.add(pf1);
        PointF pf2 = new PointF();
        pf2.set(vWidth - a - vLenght, vHeight - c);
        list.add(pf2);
        PointF pf3 = new PointF();
        pf3.set(0, vHeight / 2);
        list.add(pf3);
        PointF pf4 = new PointF();
        pf4.set(a, c);
        list.add(pf4);
        PointF pf5 = new PointF();
        pf5.set(vWidth - a, c);
        list.add(pf5);

        lasso = HexagonLassoUtils.getInstance();
        lasso.setLassoList(list);

        if (pf != null) {
            pf = null;
            pf1 = null;
            pf2 = null;
            pf3 = null;
            pf4 = null;
            pf5 = null;
         }
    }

    /**
     * 按宽/高缩放图片到指定大小并进行裁剪得到中间部分图片
     *
     * @param bitmap 源bitmap
     * @param w      缩放后指定的宽度
     * @param h      缩放后指定的高度
     * @return 缩放后的中间部分图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidht, scaleHeight, x, y;
        Bitmap newbmp;
        Matrix matrix = new Matrix();
        if (width > height) {
            scaleWidht = ((float) h / height);
            scaleHeight = ((float) h / height);
            x = (width - w * height / h) / 2;// 获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        } else if (width < height) {
            scaleWidht = ((float) w / width);
            scaleHeight = ((float) w / width);
            x = 0;
            y = (height - h * width / w) / 2;// 获取bitmap源文件中y做表需要偏移的像数大小
        } else {
            scaleWidht = ((float) w / width);
            scaleHeight = ((float) w / width);
            x = 0;
            y = 0;
        }
        matrix.postScale(scaleWidht, scaleHeight);
        try {
            newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y,
                    (int) (width - x), (int) (height - y), matrix, true);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return super.dispatchTouchEvent(event);
	}


	//========================= 对外公开方法 ==============================

    public void setImageSrc(Bitmap bitmap){
        this.bitmap = bitmap;
        postInvalidate();
    }
}
