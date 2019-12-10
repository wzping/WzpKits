/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wzp.libs.function.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import java.util.Collection;
import java.util.HashSet;
import wzp.libs.R;
import wzp.libs.function.zxing.camera.CameraManager;


/**
 * 自定义组件实现,扫描功能
 */
public final class ViewfinderView extends View {

    //手机的屏幕密度
    private float density;
    //画笔
    private final Paint paint;
    //扫描界面的那个蒙层颜色
    private final int maskColor;
    private final int resultColor;
    //扫描的时候会出现的点 的 颜色
    private final int resultPointColor;
    //出现的点的集合
    private Collection<ResultPoint> possibleResultPoints;
    // 扫描线移动的y
    private int scanLineTop;
    //================================属性设置
    //扫描框距离手机的上边距
    private float innerMarginTop;
    // 扫描框边角颜色
    private int innercornercolor;
    // 扫描框边角长度
    private int innercornerlength;
    // 扫描框边角宽度
    private int innercornerwidth;
    // 扫描线
    private Bitmap scanLight;
    // 扫描线移动速度
    private int scan_speed;
    // 扫描过程中是否展示小圆点
    private boolean isCircle;
    //扫描边框下面的提示文字
    private String scanHintText;
    //扫描边框下面的提示文字颜色
    private int scanHintColor;
    //扫描边框下面的提示文字大小
    private int scanHintSize;
    //文字距离扫描边框下面的距离
    private int scanHintMarginTop;
    //扫描边框下面的提示文字是否加粗
    private boolean isScanHintbold;

    //刷新界面的时间
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    private Bitmap resultBitmap;
    private Collection<ResultPoint> lastPossibleResultPoints;



    public ViewfinderView(Context context) {
        this(context, null);
    }


