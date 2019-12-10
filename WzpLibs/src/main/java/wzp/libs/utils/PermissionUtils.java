package wzp.libs.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 权限请求工具类
 * hint: 使用方法
 *      PermissionUtils.permission("").callBack(null).request();
 */
public class PermissionUtils {

    // 判断是否请求过
    private boolean isRequest = false;
    /** 申请的权限 */
    private List<String> mPermissions = new ArrayList<>();
    /** 准备请求的权限 */
    private List<String> mPermissionsRequest = new ArrayList<>();
    /** 申请通过的权限 */
    private List<String> mPermissionsGranted = new ArrayList<>();
    /** 申请未通过的权限 */
    private List<String> mPermissionsDenied = new ArrayList<>();
    /** 申请未通过的权限 - 永久拒绝 */
    private List<String> mPermissionsDeniedForever = new ArrayList<>();
    /** 操作回调 */
    private PermissionCallBack mCallBack;

    /**
     * 构造函数
     * @param permissions
     */
    private PermissionUtils(final String... permissions){
        mPermissions.clear();
        // 防止数据为null
        if (permissions != null && permissions.length != 0){
            // 遍历全部需要申请的权限
            for (String permission : permissions){
                mPermissions.add(permission);
            }
        }
    }

    // ==

    /**
     * 判断是否授予了权限
     * @param permissions
     * @return
     */
    public static boolean isGranted(Context mContext,final String... permissions) {
        // 防止数据为null
        if (permissions != null && permissions.length != 0) {
            // 遍历全部需要申请的权限
            for (String permission : permissions) {
                if (!isGranted(mContext, permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否授予了权限
     * @param mContext
     * @param permission
     * @return
     */
    private static boolean isGranted(final Context mContext, final String permission) {
        // SDK 版本小于 23 则表示直接通过 || 检查是否通过权限
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    // == 使用方法 ==

    /**
     * 申请权限初始化
     * @param permissions
     * @return
     */
    public static PermissionUtils permission(final String... permissions){
        return new PermissionUtils(permissions);
    }

    /**
     * 设置回调方法
     * @param callBack
     */
    public PermissionUtils callBack(PermissionCallBack callBack){
        if (isRequest){
            return this;
        }
        this.mCallBack = callBack;
        return this;
    }

    /** 请求权限 */
    public void request(Context mContext){
        if (isRequest){
            return;
        }
        isRequest = true;
        // 如果 SDK 版本小于 23 则直接通过
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 表示全部权限都通过
            mPermissionsGranted.addAll(mPermissions);
            // 处理请求回调
            requestCallback();
        } else {
            for (String permission : mPermissions){
                // 判断是否通过请求
                if (isGranted(mContext, permission)){
                    mPermissionsGranted.add(permission);
                } else {
                    mPermissionsRequest.add(permission);
                }
            }
            // 判断是否存在等待请求的权限
            if (mPermissionsRequest.isEmpty()){
                // 处理请求回调
                requestCallback();
            } else {
                sInstance = this;
                // 跳转到权限申请页面
                PermissActivity.start(mContext);
            }
        }
    }

    // == 请求权限回调 ==

    public interface PermissionCallBack {
        /**
         * 授权通过权限
         * @param permissionUtils
         */
        void onGranted(PermissionUtils permissionUtils);

        /**
         * 授权未通过权限
         * @param permissionUtils
         */
        void onDenied(PermissionUtils permissionUtils);
    }

    // == 内部Activity ==

    // 内部持有对象
    private static PermissionUtils sInstance;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static class PermissActivity extends Activity {

        public static void start(final Context context) {
            Intent starter = new Intent(context, PermissActivity.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 请求权限
            int size = sInstance.mPermissionsRequest.size();
            requestPermissions(sInstance.mPermissionsRequest.toArray(new String[size]), 1);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            sInstance.onRequestPermissionsResult(this); // 处理回调
            finish(); // 关闭当前页面
        }
    }

    // == 内部处理方法 ==

    /** 内部请求回调, 统一处理方法 */
    private void requestCallback() {
        if (mCallBack != null){
            // 判断是否允许全部权限
            boolean isGrantedAll = (mPermissions.size() == mPermissionsGranted.size());
            // 允许则触发回调
            if (isGrantedAll){
                mCallBack.onGranted(this);
            } else {
                mCallBack.onDenied(this);
            }
        }
    }

    /**
     * 请求回调权限回调处理
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onRequestPermissionsResult(final Activity activity) {
        // 获取权限状态
        getPermissionsStatus(activity);
        // 判断请求结果
        requestCallback();
    }

    /**
     * 获取权限状态
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermissionsStatus(final Activity activity) {
        for (String permission : mPermissionsRequest) {
            // 判断是否通过请求
            if (isGranted(activity, permission)){
                mPermissionsGranted.add(permission);
            } else {
                // 未授权
                mPermissionsDenied.add(permission);
                // 拒绝权限
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    mPermissionsDeniedForever.add(permission);
                }
            }
        }
    }

    //==================================  分割线 - 悬浮窗权限使用方法 ======================================
    /**
     * 判断是否有权限
     * @param mContext
     * @return
     */
    public static boolean commonROMPermissionCheck(Context mContext) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, mContext);
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return result;
    }
}