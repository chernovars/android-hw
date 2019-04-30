package com.example.arseniy.hw8_network.retrofit;

import com.google.gson.annotations.SerializedName;

public class NewsListPayload
{
    private String id;
    private String text;
    @SerializedName("publicationDate")
    private MsDate publicationMsDate;

    public String getId(){
        return this.id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText(){
        return this.text;
    }

    public MsDate getPublicationMsDate(){
        return this.publicationMsDate;
    }

}
