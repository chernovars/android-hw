package com.example.arseniy.hw4_recyclerview;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT;
    private String tabTitles[];

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabTitles = new String[] { context.getString(R.string.recents_tab), context.getString(R.string.favorites_tab) };
        PAGE_COUNT = tabTitles.length;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        if (position == 0) {
            return NewsListFragment.newInstance(NewsListFragment.generateMockData());
        }
        else {
            return NewsListFragment.newInstance();
        }
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
