package com.example.kamusmanarang.Api.Model;

import com.google.gson.annotations.SerializedName;

public class DaftarKata {
    @SerializedName("id")
    String id;
    @SerializedName("indonesia")
    String indonesia;
    @SerializedName("mamuju")
    String mamuju;

    public DaftarKata(String id, String indonesia, String mamuju) {
        this.id = id;
        this.indonesia = indonesia;
        this.mamuju = mamuju;
    }

    public String getId() {
        return id;
    }

    public String getIndonesia() {
        return indonesia;
    }

    public String getMamuju() {
        return mamuju;
    }
}
