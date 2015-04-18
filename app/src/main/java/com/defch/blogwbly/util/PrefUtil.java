package com.defch.blogwbly.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.defch.blogwbly.R;

/**
 * Created by DiegoFranco on 4/14/15.
 */
public class PrefUtil {

    private static PrefUtil instance;
    private SharedPreferences pref;

    public static PrefUtil getInstance(Context context) {
        if(instance == null) {
            instance = new PrefUtil(context);
        }
        return instance;
    }

    private PrefUtil(Context context) {
        pref = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveStringInformation(String key ,String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveIntInformation(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getStringInformation(String key) {
        return pref.getString(key, null);
    }

    public int getIntInformation(String key) {
        return pref.getInt(key, 0);
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
