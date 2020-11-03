package wzp.libs.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


/**
 * ViewPager展示Fragment
 */
public class FraViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList;


    public FraViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList){
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
    }


    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }
}
