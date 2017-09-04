package com.ziguang.ptz.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

import com.ziguang.ptz.App;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class UIUtils {

    public static View inflate(int vId) {
        return View.inflate(getContext(), vId, null);
    }

    public static Context getContext() {
        return App.INSTANCE;
    }

    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getScreenRate(Context context, int screenRotaion){
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        if (screenRotaion == 0) {
            return (H/W);
        } else {
            return (W/H);
        }
    }
}
