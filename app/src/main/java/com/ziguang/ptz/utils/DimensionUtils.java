package com.ziguang.ptz.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.ziguang.ptz.App;

/**
 * Created by zero-zq on 2016/4/6.
 */
public class DimensionUtils {

    private static Context context = App.INSTANCE;

    public static int getSystemScreenWidth() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getSystemScreenHeight() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float getScreenDensity() {
        return context.getResources().getDisplayMetrics().density;
    }


    public static int Dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public int Px2Dp(float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    public static int px2sp(float pxValue) {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int SCREENWIDTH = 0;

}
