package com.defch.blogwbly.util;

import android.util.Log;

import com.defch.blogwbly.BuildConfig;


/**
 * Created by DiegoFranco on 4/16/15.
 */
public class LogUtil {
    public static boolean LOG_SENTENCES = BuildConfig.DEBUG;

    public static void onCreateApplication(boolean writeLogs) {
        LOG_SENTENCES = writeLogs;
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LOG_SENTENCES) Log.v(tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (LOG_SENTENCES) Log.v(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_SENTENCES) Log.w(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (LOG_SENTENCES) Log.w(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_SENTENCES) Log.e(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        if (LOG_SENTENCES) Log.e(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_SENTENCES) Log.d(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (LOG_SENTENCES) Log.d(tag, msg);
    }
}
