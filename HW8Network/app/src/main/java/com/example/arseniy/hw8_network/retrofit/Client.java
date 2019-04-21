package com.example.arseniy.hw8_network.retrofit;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Client ourInstance = new Client();
    private static INewsService api;

    private static final String BASE_URL = "https://api.tinkoff.ru/";

    public static Client getInstance() {
        return ourInstance;
    }

    private Client() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();


        api = retrofit.create(INewsService.class);
    }

    public INewsService getApi() {
        return api;
    }
}