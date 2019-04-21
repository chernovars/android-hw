package com.example.arseniy.hw8_network.retrofit;

public class NewsListPayload
{
    private String id;

    private String name;

    private String text;

    private Date publicationDate;

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
    public void setPublicationDate(Date publicationDate){
        this.publicationDate = publicationDate;
    }
    public Date getPublicationDate(){
        return this.publicationDate;
    }
    public void setBankInfoTypeId(int bankInfoTypeId){
        this.bankInfoTypeId = bankInfoTypeId;
    }
    public int getBankInfoTypeId(){
        return this.bankInfoTypeId;
    }
}
