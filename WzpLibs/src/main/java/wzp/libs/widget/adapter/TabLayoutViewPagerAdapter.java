package wzp.libs.widget.adapter;



import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * TabLayout + ViewPager（TabLayout与ViewPager联动）
 * 此时ViewPager的适配器
 */
public class TabLayoutViewPagerAdapter extends FragmentPagerAdapter {

	/** 页面Fragment集 */
	private ArrayList<Fragment> fragList;
	/** tab */
	private String[] CONTENT;

	/**
	 * 初始化构造函数
	 * @param fm Fragment管理器
	 * @param fragList Fragment集
	 */
	public TabLayoutViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragList, String[] CONTENT) {
		super(fm);
		this.fragList = fragList;
		this.CONTENT = CONTENT;
	}

	@Override
	public Fragment getItem(int pos) {
		return fragList.get(pos);
	}

	@Override
	public int getCount() {
		return fragList.size();
	}


	//显示TabLayout的tab上的名字
	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position];
	}
}
