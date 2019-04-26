package com.example.arseniy.hw8_network;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!Utils.isConnected(this))
            Utils.showExitDialog(this);
        else {
            setContentView(R.layout.activity_main);
            ViewPager viewPager = findViewById(R.id.viewpager);
            TabLayout tabLayout = findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);

            TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), MainActivity.this);
            viewPager.setAdapter(tabPagerAdapter);
        }
    }
}

