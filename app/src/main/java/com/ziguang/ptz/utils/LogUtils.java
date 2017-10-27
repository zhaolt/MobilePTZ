package com.ziguang.ptz.utils;

import android.util.Log;

import com.ziguang.ptz.BuildConfig;

/**
 * Created by zhaoliangtai on 2017/10/27.
 */

public class LogUtils {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    public static void v(String tag, String info) {
        if (DEBUG) {
            Log.v(tag, info);
        }
    }
    public static void d(String tag, String info) {
        if (DEBUG) {
            Log.d(tag, info);
        }
    }
    public static void i(String tag, String info) {
        if (DEBUG) {
            Log.i(tag, info);
        }
    }
    public static void w(String tag, String info) {
        if (DEBUG) {
            Log.w(tag, info);
        }
    }
    public static void e(String tag, String info) {
        if (DEBUG) {
            Log.e(tag, info);
        }
    }
}
