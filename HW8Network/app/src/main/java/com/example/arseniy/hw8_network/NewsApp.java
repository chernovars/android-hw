package com.example.arseniy.hw8_network;

import android.app.Application;

import com.example.arseniy.hw8_network.persistence.NewsRepository;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsApp extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) return;
            LeakCanary.install(this);
        }

        NewsRepository newsRepository = NewsRepository.getInstance(this);

        newsRepository.rxDeleteAllExceptNewestAndFavorites(NewsRepository.TOP_NEWS_TO_PRESERVE)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static Disposable rxPopulateDBFromAPI(NewsRepository repository) {
        //this.removeAll();
        return repository.rxDownloadNewsListPayload()
                .subscribeOn(Schedulers.io())
                .subscribe(repository::add);
    }
}
