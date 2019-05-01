package com.example.arseniy.hw8_network;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.Application;
import android.os.Bundle;

import com.example.arseniy.hw8_network.persistence.NewsRepository;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!Utils.isConnected(this))
            Utils.showWarningDialog(this);

        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Application application = getApplication();
        if (Utils.isConnected(application))
            mCompositeDisposable.add(NewsRepository.getInstance(application).rxPopulateDBFromAPI());

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), application);
        viewPager.setAdapter(tabPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }
}

