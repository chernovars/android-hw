package com.example.arseniy.hw4_recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    private HashSet<CharSequence> mFavorites = new HashSet<>();

    private BroadcastReceiver mFavoriteBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence title = intent.getCharSequenceExtra(NewsActivity.BROADCAST_RETURN_NEWS_TITLE_EXTRA);
            if (intent.getBooleanExtra(NewsActivity.BROADCAST_RETURN_IS_FAVORITE_EXTRA, false))
                mFavorites.add(title);
            else
                mFavorites.remove(title);
        }
    };

    public HashSet<CharSequence> getFavorites() {
        return mFavorites;
    }

    public BroadcastReceiver getFavoriteBroadcastReceiver() {
        return mFavoriteBroadcastReceiver;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new TabPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

