package com.example.arseniy.hw4_recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[];
    private static final int RECENTS_PAGE_POSITION = 0;
    private static final int FAVORITES_PAGE_POSITION = 1;
    NewsListFragment mFavoritesFragment;

    TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabTitles = new String[] { context.getString(R.string.recents_tab), context.getString(R.string.favorites_tab) };
    }

    @Override public int getCount() {
        return tabTitles.length;
    }

    @Override public NewsListFragment getItem(int position) {
        NewsListFragment f;
        switch (position) {
            case RECENTS_PAGE_POSITION:
                f = NewsListFragment.newInstance(Utils.generateMockData(NewsListFragment.MOCK_NEWS_COUNT),
                                                 Utils.generateMockDates(NewsListFragment.MOCK_NEWS_COUNT));
                break;
            case FAVORITES_PAGE_POSITION:
                f = NewsListFragment.newInstance();
                break;
            default:
                throw new RuntimeException();
        }
        f.setRetainInstance(true);
        return f;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // функция для сохранения ссылки на фрагмент избранных, срабатывает и после поворота экрана
        NewsListFragment createdFragment = (NewsListFragment) super.instantiateItem(container, position);
        if (position == FAVORITES_PAGE_POSITION)
            mFavoritesFragment = createdFragment;
        return createdFragment;
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
