package wzp.kits.photoview;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wzp.kits.BaseActivity;
import wzp.kits.R;
import wzp.libs.widget.adapter.RvBaseAdapter;
import wzp.libs.widget.adapter.RvBaseViewHolder;


public class PhotoActivity extends BaseActivity {

    /** 展示图片的recyclerview */
    @BindView(R.id.photo_rv)
    RecyclerView photo_rv;

    @Override
    protected int getLayout() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initValues() {

        List<String> imageList = new ArrayList<>();
        imageList.add("http://p2.so.qhimg.com/t01dac8a835c87bd007.jpg");
        imageList.add("http://hbimg.b0.upaiyun.com/a56d0eef33cc70b5f43978559b72976267e3b2451d1a4-qOvjqS_fw658");
        imageList.add("http://pic1.win4000.com/pic/b/03/21691230681.jpg");
        imageList.add("https://goss.veer.com/creative/vcg/veer/1600water/veer-133331142.jpg");
        imageList.add("https://goss.veer.com/creative/vcg/veer/800water/veer-135261461.jpg");
        imageList.add("https://goss.veer.com/creative/vcg/veer/1600water/veer-137316320.jpg");
        imageList.add("https://goss.veer.com/creative/vcg/veer/1600water/veer-129802470.jpg");
        imageList.add("https://goss.veer.com/creative/vcg/veer/1600water/veer-157311470.jpg");
        imageList.add("https://goss.veer.com/creative/vcg/veer/1600water/veer-133557850.jpg");


        photo_rv.setLayoutManager(new GridLayoutManager(mContext,3));  //3列
        photo_rv.setAdapter(photoAdapter);
        photoAdapter.setData(imageList);
    }

    //展示图片的recyclerView的adapter
    private RvBaseAdapter<String> photoAdapter = new RvBaseAdapter<>(R.layout.item_rv_photo, new RvBaseAdapter.OnBindViewListener<String>() {
        @Override
        public void onBindView(RvBaseViewHolder holder, String image, final int position) {
            holder.setUrlImageList(mContext,R.id.photo_iv, image) //展示图片
                    .setOnItemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,PhotoViewActivity.class);
                            intent.putStringArrayListExtra("arraylist", (ArrayList<String>) photoAdapter.getData());
                            intent.putExtra("position",position);
                            startActivity(intent);
                        }
                    });
        }
    });
}

