package wzp.libs.function.zxing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import wzp.libs.R;
import wzp.libs.function.zxing.util.QrCodeUtils;
import wzp.libs.utils.PermissionUtils;
import wzp.libs.utils.ToastUtils;


/**
 * 默认的二维码扫描Activity(使用默认的布局)
 */
public class DefaultScanActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_default_scan);  //一个帧布局
        mContext = DefaultScanActivity.this;

        //判断是否授予权限
        boolean grantedCamera = PermissionUtils.isGranted(mContext, Manifest.permission.CAMERA);
        //如果没有授予权限 ，则去动态申请权限
        if(!grantedCamera){
            PermissionUtils.permission(Manifest.permission.CAMERA).callBack(new PermissionUtils.PermissionCallBack() {
                @Override
                public void onGranted(PermissionUtils permissionUtils) {
                    //授予了权限
                }
                @Override
                public void onDenied(PermissionUtils permissionUtils) {
                    ToastUtils.showToast(mContext,"拒绝授权无法使用该功能,请打开相机权限");
                    finish();
                }
            }).request(mContext);
        }

        CaptureFragment captureFragment = new CaptureFragment();  //默认就一个扫描的布局
        captureFragment.setAnalyzeCallback(analyzeCallback); //扫描结果回调处理
        getSupportFragmentManager().beginTransaction().replace(R.id.default_scan_frame, captureFragment).commit();

    }


    /**
     * 二维码解析回调函数
     */
    QrCodeUtils.AnalyzeCallback analyzeCallback = new QrCodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(QrCodeUtils.RESULT_TYPE, QrCodeUtils.RESULT_SUCCESS);
            bundle.putString(QrCodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(QrCodeUtils.RESULT_TYPE, QrCodeUtils.RESULT_FAILED);
            bundle.putString(QrCodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };
}