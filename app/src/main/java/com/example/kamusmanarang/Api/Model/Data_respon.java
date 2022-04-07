package com.example.kamusmanarang.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data_respon {
    @SerializedName("error")
    boolean error;
    @SerializedName("result")
    String result;
    @SerializedName("message")
    String message;

    public Data_respon(boolean error, String result, String message) {
        this.error = error;
        this.result = result;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
