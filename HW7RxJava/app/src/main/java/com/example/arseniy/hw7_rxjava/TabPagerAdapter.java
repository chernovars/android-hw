package com.example.arseniy.hw7_rxjava;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

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

        switch (position) {
            case RECENTS_PAGE_POSITION:
                NewsRepository.getInstance(mContext).rxPopulateDB(mContext);
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




