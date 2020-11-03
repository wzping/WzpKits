package wzp.libs.widget.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import wzp.libs.widget.able.OnItemClickListener;


/**
 * ViewPager展示View(例如 RecyclerView、PhotoView、ImageView、LayoutInflater加载的布局等)
 */
public class ViewPagerAdapter extends PagerAdapter {

	/** 显示的数据源 */
	private List<View> views = null;
	/** ViewPager点击监听 */
	private OnItemClickListener onItemClickListener;

	/**
	 * 构造函数
	 * @param views
	 */
	public ViewPagerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	public ViewPagerAdapter(List<View> views,OnItemClickListener onItemClickListener) {
		super();
		this.views = views;
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View v = views.get(position);
		container.addView(v);

		if (onItemClickListener!=null){
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClickListener.onItemClick(position);
				}
			});
		}
		return v;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
