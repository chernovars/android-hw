package com.example.arseniy.hw8_network.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class News {
    @PrimaryKey
    public int id;
    @NonNull
    public String title;
    public Date date;
    public String shortDesc;
    public String fullDesc;
}