    public ViewfinderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);

    }

    public ViewfinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.balck_trans60);  //扫描界面的那个蒙层颜色
        resultColor = resources.getColor(R.color.black_transb0);
        resultPointColor = resources.getColor(R.color.yellow_c0ffff00);  //扫描的时候会出现的点 的 颜色
        possibleResultPoints = new HashSet<>(5);

        scanLight = BitmapFactory.decodeResource(resources, R.drawable.ic_scan_line); //扫描线

        initInnerRect(context, attrs);
    }


    /**
     * 初始化内部框的大小
     * @param context
     * @param attrs
     */
    private void initInnerRect(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);

        // 扫描框距离顶部
        innerMarginTop = ta.getDimension(R.styleable.ViewfinderView_inner_margintop, -1);
        if (innerMarginTop != -1) {
            CameraManager.FRAME_MARGINTOP = (int) innerMarginTop;
        }

        // 扫描框的宽度
        CameraManager.FRAME_WIDTH = (int) ta.getDimension(R.styleable.ViewfinderView_inner_width, context.getResources().getDisplayMetrics().widthPixels / 2);
        // 扫描框的高度
        CameraManager.FRAME_HEIGHT = (int) ta.getDimension(R.styleable.ViewfinderView_inner_height, context.getResources().getDisplayMetrics().widthPixels / 2);
        // 扫描框边角颜色
        innercornercolor = ta.getColor(R.styleable.ViewfinderView_inner_corner_color, Color.parseColor("#45DDDD"));
        // 扫描框边角长度
        innercornerlength = (int) ta.getDimension(R.styleable.ViewfinderView_inner_corner_length, 65);
        // 扫描框边角宽度
        innercornerwidth = (int) ta.getDimension(R.styleable.ViewfinderView_inner_corner_width,15 );
        // 扫描线
        scanLight = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.ViewfinderView_inner_scan_bitmap, R.drawable.ic_scan_line));
        // 扫描速度
        scan_speed = ta.getInt(R.styleable.ViewfinderView_inner_scan_speed, 5);
        //扫描过程中是否显示圆点
        isCircle = ta.getBoolean(R.styleable.ViewfinderView_inner_scan_iscircle, true);
        //扫描框下面提示文字
        scanHintText = ta.getString(R.styleable.ViewfinderView_inner_scan_hint_text);
        //扫描框下面提示文字的颜色
        scanHintColor = ta.getColor(R.styleable.ViewfinderView_inner_scan_hint_color,Color.WHITE);
        //扫描框下面的提示文字的大小
        scanHintSize = ta.getDimensionPixelSize(R.styleable.ViewfinderView_inner_scan_hint_size,16);
        //扫描框下面的提示文字距离扫描框的边距（上边距）
        scanHintMarginTop = (int)ta.getDimension(R.styleable.ViewfinderView_inner_scan_hint_margin_top,30);
        //扫描框下面提示文字是否加粗
        isScanHintbold = ta.getBoolean(R.styleable.ViewfinderView_inner_scan_hint_bold,true);

        ta.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        //获取屏幕的宽和高
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //canvas.drawRect(left,top,right,bottom,paint);
        //left:该控件距离手机左边的距离  其它同理

        //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
        //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {
            //绘制扫描的那四个角上的东西
            drawFrameBounds(canvas, frame);
            //绘制扫描线
            drawScanLight(canvas, frame);
            //绘制扫描框下面的提示文字
            drawScanHint(canvas,frame);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                //注释这段，在扫描的过程中就不会展示小圆点了
                paint.setColor(resultPointColor);
                if (isCircle) {
                    for (ResultPoint point : currentPossible) {
                        canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                    }
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                //注释这段，在扫描的过程中就不会展示小圆点了
                paint.setColor(resultPointColor);
                if (isCircle) {
                    for (ResultPoint point : currentLast) {
                        canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                    }
                }
            }
            //只刷新扫描框的内容，其他地方不刷新
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }


    /**
     * 绘制取景框边框
     * @param canvas
     * @param frame
     */
    private void drawFrameBounds(Canvas canvas, Rect frame) {
        /*paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(frame, paint);*/

        paint.setColor(innercornercolor);
        paint.setStyle(Paint.Style.FILL);  // 填充内部

        int corWidth = innercornerwidth;
        int corLength = innercornerlength;

        // 左上角
        canvas.drawRect(frame.left, frame.top, frame.left + corWidth, frame.top + corLength, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + corLength, frame.top + corWidth, paint);
        // 右上角
        canvas.drawRect(frame.right - corWidth, frame.top, frame.right, frame.top + corLength, paint);
        canvas.drawRect(frame.right - corLength, frame.top, frame.right, frame.top + corWidth, paint);
        // 左下角
        canvas.drawRect(frame.left, frame.bottom - corLength, frame.left + corWidth, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - corWidth, frame.left + corLength, frame.bottom, paint);
        // 右下角
        canvas.drawRect(frame.right - corWidth, frame.bottom - corLength, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - corLength, frame.bottom - corWidth, frame.right, frame.bottom, paint);
    }


    /**
     * 绘制移动扫描线
     * @param canvas
     * @param frame
     */
    private void drawScanLight(Canvas canvas, Rect frame) {
        if (scanLineTop == 0) {
            scanLineTop = frame.top;
        }
        if (scanLineTop >= frame.bottom - 30) {
            scanLineTop = frame.top;
        } else {
            scanLineTop += scan_speed;
        }
        Rect scanRect = new Rect(frame.left, scanLineTop, frame.right, scanLineTop + 30);
        canvas.drawBitmap(scanLight, null, scanRect, paint);
    }


    private void drawScanHint(Canvas canvas, Rect frame){
        int width = canvas.getWidth();

        paint.setColor(scanHintColor);
        paint.setTextSize(scanHintSize*density);  //乘以像素
        if (isScanHintbold){
            paint.setTypeface(Typeface.create("System",Typeface.BOLD));
        }else{
            paint.setTypeface(Typeface.create("System",Typeface.NORMAL));
        }

        if (TextUtils.isEmpty(scanHintText))
            return;
        float textWidth = paint.measureText(scanHintText);
        canvas.drawText(scanHintText,(width - textWidth)/2,(float) (frame.bottom + (float)scanHintMarginTop *density),paint);
    }


    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

    //=================================== 对外提供方法

    //代码中设置上边距
    public void setInnerMarginTop(int marginTop){
         CameraManager.FRAME_MARGINTOP = marginTop;
    }
}
