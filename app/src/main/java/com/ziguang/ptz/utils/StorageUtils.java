package com.ziguang.ptz.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.ziguang.ptz.core.media.MediaRecorderWrapper;

import java.io.File;

/**
 * Created by zhaoliangtai on 2017/10/27.
 */

public class StorageUtils {
    public static float readSDCard() {
        String state = Environment.getExternalStorageState();
        float availableSize = 0;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long size = sf.getAvailableBytes();
            availableSize = size / 1024f / 1024f;
            Log.i("", "available size = " + availableSize);
        }
        return availableSize;
    }

    public static String getSurplusTime() {
        return TimeUtils.getDurationSecond((int) (readSDCard() * 1024 * 1024 /
                (Float.valueOf((String) SharedPrefUtils.getParam(MediaRecorderWrapper.VIDEO_RESOLUTION, "2"))
                        * 1024 * 1024)));
    }

//    public static int getSurplusTime()
}
