package wzp.kits.scan;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.function.zxing.CaptureFragment;
import wzp.libs.function.zxing.util.QrCodeUtils;
import wzp.libs.utils.PermissionUtils;
import wzp.libs.utils.ToastUtils;


/**
 * 自定义二维码扫描页面
 */
public class StyleScanActivity extends BaseActivity {

    /** 闪光灯 */
    @BindView(R.id.iv_scan_light)
    ImageView iv_scan_light;

    //闪光灯默认是关闭的
    public static boolean isOpen = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_style_scan;
    }

    @Override
    protected void initValues() {
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

        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        QrCodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_style_scan);
        captureFragment.setAnalyzeCallback(analyzeCallback); //扫描结果回调处理
        getSupportFragmentManager().beginTransaction().replace(R.id.style_scan_frame, captureFragment).commit();



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //设置居中显示
//                int height = (ScreenUtils.getHeight(mContext)-ScreenUtils.dipConvertPx(mContext,95)-ScreenUtils.getWidth(mContext)/2)/2;
//                captureFragment.setInnerMarginTop(height); //要延迟设置，否则没效果
//            }
//        }, 100);
    }


    @Override
    protected void initListener() {
        //闪光灯
        iv_scan_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen){ //如果是关闭的，则去打开
                    iv_scan_light.setImageResource(R.drawable.ic_light_on);
                    QrCodeUtils.isOpenLight(true);
                    isOpen = true;
                }else{ //去关闭
                    iv_scan_light.setImageResource(R.drawable.ic_light_off);
                    QrCodeUtils.isOpenLight(false);
                    isOpen = false;
                }
            }
        });
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
