package com.example.arseniy.hw8_network.retrofit;

import com.example.arseniy.hw8_network.BuildConfig;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Client ourInstance = new Client();
    private static INewsService api;

    public static Client getInstance() {
        return ourInstance;
    }

    private Client() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.tinkoff_news_api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();


        api = retrofit.create(INewsService.class);
    }

    public INewsService getApi() {
        return api;
    }
}