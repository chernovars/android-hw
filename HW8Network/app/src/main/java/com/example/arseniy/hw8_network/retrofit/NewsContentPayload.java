package com.example.arseniy.hw8_network.retrofit;

public class NewsContentPayload {
    private NewsContentTitle title;

    private Date creationDate;

    private Date lastModificationDate;

    private String content;

    private int bankInfoTypeId;

    private String typeId;

    public void setTitle(NewsContentTitle title){
        this.title = title;
    }
    public NewsContentTitle getTitle(){
        return this.title;
    }
    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }
    public Date getCreationDate(){
        return this.creationDate;
    }
    public void setLastModificationDate(Date lastModificationDate){
        this.lastModificationDate = lastModificationDate;
    }
    public Date getLastModificationDate(){
        return this.lastModificationDate;
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