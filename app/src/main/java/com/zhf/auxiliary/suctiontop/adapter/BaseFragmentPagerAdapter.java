package com.zhf.auxiliary.suctiontop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> list, String[] title) {
        super(fm);
        this.list = list;
        tabTitles = title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
    }

    final int PAGE_COUNT = 3;
    private String tabTitles[];

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

