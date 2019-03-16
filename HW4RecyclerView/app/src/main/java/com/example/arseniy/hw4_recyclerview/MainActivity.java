package com.example.arseniy.hw4_recyclerview;

import androidx.annotation.NonNull;
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

    private static final String FAVORITES_MAP_EXTRA = "favorites_map_extra";

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
            NewsListFragment favFrag = mTabPagerAdapter.mFavoritesFragment;
            if (favFrag != null) {
                NewsListAdapter favNewsListAdapter = favFrag.getAdapter();
                updateFavoritesAdapter(favNewsListAdapter);
            }
        }
    };

    public void updateFavoritesAdapter(@NonNull NewsListAdapter favNewsListAdapter){

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

        LocalBroadcastManager.getInstance(this).registerReceiver(mFavoriteBroadcastReceiver,
                new IntentFilter(NewsActivity.BROADCAST_INTENT_ACTION));

        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(mTabPagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FAVORITES_MAP_EXTRA, mFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mFavorites = (HashMap<CharSequence, Date>) savedInstanceState.getSerializable(FAVORITES_MAP_EXTRA);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mFavoriteBroadcastReceiver);
        super.onDestroy();
    }
}

