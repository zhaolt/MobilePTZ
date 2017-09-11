package com.ziguang.ptz.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;

import java.io.File;

/**
 * Created by zhaoliangtai on 2017/8/23.
 */

public class ImageUtils {
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }

    public static void addToMediaStore(Context context, File file) {
        String[] path = new String[]{file.getPath()};
        MediaScannerConnection.scanFile(context, path, null, null);
    }
}
