package com.example.arseniy.hw8_network;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class NewsApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        // Normal app init code...
    }
}
