package wzp.libs.function.zxing.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import wzp.libs.function.zxing.CaptureFragment;
import wzp.libs.function.zxing.camera.BitmapLuminanceSource;
import wzp.libs.function.zxing.camera.CameraManager;
import wzp.libs.function.zxing.decoding.DecodeFormatManager;


/**
 * 二维码相关工具类
 */
public class QrCodeUtils {

    //----用到的一些常量

    //二维码扫描 - 解析类型 - 成功（RESULT_SUCCESS）/ 失败（RESULT_FAILED）
    public static final String RESULT_TYPE = "result_type";
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILED = 2;
    //二维码扫描 - 解析出来的二维码内容
    public static final String RESULT_STRING = "result_string";
    //CaptureFragment的布局文件，不设置的话，就加载默认的布局文件
    public static final String LAYOUT_ID = "layout_id";
    // 解析配置
    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);

    //------------------------------------------

    /**
     * 为CaptureFragment设置layout参数
     * @param captureFragment
     * @param layoutId 布局文件
     */
    public static void setFragmentArgs(CaptureFragment captureFragment, int layoutId) {
        if (captureFragment == null || layoutId == -1) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        captureFragment.setArguments(bundle);
    }


    //------------------------------------------

    /**
     * 去相册选择图片并解析图片二维码信息
     * @param path  图片的路径
     * @param analyzeCallback  解析结果回调
     */
    public static void analyzeBitmap(String path, AnalyzeCallback analyzeCallback) {
        if (TextUtils.isEmpty(path)) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeFailed();
            }
        }
        //首先判断图片的大小,若图片过大,则执行图片的裁剪操作,防止OOM
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap mBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 400);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        mBitmap = BitmapFactory.decodeFile(path, options);

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
         hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(mBitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult != null) {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(mBitmap, rawResult.getText());
            }
        } else {
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeFailed();
            }
        }
    }


    /**
     * 去相册选择图片并解析图片二维码信息
     * @param bitmap  要解析的二维码图片
     * @param analyzeCallback  解析结果回调
     */
    public static void analyzeBitmap(Bitmap bitmap, AnalyzeCallback analyzeCallback) {
        if (bitmap == null) {
            if (analyzeCallback != null) {
                // 解析失败
                analyzeCallback.onAnalyzeFailed();
            }
            return;
        }
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
            // 判断是否为null
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeSuccess(bitmap, result.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 判断是否为null
            if (analyzeCallback != null) {
                analyzeCallback.onAnalyzeFailed();
            }
        }
    }

    /**
     * 解析二维码结果回调
     */
    public interface AnalyzeCallback{
        public void onAnalyzeSuccess(Bitmap mBitmap, String result);
        public void onAnalyzeFailed();
    }


    //------------------------------------------

    /**
     * 生成二维码图片   该方法是耗时操作，请在子线程中调用。
     * @param content 二维码内容
     * @param size 二维码大小 单位为px
     * @param logo  二维码添加的logo，不添加logo的话传递null即可
     * @return 生成的二维码图片
     */
    public static Bitmap generateQrCode(String content,int size,Bitmap logo){
        return generateQrCode(content,size,size,Color.BLACK,Color.WHITE,0,logo);
    }


    /**
     * 生成二维码图片
     * @param content 二维码内容
     * @param w  二维码宽
     * @param h  二维码高
     * @param foregroundColor  前景色
     * @param backgroundColor  背景色
     * @param blankMargin  二维码空白边距
     * @param logo 二维码添加的logo，不添加logo的话传递null即可
     * @return 生成的二维码图片
     */
    public static Bitmap generateQrCode(String content,int w,int h,int foregroundColor,int backgroundColor,int blankMargin,Bitmap logo) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        try {
            Bitmap scaleLogo = getScaleLogo(logo,w,h);

            int offsetX = w / 2;
            int offsetY = h / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (w - scaleWidth) / 2;
                offsetY = (h - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, blankMargin);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if(x >= offsetX && x < offsetX + scaleWidth && y>= offsetY && y < offsetY + scaleHeight){
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        if(pixel == 0){
                            if(bitMatrix.get(x, y)){
                                pixel = foregroundColor;
                            }else{
                                pixel = backgroundColor;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    }else{
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = foregroundColor;
                        } else {
                            pixels[y * w + x] = backgroundColor;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Bitmap getScaleLogo(Bitmap logo,int w,int h){
        if(logo == null)return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 /logo.getHeight());
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(),   logo.getHeight(), matrix, true);
        return result;
    }

    //-------------------------------

    /**
     * 是否打开闪光灯
     * @param isEnable  true:打开  false：关闭
     */
    public static void isOpenLight(boolean isEnable) {
        if (isEnable) {
            Camera camera = CameraManager.get().getCamera();
            if (camera != null) {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameter);
            }
        } else {
            Camera camera = CameraManager.get().getCamera();
            if (camera != null) {
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameter);
            }
        }
    }
}
