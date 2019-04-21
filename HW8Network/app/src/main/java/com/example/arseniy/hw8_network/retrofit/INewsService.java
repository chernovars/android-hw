package com.example.arseniy.hw8_network.retrofit;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INewsService {

    @GET("v1/news")
    Observable<NewsListResponse> getNewsListResponse();

    @GET("v1/news_content")
    Observable<NewsContentResponse> getNewsContentResponse(@Query("id") int id);

}
