package wzp.libs.utils.image;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * 图片相关转换类
 */
public final class ImageConvertUtils {
    // 日志Tag
    private static final String TAG = ImageConvertUtils.class.getSimpleName();

    private ImageConvertUtils() {
    }

    //-------------------------------转为bitmap

    /**
     * 将res转化为Bitmap
     * @param res 本地资源图片
     * @return Bitmap
     */
    public static Bitmap resToBitmap(Context mContext, int res) {
        return  BitmapFactory.decodeResource(mContext.getResources(),res);
    }


    /**
     * Drawable 转换成 Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }


    /**
     * 输入流转化为bitmap
     * @param is 输入流
     * @return bitmap
     */
    public static Bitmap streamToBitmap(InputStream is) {
        return (is == null) ? null : BitmapFactory.decodeStream(is);
    }

    /**
     * 路径转换为Bitmap
     * @param fPath 图片地址
     * @return
     */
    public static Bitmap pathToBitmap(String fPath) {
        try {
            return BitmapFactory.decodeFile(fPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 路径转换为Stream，Stream再转为Bitmap
     * @param fPath
     * @return
     */
    public static Bitmap pathToStreamToBitmap(String fPath) {
        try {
            FileInputStream fis = new FileInputStream(new File(fPath));//文件输入流
            Bitmap bmp = BitmapFactory.decodeStream(fis);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte[]转换为bitmap
     * @param bytes
     * @return
     */
    public static Bitmap byteToBitmap(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            try {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 将File转换成Bitmap
     * @param file The file.
     * @return Bitmap
     */
    public static Bitmap fileToBitmap(File file) {
        return (file == null) ? null:BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /**
     * Uri转换为Bitmap
     * @param uri
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap uriToBitmap(Context mContext, Uri uri, int targetWidth, int targetHeight) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        android.content.ContentResolver resolver = mContext.getContentResolver();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new BufferedInputStream(resolver.openInputStream(uri), 16 * 1024), null, options);
        // 获取图片的宽度、高度；
        float imgWidth = options.outWidth;
        float imgHeight = options.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        options.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(new BufferedInputStream(resolver.openInputStream(uri), 16 * 1024), null, options);
    }

    //--------------------------------bitmap转

    /**
     * Bitmap—>byte[]
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        return bitmapToByte(bitmap, 100, Bitmap.CompressFormat.PNG);
    }

    /**
     * Bitmap—>byte[]
     * @param bitmap
     * @param format
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap, Bitmap.CompressFormat format) {
        return bitmapToByte(bitmap, 100, format);
    }


    /**
     * Bitmap—>byte[]
     * @param bitmap
     * @param quality
     * @param format
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bitmap, int quality, Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            bitmap.compress(format, quality, o);
            return o.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Bitmap—>Drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Context mContext,Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(mContext.getResources(), bitmap);
    }


    //----------------------------------转为Drawable

    /**
     * Resources—>Drawable
     * @param context
     * @param id
     * @return
     */
    public static Drawable resToDrawable(Context context, @DrawableRes int id) {
        if (context == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)  //21
            return context.getDrawable(id);
        else
            return context.getResources().getDrawable(id);
    }

    /**
     * byte[]—>Drawable
     * @param bytes
     * @return
     */
    public static Drawable byteToDrawable(Context mContext,byte[] bytes) {
        return bitmapToDrawable(mContext,byteToBitmap(bytes));
    }

    //----------------------------------Drawable转

    /**
     * Drawable 转换成 byte数组
     * @param drawable
     * @return
     */
    public static byte[] drawableToByte(Drawable drawable) {
        return drawable == null ? null : bitmapToByte(drawableToBitmap(drawable));
    }


    /**
     * Drawable—>byte[]
     * @param drawable
     * @param format
     * @return
     */
    public static byte[] drawableToByte(Drawable drawable, Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmapToByte(drawableToBitmap(drawable), format);
    }

    //----------------------------------Uri转

    /**
     * 根据Uri获取图片路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     * @return
     */
    @TargetApi(19)
    public static String uriToPath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //----------------------------------
}
