package com.example.kamusmanarang.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
//    public static final String BASE_URL = "http://192.168.1.6/kamusmanarang/";
    public static final String BASE_URL = "http://kamusmanarang.mattoanginjaya.com/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
