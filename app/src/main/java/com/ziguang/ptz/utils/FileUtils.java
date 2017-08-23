package com.ziguang.ptz.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by zhaoliangtai on 2017/8/23.
 */

public class FileUtils {
    /**
     * 判断文件夹是否存在
     */
    public static void checkJpegDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/jpeg/");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static File createJpeg() {
        long time = System.currentTimeMillis();
        int random = new Random().nextInt(1000);
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/jpeg/");
        Log.i("JpegSaver", time + "_" + random + ".jpg");
        return new File(dir, time + "_" + random + ".jpg");
    }


    public static void saveBitmap(File file, Bitmap b){
        try {
            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
