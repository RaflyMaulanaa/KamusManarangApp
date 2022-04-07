package com.example.kamusmanarang.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kamusmanarang.Api.Model.DataUser;

public class SharedPMUser {
    private static final String SHARED_PREF_NAME = "shared_pref_user";
    private static SharedPMUser mInstance;
    private Context mContext;

    private SharedPMUser(Context mContext2) {
        this.mContext = mContext2;
    }

    public static synchronized SharedPMUser getmInstance(Context mContext2) {
        SharedPMUser sharedPMUser;
        synchronized (SharedPMUser.class) {
            if (mInstance == null) {
                mInstance = new SharedPMUser(mContext2);
            }
            sharedPMUser= mInstance;
        }
        return sharedPMUser;
    }

    public void saveUser(DataUser dataUsers){
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putString("nama", dataUsers.getNama());
        editor.putString("username", dataUsers.getUsername());
        editor.apply();
    }

    public boolean isLoggedIn() {
        if (this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).getString("username", null) != null) {
            return true;
        }
        return false;
    }

    public DataUser getUser() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        DataUser dataUsers = new DataUser(
                sharedPreferences.getBoolean("error", false),
                sharedPreferences.getString("message", null),
                sharedPreferences.getString("nama", null),
                sharedPreferences.getString("username",null));
        return dataUsers;
    }

    public void clear() {
        SharedPreferences.Editor editor = this.mContext.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.clear();
        editor.apply();
    }
}
