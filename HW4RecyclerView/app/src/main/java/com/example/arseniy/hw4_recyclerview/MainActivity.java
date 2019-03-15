package com.example.arseniy.hw4_recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //Мапа нужна для быстрой проверки на существование ключа -  хранение "избранности" новости
    private HashMap<CharSequence, Date> mFavorites = new HashMap<>();
    TabPagerAdapter mTabPagerAdapter;

    // Получение информации о добавлении/удалении из избранных (происходит в NewsActivity)
    private BroadcastReceiver mFavoriteBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence title = intent.getCharSequenceExtra(NewsActivity.BROADCAST_RETURN_NEWS_TITLE_EXTRA);
            if (intent.getBooleanExtra(NewsActivity.BROADCAST_RETURN_IS_FAVORITE_EXTRA, false)) {
                Date newsDate = Utils.parseDateCharSequence(intent.getCharSequenceExtra(NewsActivity.BROADCAST_RETURN_NEWS_DATE_EXTRA));
                mFavorites.put(title, newsDate);
                Toast.makeText(context, R.string.toast_added_to_favorites, Toast.LENGTH_SHORT).show();
            }
            else
                mFavorites.remove(title);

            //Обновляем адаптер для фрагмента "избранное"
            NewsListAdapter favNewsListAdapter = mTabPagerAdapter.getItem(TabPagerAdapter.FAVORITES_PAGE_POSITION).getAdapter();
            updateFavoritesAdapter(favNewsListAdapter);
        }
    };

    public void updateFavoritesAdapter(NewsListAdapter favNewsListAdapter){

        //Переводим мапу избранных в список нужный адаптеру в RecyclerView "избранное"
        ArrayList<Pair<CharSequence, Date>> favoritesList = new ArrayList<>();
        for (Map.Entry<CharSequence, Date> entry : mFavorites.entrySet()) {
            favoritesList.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        favNewsListAdapter.setDataset(favoritesList);
    }

    public boolean isFavorite(CharSequence title) {
        return mFavorites.containsKey(title);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //обработка поворота экрана
        MainActivity prevActivity = (MainActivity) getLastCustomNonConfigurationInstance();
        if (prevActivity != null) {
            //получаем старые "избранные" и PagerAdapter со старыми фрагментами внутри
            mFavorites = prevActivity.mFavorites;
            mTabPagerAdapter = prevActivity.mTabPagerAdapter;
            viewPager.setAdapter(mTabPagerAdapter);
            NewsListFragment favFragment = mTabPagerAdapter.getItem(TabPagerAdapter.FAVORITES_PAGE_POSITION);
            NewsListAdapter favAdapter = favFragment.getAdapter();
            updateFavoritesAdapter(favAdapter);
        }
        else {
            LocalBroadcastManager.getInstance(this).registerReceiver(mFavoriteBroadcastReceiver,
                    new IntentFilter(NewsActivity.BROADCAST_INTENT_ACTION));
            mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), MainActivity.this);
            viewPager.setAdapter(mTabPagerAdapter);
        }

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        // для сохранения адаптера, хрянящего фрагменты для ViewPager при повороте
        return this;
    }
}

