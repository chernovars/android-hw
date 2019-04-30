package com.example.arseniy.hw8_network;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.arseniy.hw8_network.persistence.NewsRepository;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!Utils.isConnected(this))
            Utils.showWarningDialog(this);

        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), getApplication());
        viewPager.setAdapter(tabPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewsRepository.getInstance(this).disposeSubscriptions();
    }
}

