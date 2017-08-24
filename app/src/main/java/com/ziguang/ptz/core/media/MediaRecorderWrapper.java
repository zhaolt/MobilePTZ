package com.ziguang.ptz.core.media;

import android.media.MediaRecorder;

/**
 * Created by zhaoliangtai on 2017/8/24.
 */

public class MediaRecorderWrapper {


    private MediaRecorder mMediaRecorder;

    private MediaRecorderWrapper() {
        mMediaRecorder = new MediaRecorder();
    }


    public static MediaRecorderWrapper setUpMediaRecord(Object camera, int videoWidth,
                                                        int videoHeight, int fps, int bitRate,
                                                        int rotation, String outputFilePath) {
        MediaRecorderWrapper result = null;
        return result;
    }

}
