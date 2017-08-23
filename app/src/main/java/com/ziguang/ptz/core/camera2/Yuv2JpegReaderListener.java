package com.ziguang.ptz.core.camera2;

import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.ziguang.ptz.core.jpeglib.JpegNativeInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by zhaoliangtai on 2017/8/22.
 */

public class Yuv2JpegReaderListener implements ImageReader.OnImageAvailableListener {


    @Override
    public void onImageAvailable(ImageReader reader) {
        new Thread(new ImageSaver(reader)).start();
    }

    class ImageSaver implements Runnable {

        private ImageReader mImageReader;

        ImageSaver(ImageReader imageReader) {
            mImageReader = imageReader;
        }

        @Override
        public void run() {
            Image image = mImageReader.acquireLatestImage();
            ByteBuffer[] planeBuf = new ByteBuffer[3];
            int uvStride = 1;
            for (int i = 0; i < 3; i++) {
                Image.Plane plane = image.getPlanes()[i];
                planeBuf[i] = plane.getBuffer();
                uvStride = plane.getPixelStride();
            }
            checkParentDir();
            checkJpegDir();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android_L_Test/" + "a.jpg";
            long startTime = SystemClock.uptimeMillis();
            JpegNativeInterface.getInstance().writeJpegFile(path,
                    planeBuf[0], planeBuf[0].remaining(),
                    planeBuf[1], planeBuf[1].remaining(),
                    planeBuf[2], uvStride,
                    95, image.getWidth(), image.getHeight());
            Log.i(TAG, "time: " + (SystemClock.uptimeMillis() - startTime));
            image.close();
        }
    }

    private void checkParentDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 判断文件夹是否存在
     */
    private void checkJpegDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/jpeg/");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private File createJpeg() {
        long time = System.currentTimeMillis();
        int random = new Random().nextInt(1000);
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android_L_Test/jpeg/");
        Log.i("JpegSaver", time + "_" + random + ".jpg");
        return new File(dir, time + "_" + random + ".jpg");
    }


    /**
     * 保存
     *
     * @param bytes
     * @param file
     * @throws java.io.IOException
     */
    private void save(byte[] bytes, File file) throws IOException {
        Log.i("JpegSaver", "save");
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
