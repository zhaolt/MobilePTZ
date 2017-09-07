package com.ziguang.ptz.core.media;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;

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
            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        }
        Log.i(TAG, "video width: " + videoWidth + ", height: " + videoHeight);
        mediaRecorder.setOrientationHint(rotation);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(2);
        mediaRecorder.setVideoEncodingBitRate(bitRate);
        mediaRecorder.setVideoFrameRate(fps);
        mediaRecorder.setVideoSize(videoWidth, videoHeight);
        mediaRecorder.setPreviewDisplay(displaySurface);
        result = new MediaRecorderWrapper(mediaRecorder);
        return result;
    }

    private void checkRecorderNotNull() {
        if (null == mMediaRecorder) {
            throw new NullPointerException("MediaRecorder is NULL!");
        }
    }

    public void startRecordingVideo(String outputFilePath) {
        checkRecorderNotNull();
        mMediaRecorder.setOutputFile(outputFilePath);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void stopRecordingVideo() {
        checkRecorderNotNull();
        mMediaRecorder.stop();
        mMediaRecorder.reset();
    }

}
