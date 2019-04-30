package com.example.arseniy.hw8_network.retrofit;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INewsService {

    @GET("v1/news")
    Single<TinkoffApiResponse<ArrayList<NewsListPayload>>> getNewsListResponse();

    @GET("v1/news_content")
    Single<TinkoffApiResponse<NewsContentPayload>> getNewsContentResponse(@Query("id") int id);

}
