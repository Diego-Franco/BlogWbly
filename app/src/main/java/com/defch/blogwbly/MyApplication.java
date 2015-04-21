package com.defch.blogwbly;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.defch.blogwbly.activities.SettingsActivity;
import com.defch.blogwbly.ifaces.IfaceSnapMap;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.WeeblyThemes;
import com.defch.blogwbly.util.SqlHelper;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/14/15.
 */
//TODO create a folder for the images on the posts
    //TODO create the test classes for the application
    //TODO pending pagination with infinite scrolling
    //TODO create the layout for tablets
public class MyApplication extends Application {

    private static MyApplication instance;

    private SqlHelper sqlHelper;

    private SharedPreferences mPref;

    private IfaceSnapMap ifaceSnapMap;

    public int sdkVersion = Build.VERSION.SDK_INT;
    private WeeblyThemes theme = WeeblyThemes.WEEBLY;

    private ArrayList<BlogPost> posts = new ArrayList<>();

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
        new LoadPostTask().execute();
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

    public void savePostOnDB(BlogPost post) {
        sqlHelper.insertPost(post);
    }

    public void updatePostOnDB(BlogPost post) {
        sqlHelper.updatePost(post);
    }

    public void deletePostOnDB(int postId) {
        sqlHelper.deletePost(postId);
    }

    public ArrayList<BlogPost> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<BlogPost> posts) {
        this.posts = posts;
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

    private class LoadPostTask extends AsyncTask<Void, Void, ArrayList<BlogPost>> {

        @Override
        protected ArrayList<BlogPost> doInBackground(Void... params) {
            posts = sqlHelper.getPosts();
            return posts;
        }
    }
}
