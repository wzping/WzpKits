package wzp.kits.use.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.utils.LogUtils;
import wzp.libs.utils.PermissionUtils;


public class PermissionActivity extends BaseActivity{
    /** 普通步骤申请拍照权限示例 */
    @BindView(R.id.ap_btn_normal)
    Button ap_btn_normal;
    /** 拍照按钮 */
    @BindView(R.id.ap_btn_camera)
    Button ap_btn_camera;
    /** 定位权限 */
    @BindView(R.id.ap_btn_location)
    Button ap_btn_location;
    /** 悬浮窗权限 */
    @BindView(R.id.ap_btn_window)
    Button ap_btn_window;
    /** 拍照权限请求码 */
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 100;
    /** 悬浮窗权限请求码 */
    private static final int REQUEST_CODE_WINDOW_PERMISSION = 101;


    @Override
    protected int getLayout() {
        return R.layout.activity_permission;
    }

    @Override
    protected void initListener() {
        //拍照权限一般的写法
        ap_btn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否授予权限
                boolean grantedNormal = PermissionUtils.isGranted(mContext,Manifest.permission.CAMERA);
                LogUtils.d("相机权限 是否授权了:" + grantedNormal);
                if(!grantedNormal){
                    //没有授权的话去申请授权  ,
                    ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{ Manifest.permission.CAMERA }, REQUEST_CODE_CAMERA_PERMISSION);
                }else{
                    Toast.makeText(mContext,"已经授权了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //拍照权限工具类的写法
        ap_btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否授予权限
                boolean grantedCamera = PermissionUtils.isGranted(mContext,Manifest.permission.CAMERA);
                LogUtils.d("相机权限 是否授权了:" + grantedCamera);
                //如果没有授予权限 ，则去动态申请权限
                if(!grantedCamera){
                    PermissionUtils.permission(Manifest.permission.CAMERA).callBack(new PermissionUtils.PermissionCallBack() {
                        @Override
                        public void onGranted(PermissionUtils permissionUtils) {
                            Toast.makeText(mContext,"同意授权了",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDenied(PermissionUtils permissionUtils) {
                            Toast.makeText(mContext,"拒绝授权了",Toast.LENGTH_SHORT).show();
                        }
                    }).request(mContext);
                }else{
                    Toast.makeText(mContext,"已经授权了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ap_btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否授予权限
                boolean grantedLocation = PermissionUtils.isGranted(mContext,Manifest.permission.ACCESS_COARSE_LOCATION);
                LogUtils.d("定位权限 是否授权了:" + grantedLocation);
                //如果没有授予权限 ，则去动态申请权限
                if(!grantedLocation){
                    PermissionUtils.permission(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION).callBack(new PermissionUtils.PermissionCallBack() {
                        @Override
                        public void onGranted(PermissionUtils permissionUtils) {
                            Toast.makeText(mContext,"同意授权了",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDenied(PermissionUtils permissionUtils) {
                            //ACCESS_COARSE_LOCATION、ACCESS_FINE_LOCATION 2个权限中只要拒绝一个，就会回调失败 (其他多个的情况也是如此)
                            Toast.makeText(mContext,"拒绝授权了",Toast.LENGTH_SHORT).show();
                        }
                    }).request(mContext);
                }else{
                    Toast.makeText(mContext,"已经授权了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //特殊权限，不能使用PermissionUtils去申请了
        ap_btn_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //悬浮窗权限
                //Settings.canDrawOverlays(this)  检查是否有悬浮窗权限
                //判断是否授予权限
                boolean grantedWindow = PermissionUtils.commonROMPermissionCheck(mContext);
                LogUtils.d("悬浮窗权限 是否授权了:" + grantedWindow);
                if(!grantedWindow){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE_WINDOW_PERMISSION);
                }else{
                    Toast.makeText(mContext,"已经授权了",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





    /**
     * 请求权限结果回调
     * @param requestCode  请求码
     * @param permissions
     * @param grantResults
     *  ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{ Manifest.permission.CAMERA }, REQUEST_CODE_CAMERA_PERMISSION);
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.d("权限的个数:" + permissions.length + "|权限:" + permissions[0] + "|该权限是否同意授权：" + grantResults[0] + "(0代表同意授权)");
        switch (requestCode) {
        case REQUEST_CODE_CAMERA_PERMISSION:
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext,"同意授权了",Toast.LENGTH_SHORT).show();
             } else {
                Toast.makeText(mContext,"拒绝授权无法使用该功能",Toast.LENGTH_SHORT).show();
             }
             break;
        }
    }


    /**
     * 悬浮窗权限结果回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WINDOW_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(mContext,"同意授权了",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"拒绝授权无法使用该功能",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
