package com.example.arseniy.hw8_network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;

import androidx.core.util.Pair;

public class NewsJSON {
    @SerializedName("id")
    String id;
    @SerializedName("currencyName")
    String name;
    @SerializedName("currencySymbol")
    String text;
    LinkedList<Pair<String, Integer>> publicationDate;
    int bankInfoTypeId;
}
