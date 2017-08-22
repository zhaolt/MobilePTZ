package com.ziguang.ptz.core.jpeglib;

import java.nio.ByteBuffer;

/**
 * Created by zhaoliangtai on 2017/8/21.
 */

public class JpegNativeInterface {

    static {
        System.loadLibrary("native");
        System.loadLibrary("jpeg");
    }

    private static class SingleTon {
        private static final JpegNativeInterface INSTANCE = new JpegNativeInterface();
    }

    private JpegNativeInterface() {}

    public static JpegNativeInterface getInstance() {
        return SingleTon.INSTANCE;
    }

    public native void writeJpegFile(String fileName,
                                     ByteBuffer y, int yLen,
                                     ByteBuffer cb, int cbLen,
                                     ByteBuffer cr, int uvStride,
                                     int quality, int width, int height);
}
