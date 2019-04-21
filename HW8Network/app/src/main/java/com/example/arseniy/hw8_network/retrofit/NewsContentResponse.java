package com.example.arseniy.hw8_network.retrofit;

import com.google.gson.annotations.SerializedName;

public class NewsContentResponse {
    private String resultCode;

    @SerializedName("payload")
    private NewsContentPayload newsContentPayload;

    private String trackingId;

    public void setResultCode(String resultCode){
        this.resultCode = resultCode;
    }
    public String getResultCode(){
        return this.resultCode;
    }
    public void setNewsContentPayload(NewsContentPayload newsContentPayload){
        this.newsContentPayload = newsContentPayload;
    }
    public NewsContentPayload getNewsListPayload(){
        return this.newsContentPayload;
    }
    public void setTrackingId(String trackingId){
        this.trackingId = trackingId;
    }
    public String getTrackingId(){
        return this.trackingId;
    }
}