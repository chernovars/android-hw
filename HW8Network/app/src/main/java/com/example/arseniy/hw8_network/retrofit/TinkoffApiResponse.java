package com.example.arseniy.hw8_network.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TinkoffApiResponse<T> implements Serializable {
    @SerializedName("payload")
    private T payload;

    public T getPayload() {
        return payload;
    }
}
