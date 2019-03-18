package com.example.arseniy.hw5_storage;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[];
    private static final int RECENTS_PAGE_POSITION = 0;
    private static final int FAVORITES_PAGE_POSITION = 1;
    private static final boolean MAIN_FLAG = true;
    private Context mContext;

    TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabTitles = new String[] { context.getString(R.string.recents_tab), context.getString(R.string.favorites_tab) };
        mContext = context;
    }

    @Override public int getCount() {
        return tabTitles.length;
    }

    @Override public NewsListFragment getItem(int position) {
        NewsListFragment f;
        NewsRepository repo = NewsRepository.getInstance(mContext);
        switch (position) {
            case RECENTS_PAGE_POSITION:
                repo.removeAll();
                repo.add(Utils.generateNews(NewsListFragment.MOCK_NEWS_COUNT, mContext));
                f = NewsListFragment.newInstance(MAIN_FLAG);
                break;
            case FAVORITES_PAGE_POSITION:
                f = NewsListFragment.newInstance(!MAIN_FLAG);
                break;
            default:
                throw new RuntimeException();
        }
        f.setRetainInstance(true);
        return f;
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}

