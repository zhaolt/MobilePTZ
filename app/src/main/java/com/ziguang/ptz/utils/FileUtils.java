package com.ziguang.ptz.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by zhaoliangtai on 2017/8/23.
 */

public class FileUtils {

    public static final String ROOT_DIR_PATH = Environment.getExternalStorageDirectory().getPath() +
            File.separator + "ziguang" + File.separator;

    public static final String VIDEO_DIR_PATH = ROOT_DIR_PATH + "video" + File.separator;

    public static final String PHOTO_DIR_PATH = ROOT_DIR_PATH + "photo" + File.separator;



    public static File createJpeg() {
        return new File(FileUtils.PHOTO_DIR_PATH + getPrefix() + ".jpg");
    }

    public static File createMP4() {
        return new File(FileUtils.VIDEO_DIR_PATH + getPrefix() + ".mp4");
    }

    public static File createMov() {
        return new File(FileUtils.VIDEO_DIR_PATH + getPrefix() + ".mov");
    }

    public static boolean checkJpegDir() {
        File file = new File(FileUtils.PHOTO_DIR_PATH);
        return file.exists();
    }

    private static String getPrefix() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(new Date());
        int random = new Random().nextInt(1000);
        return "VID" + time + "_" + random;
    }


    public static boolean saveBitmap(File file, Bitmap b){
        try {
            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void createDirs() {
        List<String> dirs = new ArrayList<>();
        dirs.add(FileUtils.PHOTO_DIR_PATH);
        dirs.add(FileUtils.VIDEO_DIR_PATH);
        dirs.add(FileUtils.PHOTO_DIR_PATH);
        for (String dir : dirs) {
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
