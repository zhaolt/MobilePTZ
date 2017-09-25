package com.ziguang.ptz.utils;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
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

    public static float dp2px(float dpValue) {
        final float scale = App.INSTANCE.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
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

    public static int[] getVideoSize(String path) {
        int[] size = new int[2];
        if (path.endsWith("jpg")
                || path.endsWith("jpeg")
                || path.endsWith("JPG")
                || path.endsWith("JPEG")) {
            return size;
        }
        int w;
        int h;
        try {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(path);
            String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            int orientation = getVideoOrientation(path);

            if (orientation == 0) {
                w = Integer.valueOf(width);
                h = Integer.valueOf(height);
            } else {
                h = Integer.valueOf(width);
                w = Integer.valueOf(height);
            }
        } catch (Exception e) {
            w = 1;
            h = 1;
        }
        System.out.println("getVideoSize, width: " + w + ", height: " + h);
        size[0] = w;
        size[1] = h;
        return size;
    }


    public static int getVideoOrientation(String path) {
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        try {
            retr.setDataSource(path);
        } catch (Exception e) {
            return 0;
        }
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
        if("0".equals(rotation)||"180".equals(rotation)||"360".equals(rotation)){
            return 0;
        } else {
            return 1;
        }
    }


    public static boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        return (x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()));
    }
}
