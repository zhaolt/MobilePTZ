package com.ziguang.ptz.core.jpeglib;

/**
 * Created by zhaoliangtai on 2017/8/21.
 */

public class JpegNativeInterface {

    static {
        System.loadLibrary("libnative");
        System.loadLibrary("libjpeg");
    }

    public static native void writeJpegFile(String fileName, byte[] yData, byte[] uData,
                                            byte[] vData, int quality, int width, int height);
}
