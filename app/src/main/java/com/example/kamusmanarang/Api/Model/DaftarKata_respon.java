package com.example.kamusmanarang.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DaftarKata_respon {
    @SerializedName("error")
    boolean error;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<DaftarKata> data;

    public DaftarKata_respon(boolean status, String message, List<DaftarKata> data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<DaftarKata> getData() {
        return data;
    }
}
