package wzp.kits.scan;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.function.zxing.util.QrCodeUtils;
import wzp.libs.utils.ScreenUtils;
import wzp.libs.utils.image.ImageConvertUtils;


/**
 * 生成二维码页面
 */
public class GenerateQrCodeActivity extends BaseActivity {

    /** 输入内容的EditText */
    @BindView(R.id.input_text)
    EditText input_text;
    /** 生成二维码（不带logo）*/
    @BindView(R.id.btn_generate)
    Button btn_generate;
    /** 生成二维码（带logo）*/
    @BindView(R.id.btn_generate_logo)
    Button btn_generate_logo;
    /** 展示二维码图片 */
    @BindView(R.id.iv_qrcode)
    ImageView iv_qrcode;

    @Override
    protected int getLayout() {
        return R.layout.activity_generate_qrcode;
    }

    @Override
    protected void initListener() {
        //生成二维码（不带logo）
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrCodeStr = input_text.getText().toString().trim();
                if (TextUtils.isEmpty(qrCodeStr)){
                    input_text.setError("输入内容不能为空");
                    return;
                }
                Bitmap generateBitmap = QrCodeUtils.generateQrCode(qrCodeStr, ScreenUtils.dipConvertPx(mContext,120), null);
                iv_qrcode.setImageBitmap(generateBitmap);
            }
        });
        //生成二维码（带logo）
        btn_generate_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrCodeString = input_text.getText().toString().trim();
                if (TextUtils.isEmpty(qrCodeString)){
                    input_text.setError("输入内容不能为空");
                    return;
                }
                Bitmap generateBitmap = QrCodeUtils.generateQrCode(qrCodeString, ScreenUtils.dipConvertPx(mContext,120), ImageConvertUtils.resToBitmap(mContext,R.drawable.ic_launcher));
                iv_qrcode.setImageBitmap(generateBitmap);
            }
        });
    }
}
