package com.example.arseniy.hw4_recyclerview;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[];
    private NewsListFragment[] fragments;
    static final int FAVORITES_PAGE_POSITION = 1;

    TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabTitles = new String[] { context.getString(R.string.recents_tab), context.getString(R.string.favorites_tab) };
        fragments = new NewsListFragment[] {NewsListFragment.newInstance(Utils.generateMockData(NewsListFragment.MOCK_NEWS_COUNT),
                                                    Utils.generateMockDates(NewsListFragment.MOCK_NEWS_COUNT)),
                                            NewsListFragment.newInstance()};
        for (NewsListFragment f : fragments) {
            f.setRetainInstance(true);
        }
    }

    @Override public int getCount() {
        return tabTitles.length;
    }

    @Override public NewsListFragment getItem(int position) {
        return fragments[position];
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
