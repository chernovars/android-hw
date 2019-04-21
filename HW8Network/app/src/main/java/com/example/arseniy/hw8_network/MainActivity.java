package com.example.arseniy.hw8_network;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.arseniy.hw8_network.retrofit.NewsContentResponse;
import com.example.arseniy.hw8_network.retrofit.NewsListPayload;
import com.example.arseniy.hw8_network.retrofit.NewsListResponse;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isConnected(MainActivity.this))
            buildDialog(MainActivity.this).show();
        else {
            setContentView(R.layout.activity_main);
            ViewPager viewPager = findViewById(R.id.viewpager);
            TabLayout tabLayout = findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);

            TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), MainActivity.this);
            viewPager.setAdapter(tabPagerAdapter);
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Нет доступа к интернету");
        builder.setMessage("Нажмите ОК для выхода.");

        builder.setPositiveButton("ОК", (dialog, which) -> finish());

        return builder;
    }

}

