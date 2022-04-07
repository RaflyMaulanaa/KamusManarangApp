package com.example.kamusmanarang.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DataUser {
    @SerializedName("error")
    boolean error;
    @SerializedName("message")
    String message;
    @SerializedName("nama")
    String nama;
    @SerializedName("username")
    String username;

    public DataUser(boolean error, String message, String nama, String username) {
        this.error = error;
        this.message = message;
        this.nama = nama;
        this.username = username;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }
}
