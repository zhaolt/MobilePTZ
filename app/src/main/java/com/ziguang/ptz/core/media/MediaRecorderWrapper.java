package com.ziguang.ptz.core.media;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;

/**
 * Created by zhaoliangtai on 2017/8/24.
 */

public class MediaRecorderWrapper {

    private static final String TAG = MediaRecorderWrapper.class.getSimpleName();

    private MediaRecorder mMediaRecorder;

    private static boolean isLollipop = false;

    private MediaRecorderWrapper(MediaRecorder mediaRecorder) {
        mMediaRecorder = mediaRecorder;
    }


    public static MediaRecorderWrapper setUpMediaRecord(Camera camera, int videoWidth,
                                                        int videoHeight, int fps, int bitRate,
                                                        int rotation, Surface displaySurface) {
        MediaRecorderWrapper result = null;
        MediaRecorder mediaRecorder = new MediaRecorder();
        if (camera == null) {
            isLollipop = true;
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        } else {
            isLollipop = false;
//            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        }
        Log.i(TAG, "video width: " + videoWidth + ", height: " + videoHeight);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoFrameRate(fps);
        mediaRecorder.setVideoEncodingBitRate(bitRate);
        mediaRecorder.setVideoSize(videoWidth, videoHeight);
//        mediaRecorder.setOrientationHint(rotation);
        mediaRecorder.setPreviewDisplay(displaySurface);
        result = new MediaRecorderWrapper(mediaRecorder);
        return result;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Surface getSurface() {
        if (mMediaRecorder != null) {
            return mMediaRecorder.getSurface();
        }
        return null;
    }

    public void startRecordingVideo(String outputFilePath) {
        if (null == mMediaRecorder) {
            throw new NullPointerException("MediaRecorder is NULL!");
        }
        mMediaRecorder.setOutputFile(outputFilePath);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();
    }

    public void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void stopRecordingVideo() {
        if (null == mMediaRecorder) {
            throw new NullPointerException("MediaRecorder is NULL!");
        }
        mMediaRecorder.stop();
        mMediaRecorder.reset();
    }
}
