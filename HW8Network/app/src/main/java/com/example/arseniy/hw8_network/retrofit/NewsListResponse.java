package com.example.arseniy.hw8_network.retrofit;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewsListResponse
{
    private String resultCode;

    @SerializedName("payload")
    private ArrayList<NewsListPayload> newsListPayload;

    private String trackingId;

    public void setResultCode(String resultCode){
        this.resultCode = resultCode;
    }
    public String getResultCode(){
        return this.resultCode;
    }
    public void setNewsListPayload(ArrayList<NewsListPayload> newsListPayload){
        this.newsListPayload = newsListPayload;
    }
    public ArrayList<NewsListPayload> getNewsListPayload(){
        return this.newsListPayload;
    }
    public void setTrackingId(String trackingId){
        this.trackingId = trackingId;
    }
    public String getTrackingId(){
        return this.trackingId;
    }
}

