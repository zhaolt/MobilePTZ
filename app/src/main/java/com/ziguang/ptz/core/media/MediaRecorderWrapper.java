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

    private boolean isRecording = false;

    private static boolean isLollipop = false;

    public static final String VIDEO_RESOLUTION = "video_size";
    public static final int VIDEO_RESOLUTION_1080P = 1080;
    public static final int VIDEO_RESOLUTION_720P = 720;
    public static final int VIDEO_RESOLUTION_480P = 480;

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
            isRecording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getVolumeLevel() {
        int volume = 0;
        if (mMediaRecorder != null && isRecording) {
            volume = mMediaRecorder.getMaxAmplitude() / 650;
            if (volume != 0)
                volume = (int) (10 * Math.log10(volume)) / 2;
            Log.d("volume", volume + "");
        }
        return volume;
    }

    public void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void stopRecordingVideo() {
        checkRecorderNotNull();
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            isRecording = false;
        } catch (RuntimeException e) {
            e.printStackTrace();
            stopRecordingVideo();
        }

    }

}
