package wzp.kits.scan;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.function.zxing.DefaultScanActivity;
import wzp.libs.function.zxing.util.QrCodeUtils;
import wzp.libs.utils.PermissionUtils;
import wzp.libs.utils.ToastUtils;
import wzp.libs.utils.image.ImageConvertUtils;


public class RelatedCodeActivity extends BaseActivity {

    /** 扫描二维码/条码（使用库中默认布局） */
    @BindView(R.id.scan_default)
    TextView scan_default;
    /** 扫描二维码/条码（自定义布局） */
    @BindView(R.id.scan_style)
    TextView scan_style;
    /** 生成二维码图片 */
    @BindView(R.id.generate_code)
    TextView generate_code;
    /** 选择图片并解析 */
    @BindView(R.id.select_and_analys)
    TextView select_and_analys;
    /** 展示二维码图片解析出来的内容 */
    @BindView(R.id.select_show_tv)
    TextView select_show_tv;
    /** 展示选择的二维码图片 */
    @BindView(R.id.select_show_iv)
    ImageView select_show_iv;

    //扫码跳转-数据回传
    public static final int FORRESULT_SCAN = 111;
    //选择图片并解析-数据回传
    public static final int FORRESULT_ALBUM = 112;

    @Override
    protected int getLayout() {
        return R.layout.activity_related_code;
    }


    @Override
    protected void initListener() {
        //扫描二维码/条码（使用库中默认布局）
        scan_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, DefaultScanActivity.class),FORRESULT_SCAN);
            }
        });
        //扫描二维码/条码（自定义布局）
        scan_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, StyleScanActivity.class),FORRESULT_SCAN);
            }
        });
        //生成二维码图片
        generate_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,GenerateQrCodeActivity.class));
            }
        });
        //选择图片并解析
        select_and_analys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里不动态申请权限，后面的图片显示不出来
                //判断是否授予权限
                boolean isGranted = PermissionUtils.isGranted(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
                //如果没有授予权限 ，则去动态申请权限
                if(!isGranted){
                    PermissionUtils.permission(Manifest.permission.READ_EXTERNAL_STORAGE).callBack(new PermissionUtils.PermissionCallBack() {
                        @Override
                        public void onGranted(PermissionUtils permissionUtils) {
                        }

                        @Override
                        public void onDenied(PermissionUtils permissionUtils) {
                            ToastUtils.showToast(mContext,"拒绝授权了");
                            finish();
                        }
                    }).request(mContext);
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, FORRESULT_ALBUM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 判断是否请求成功
        if(resultCode==RESULT_OK){
            switch (requestCode) {
                case FORRESULT_SCAN: // 扫描二维码
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        int result_type = bundle.getInt(QrCodeUtils.RESULT_TYPE);
                        if (result_type == QrCodeUtils.RESULT_SUCCESS){  //成功
                            String result = bundle.getString(QrCodeUtils.RESULT_STRING);
                            ToastUtils.showToast(this, "解析结果:" + result);
                        }else if (result_type == QrCodeUtils.RESULT_FAILED){ //失败
                            ToastUtils.showToast(this, "解析二维码失败");
                        }
                    }
            	    break;
                case FORRESULT_ALBUM: //选择图片并解析
                    if (data != null) {
                        Uri uri = data.getData();
                        String mAlbumPicturePath = ImageConvertUtils.uriToPath(this, uri); //mAlbumPicturePath 返回选取的图片路径 /storage/emulated/0/DCIM/P70519-115930.jpg
                        QrCodeUtils.analyzeBitmap(mAlbumPicturePath, new QrCodeUtils.AnalyzeCallback() {
                            @Override
                            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                select_show_tv.setText("解析结果:" + result);
                                select_show_iv.setVisibility(View.VISIBLE);
                                select_show_iv.setImageBitmap(mBitmap);
                            }
                            @Override
                            public void onAnalyzeFailed() {
                                select_show_tv.setText("解析失败,不是二维码图片" );
                                select_show_iv.setVisibility(View.GONE);
                            }
                        });
                    }
                    break;
                default:
            	    break;
            }
        }
    }
}
