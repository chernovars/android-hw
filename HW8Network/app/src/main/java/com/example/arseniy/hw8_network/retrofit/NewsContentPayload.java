package com.example.arseniy.hw8_network.retrofit;

import com.google.gson.annotations.SerializedName;

public class NewsContentPayload {
    private NewsContentTitle title;

    @SerializedName("creationDate")
    private MsDate creationMsDate;

    @SerializedName("lastModificationDate")
    private MsDate lastModificationMsDate;

    private String content;

    private int bankInfoTypeId;

    private String typeId;

    public void setTitle(NewsContentTitle title){
        this.title = title;
    }
    public NewsContentTitle getTitle(){
        return this.title;
    }
    public void setCreationMsDate(MsDate creationMsDate){
        this.creationMsDate = creationMsDate;
    }
    public MsDate getCreationMsDate(){
        return this.creationMsDate;
    }
    public void setLastModificationMsDate(MsDate lastModificationMsDate){
        this.lastModificationMsDate = lastModificationMsDate;
    }
    public MsDate getLastModificationMsDate(){
        return this.lastModificationMsDate;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setBankInfoTypeId(int bankInfoTypeId){
        this.bankInfoTypeId = bankInfoTypeId;
    }
    public int getBankInfoTypeId(){
        return this.bankInfoTypeId;
    }
    public void setTypeId(String typeId){
        this.typeId = typeId;
    }
    public String getTypeId(){
        return this.typeId;
    }
}