package com.example.kamusmanarang.Api;

import com.example.kamusmanarang.Api.Model.DaftarKata_respon;
import com.example.kamusmanarang.Api.Model.Data_respon;
import com.example.kamusmanarang.Api.Model.DataUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InterfaceApi {

    @FormUrlEncoded
    @POST("daftarkata.php")
    Call<DaftarKata_respon> getDaftarKata(@Field("pencarian") String str);

    @FormUrlEncoded
    @POST("translatemamuju.php")
    Call<Data_respon> translateMamuju(@Field("bahasa") String str,
                                      @Field("translate") String str1);

    @FormUrlEncoded
    @POST("translategoogle.php")
    Call<Data_respon> translateGoogle(@Field("from") String str,
                                      @Field("to") String str1,
                                      @Field("translate") String str2);

    @FormUrlEncoded
    @POST("login.php")
    Call<DataUser> login(@Field("username") String str,
                         @Field("password") String str1);

    @FormUrlEncoded
    @POST("kosakata.php")
    Call<Data_respon> kosakata(@Field("action") String str,
                               @Field("indonesia") String str1,
                               @Field("mamuju") String str2,
                               @Field("id") String str3);
}
