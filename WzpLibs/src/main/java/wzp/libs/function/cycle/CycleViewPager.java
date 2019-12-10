package wzp.libs.function.cycle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import wzp.libs.R;
import wzp.libs.utils.ImageViewUtils;
import wzp.libs.utils.ScreenUtils;
import wzp.libs.widget.able.OnItemClickListener;
import wzp.libs.widget.adapter.ViewPagerAdapter;


/**
 * 轮播图
 * 实现可循环，可轮播的viewpager
 */
public class CycleViewPager extends Fragment implements ViewPager.OnPageChangeListener {

	private Context mContext;
	/** ViewPager */
	private ViewPager fragment_cycle_viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	/** 装点的容器 */
	private LinearLayout linear_viewpager_indicator;
	//正确的索引
	private int currentPos;
	//展示的轮播图的真实的长度
	private int size;
	//viewpager是否处于手势滑动状态(默认不处于)
	private boolean isGestureScroll = false;
	//轮询常量
	private static final int WHEEL = 100;
	private static final int WHEEL_WAIT = 101;
	private long releaseTime = 0;
	//--------------参数设置
	//默认指示点
	private int indicator_normal;
	//选中指示点
	private int indicator_selected;
	//点击事件
	private OnItemClickListener onClickListener;
	//轮播切换时间(默认2s)
	private int cycleTime = 2000;
	//指示点的展示位置
	private int indicator_gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
	//指示点距离底部的距离
	private int incicator_margin_bottom = 8;
	//指示点距离左边的距离
	private int incicator_margin_left = 0;
	//指示点距离右边的距离
	private int incicator_margin_right = 0;
	//指示点之间的距离
	private int incicator_margin_padding = 5;
	//展示轮播图的ScyleType(本地图片和网络图片)
	private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
	//异常情况下加载显示的图片(只针对网络图片的情况)
	private int defaultPic = R.drawable.ic_error1;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cycle_viewpager, null);

		mContext = view.getContext();

		fragment_cycle_viewPager = (ViewPager) view.findViewById(R.id.fragment_cycle_viewPager);
		linear_viewpager_indicator = (LinearLayout) view.findViewById(R.id.linear_viewpager_indicator);

		return view;
	}


	/**
	 * 轮播网络地址图片
	 * @param urlImages
	 */
	public void startCycle(String[] urlImages,OnItemClickListener onClickListener){
		startCycle(urlImages,R.drawable.ic_indicator_normal,R.drawable.ic_indicator_selected,onClickListener);
	}

	public void startCycle(String[] urlImages,int indicatorNormal,int indicatorSelected,OnItemClickListener onClickListener){
		if (urlImages == null || urlImages.length == 0) {
			return;
		}

		this.indicator_normal = indicatorNormal;
		this.indicator_selected = indicatorSelected;
		this.onClickListener = onClickListener;

		size = urlImages.length;  //1,2,3,4,5,6...
		List<View> imageLists = new ArrayList<>();
		imageLists.clear();
		if (size==1){  //轮播图只有一张的情况下

			imageLists.add(ImageViewUtils.getImageView(mContext,urlImages[0],scaleType,defaultPic)); //添加这一张就好了

			viewPagerAdapter = new ViewPagerAdapter(imageLists,onItemClickListener);
			fragment_cycle_viewPager.setAdapter(viewPagerAdapter);

		}else{   //轮播图大于或等于2张的情况
			//额外添加最后一张和第一张图片的目的是:最后一张再向右滑动时显示第一张，第一张向左滑动时显示最后一张
			//先添加最后一张
			imageLists.add(ImageViewUtils.getImageView(mContext,urlImages[size-1],scaleType,defaultPic));
			//再轮流全部添加
			for (int i=0;i<urlImages.length;i++){
				imageLists.add(ImageViewUtils.getImageView(mContext,urlImages[i],scaleType,defaultPic));
			}
			//最后添加第一张
			imageLists.add(ImageViewUtils.getImageView(mContext,urlImages[0],scaleType,defaultPic));

			fragment_cycle_viewPager.setOffscreenPageLimit(size); //预加载，加载过后不会再重新加载了
			viewPagerAdapter = new ViewPagerAdapter(imageLists,onItemClickListener);
			fragment_cycle_viewPager.setAdapter(viewPagerAdapter);
			//显示真正的第一张图片
			fragment_cycle_viewPager.setCurrentItem(1);
			fragment_cycle_viewPager.setOnPageChangeListener(this);

			//添加静态的点
			operatePoint(size);
			//设置选中指示点显示
			setIndicator(0);

			//开始轮播
			handler.postDelayed(runnable, cycleTime);
		}
	}


	/**
	 * 轮播本地资源图片
	 * @param resImages
	 */
	public void startCycle(int[] resImages,OnItemClickListener onClickListener){
		startCycle(resImages,R.drawable.ic_indicator_normal,R.drawable.ic_indicator_selected,onClickListener);
	}


	public void startCycle(int[] resImages,int indicatorNormal,int indicatorSelected,OnItemClickListener onClickListener){
		if (resImages == null || resImages.length == 0) {
			return;
		}

		this.indicator_normal = indicatorNormal;
		this.indicator_selected = indicatorSelected;
		this.onClickListener = onClickListener;

		size = resImages.length;  //1,2,3,4,5,6...
		List<View> imageLists = new ArrayList<>();
		imageLists.clear();
		if (size==1){  //轮播图只有一张的情况下

			imageLists.add(ImageViewUtils.getImageView(mContext,resImages[0],scaleType)); //添加这一张就好了

			viewPagerAdapter = new ViewPagerAdapter(imageLists,onItemClickListener);
			fragment_cycle_viewPager.setAdapter(viewPagerAdapter);

		}else{   //轮播图大于或等于2张的情况
			//额外添加最后一张和第一张图片的目的是:最后一张再向右滑动时显示第一张，第一张向左滑动时显示最后一张
			//先添加最后一张
			imageLists.add(ImageViewUtils.getImageView(mContext,resImages[size-1],scaleType));
			//再轮流全部添加
			for (int i=0;i<resImages.length;i++){
				imageLists.add(ImageViewUtils.getImageView(mContext,resImages[i],scaleType));
			}
			//最后添加第一张
			imageLists.add(ImageViewUtils.getImageView(mContext,resImages[0],scaleType));

			fragment_cycle_viewPager.setOffscreenPageLimit(size); //预加载，加载过后不会再重新加载了
			viewPagerAdapter = new ViewPagerAdapter(imageLists,onItemClickListener);
			fragment_cycle_viewPager.setAdapter(viewPagerAdapter);
			//显示真正的第一张图片
			fragment_cycle_viewPager.setCurrentItem(1);
			fragment_cycle_viewPager.setOnPageChangeListener(this);

			//添加静态的点
			operatePoint(size);
			//设置选中指示点显示
			setIndicator(0);

			//开始轮播
			handler.postDelayed(runnable, cycleTime);
		}
	}

	private void operatePoint(int size){
		linear_viewpager_indicator.removeAllViews();
		FrameLayout.LayoutParams linear_param = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		linear_param.gravity = indicator_gravity;
		linear_param.bottomMargin = ScreenUtils.dipConvertPx(mContext,incicator_margin_bottom);
		linear_param.rightMargin = ScreenUtils.dipConvertPx(mContext,incicator_margin_right);
		linear_param.leftMargin = ScreenUtils.dipConvertPx(mContext,incicator_margin_left);
		linear_viewpager_indicator.setLayoutParams(linear_param);
		for (int i=0;i<size;i++){
			ImageView iv = new ImageView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i != 0) { // 不是第一个点的话，就设置
				params.leftMargin = ScreenUtils.dipConvertPx(mContext,incicator_margin_padding); // 这里的数字都是指像素
			}
			linear_viewpager_indicator.addView(iv,params);
		}
	}


	private void setIndicator(int pos){
		//先全部设置成默认指示点
		for (int i=0;i<linear_viewpager_indicator.getChildCount();i++){
			linear_viewpager_indicator.getChildAt(i).setBackgroundResource(indicator_normal);
		}
		//标出当前指示点
		if (pos<linear_viewpager_indicator.getChildCount())
			linear_viewpager_indicator.getChildAt(pos).setBackgroundResource(indicator_selected);
	}


	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == WHEEL) {
				if (!isGestureScroll) {
					//pos: 			0 1 2 3 4 5
					//currentPos:   3 0 1 2 3 0
					//position:		2 3 4 5 2 3 4 5 (默认就是展示第一张，然后2 3 4 ，5相当于第一张)

					int position = (currentPos+2) % (size+2);//currentPosition初始值为1 ,默认setCurrentItem(1);
					fragment_cycle_viewPager.setCurrentItem(position, true);
				}
				releaseTime = System.currentTimeMillis();
				handler.removeCallbacks(runnable);
				handler.postDelayed(runnable, cycleTime);
			}else if (msg.what == WHEEL_WAIT){
				handler.removeCallbacks(runnable);
				handler.postDelayed(runnable, cycleTime);
			}
		}
	};


	Runnable runnable =  new Runnable() {
		@Override
		public void run() {
			if (getActivity() != null && !getActivity().isFinishing()) {
				long now = System.currentTimeMillis();  //获取的是时间戳 1573633343593
				if (now - releaseTime > cycleTime - 500) {
					handler.sendEmptyMessage(WHEEL);
				}else{
					handler.sendEmptyMessage(WHEEL_WAIT);
				}
			}
		}
	};


	//--------------------- scroll_viewPager.setOnPageChangeListener(this);

	@Override
	public void onPageScrolled(int i, float v, int i1) { }


	//因为额外的加入了最后一张和第一张图片，所以索引要进行处理
	@Override
	public void onPageSelected(int pos) {
		//pos: 			0 1 2 3 4 5
		//currentPos:   3 0 1 2 3 0

		if (pos==0){
			currentPos = size-1;
		}else if (pos==size+1){
			currentPos = 0;
		}else{
			currentPos = pos-1;
		}

		setIndicator(currentPos);
	}


	//此方法是在状态改变的时候调用
	//SCROLL_STATE_DRAGGING（1）表示用户手指“按在屏幕上并且开始拖动”的状态
	//SCROLL_STATE_SETTLING（2）在“手指离开屏幕”的状态。
	//SCROLL_STATE_IDLE（0）静止状态(滑动动作做完的状态)
	//执行顺序：1、2、0   只在有手势滑动操作时候才会执行，单纯的默认轮循是不会回调该方法的
	@Override
	public void onPageScrollStateChanged(int i) {
		if (i==ViewPager.SCROLL_STATE_DRAGGING){  // viewPager在滚动
			isGestureScroll = true;
			return;
		}else if(i==ViewPager.SCROLL_STATE_IDLE){  //viewPager滚动结束
			releaseTime = System.currentTimeMillis();
			fragment_cycle_viewPager.setCurrentItem(currentPos+1, false);
		}
		isGestureScroll = false;
	}

	/**
	 * 点击回调
	 */
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {
			if (onClickListener!=null){
				onClickListener.onItemClick(currentPos);
			}
		}
	};


	//--------------------------------  对外提供方法


	/**
	 * 设置轮播切换时间
	 * @param cycleTime 单位：毫秒
	 */
	public void setCycleTime(int cycleTime){
		this.cycleTime = cycleTime;
	}


	/**
	 * 指示点的展示位置 (默认Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM)
	 * @param indicator_gravity
	 */
	public void setIndicatorGravity(int indicator_gravity){
		this.indicator_gravity = indicator_gravity;
	}


	/**
	 * 指示点距离下边的距离(默认8dp)
	 * @param incicator_margin_bottom
	 */
	public void setIncicatorMarginBottom(int incicator_margin_bottom){
		this.incicator_margin_bottom = incicator_margin_bottom;
	}

	/**
	 * 指示点距离左边的距离(默认0dp)
	 * @param incicator_margin_left
	 */
	public void setIncicatorMarginLeft(int incicator_margin_left){
		this.incicator_margin_left = incicator_margin_left;
	}

	/**
	 * 指示点距离右边的距离(默认0dp)
	 * @param incicator_margin_right
	 */
	public void setIncicatorMarginRight(int incicator_margin_right){
		this.incicator_margin_right = incicator_margin_right;
	}

	/**
	 * 指示点之间的距离(默认5dp)
	 * @param incicator_margin_padding
	 */
	public void setIncicatorMarginPadding(int incicator_margin_padding){
		this.incicator_margin_padding = incicator_margin_padding;
	}

	/**
	 * 展示轮播图的ScyleType (默认ImageView.ScaleType.FIT_XY)    针对本地图片和网络图片
	 * @param scaleType
	 */
	public void setScaleType(ImageView.ScaleType scaleType){
		this.scaleType = scaleType;
	}

	/**
	 * 异常情况下加载显示的图片 (默认R.drawable.ic_error1)   只针对网络图片的情况
	 * @param defaultPic
	 */
	public void setDefaultPic(int defaultPic){
		this.defaultPic = defaultPic;
	}
}