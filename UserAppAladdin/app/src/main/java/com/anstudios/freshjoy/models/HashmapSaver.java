package com.anstudios.freshjoy.models;

import android.content.SharedPreferences;

import com.anstudios.freshjoy.SplashScreen;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;

public class HashmapSaver {
    public void saveHashMap(String key, Object obj) {
        SharedPreferences prefs = SplashScreen.sharedPreferences;
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public HashMap<String, String> getHashMap(String key) {
        SharedPreferences prefs = SplashScreen.sharedPreferences;
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> obj = gson.fromJson(json, type);
        return obj;
    }
}


