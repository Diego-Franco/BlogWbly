package com.defch.blogwbly.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.defch.blogwbly.MyApplication;
import com.defch.blogwbly.R;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.WeeblyThemes;
import com.defch.blogwbly.util.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by DiegoFranco on 4/16/15.
 */
public class BaseActivity extends ActionBarActivity {
    private final String TAG = getClass().getSimpleName();

    private static final String KEY_LAYOUT = "key_layout";
    private static final String POST_VALUE = "post_value";
    private static final String POST_OBJECT = "post_object";

    private boolean mIsLandscape;
    private boolean mIsTablet;

    public MyApplication app;
    public WeeblyThemes theme;

    @Override
    public void setContentView(int layoutResID) {
        LogUtil.v(TAG, "contentView");
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = MyApplication.getInstance(getApplicationContext());
        theme = app.getWTheme();
        theme.applyTheme(getTheme());
        super.onCreate(savedInstanceState);
        mIsLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mIsTablet = getResources().getBoolean(R.bool.is_tablet);
    }

    public void newIntent(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void newIntent(Class clazz, PostActivity.PostValue postValue) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(POST_VALUE, postValue);
        startActivity(intent);
    }

    public void newIntent(Class clazz, PostActivity.PostValue postValue, int position) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(POST_VALUE, postValue);
        intent.putExtra(KEY_LAYOUT, position);
        startActivity(intent);
    }

    public void newIntent(Class clazz, PostActivity.PostValue postValue, BlogPost post) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(POST_VALUE, postValue);
        intent.putExtra(POST_OBJECT, post);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean isLandscape() {
        return mIsLandscape;
    }

    public boolean ismIsTablet() {
        return mIsTablet;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor(int color) {
        if (app.sdkVersion >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

}
