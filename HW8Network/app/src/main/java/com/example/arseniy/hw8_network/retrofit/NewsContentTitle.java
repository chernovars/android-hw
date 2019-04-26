package com.example.arseniy.hw8_network.retrofit;

import com.google.gson.annotations.SerializedName;

public class NewsContentTitle {
    private String id;

    private String name;

    private String text;

    @SerializedName("publicationDate")
    private MsDate publicationMsDate;

    private int bankInfoTypeId;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setPublicationMsDate(MsDate publicationMsDate){
        this.publicationMsDate = publicationMsDate;
    }
    public MsDate getPublicationMsDate(){
        return this.publicationMsDate;
    }
    public void setBankInfoTypeId(int bankInfoTypeId){
        this.bankInfoTypeId = bankInfoTypeId;
    }
    public int getBankInfoTypeId(){
        return this.bankInfoTypeId;
    }
}
