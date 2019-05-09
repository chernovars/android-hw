package com.example.arseniy.hw8_network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private static final int RECENTS_PAGE_POSITION = 0;
    private static final int FAVORITES_PAGE_POSITION = 1;
    private static final boolean MAIN_FLAG = true;

    TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabTitles = new String[] { context.getString(R.string.recents_tab), context.getString(R.string.favorites_tab) };
    }

    @Override public int getCount() {
        return tabTitles.length;
    }

    @NonNull
    @Override public NewsListFragment getItem(int position) {
        NewsListFragment f;

        switch (position) {
            case RECENTS_PAGE_POSITION:
                f = NewsListFragment.newInstance(MAIN_FLAG);
                break;
            case FAVORITES_PAGE_POSITION:
                f = NewsListFragment.newInstance(!MAIN_FLAG);
                break;
            default:
                throw new RuntimeException();
        }
        return f;
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}




