package com.example.samplestickerapp;

import android.content.Context;
import android.content.SharedPreferences;

public class MyHelper {

    public static void saveData(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String retrieveData(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // The second parameter is a default value which will be returned if the key doesn't exist
        return prefs.getString(key, "");
    }
}
