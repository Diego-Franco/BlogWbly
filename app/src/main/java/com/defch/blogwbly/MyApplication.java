package com.defch.blogwbly;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.defch.blogwbly.activities.SettingsActivity;
import com.defch.blogwbly.ifaces.IfaceSnapMap;
import com.defch.blogwbly.ui.WeeblyThemes;
import com.defch.blogwbly.util.SqlHelper;

/**
 * Created by DiegoFranco on 4/14/15.
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    private SqlHelper sqlHelper;

    private SharedPreferences mPref;

    private IfaceSnapMap ifaceSnapMap;

    public int sdkVersion = Build.VERSION.SDK_INT;
    private WeeblyThemes theme = WeeblyThemes.WEEBLY;

    public static MyApplication getInstance() {
        return instance;
    }

    public static MyApplication getInstance(Context context) {
        return context != null ? (MyApplication) context.getApplicationContext() : instance;
    }

    /**
     * Called when the application is starting, before any other application objects have been created.
     * and i create the instance of SharedPreferences
     */
    @Override
    public void onCreate() {
        super.onCreate();
        sqlHelper = new SqlHelper(getApplicationContext());
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = WeeblyThemes.getThemeFromString(mPref.getString(SettingsActivity.THEME_KEY, "weebly"));
        theme.isDarkTheme = mPref.getBoolean(SettingsActivity.KEY_DARK_THEME, false);
    }

    public WeeblyThemes getWTheme() {
        return theme;
    }

    public void setTheme(@NonNull WeeblyThemes theme) {
        this.theme = theme;
    }

    public SharedPreferences getPreferences() {
        return mPref;
    }

    public SqlHelper getSql() {
        return sqlHelper;
    }

    public IfaceSnapMap getIfaceSnapMap() {
        return ifaceSnapMap;
    }

    public void setIfaceSnapMap(IfaceSnapMap ifaceSnapMap) {
        this.ifaceSnapMap = ifaceSnapMap;
    }

    /**
     * This method is for use in emulated process environments.
     * It will never be called on a production Android device,
     * where processes are removed by simply killing them;
     * no user code (including this callback) is executed when doing so.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * Called by the system when the device configuration changes while your component is running.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * This is called when the overall system is running low on memory,
     * and would like actively running processes to tighten their belts.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
