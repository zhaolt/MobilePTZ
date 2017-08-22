package com.ziguang.ptz.core.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by zhaoliangtai on 2017/8/22.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JpegSessionCallback extends CameraCaptureSession.CaptureCallback {

    private static final String TAG = JpegSessionCallback.class.getSimpleName();

    private Handler mHandler;

    private MediaActionSound mMediaActionSound;

    public JpegSessionCallback(Handler handler, MediaActionSound mediaActionSound) {
        mHandler = handler;
        mMediaActionSound = mediaActionSound;
    }

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
        if (afState == null || aeState == null) {
            return;
        }

        if (afState.intValue() == CameraMetadata.CONTROL_AF_STATE_FOCUSED_LOCKED || afState.intValue() == CameraMetadata.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED
                || CaptureResult.CONTROL_AF_STATE_PASSIVE_FOCUSED == afState.intValue() || CaptureResult.CONTROL_AF_STATE_PASSIVE_UNFOCUSED == afState.intValue()) {
            Log.i("JpegSessionCallback", "进去了一层,,aeState.intValue()--->" + aeState.intValue());
            if (aeState.intValue() == CameraMetadata.CONTROL_AE_STATE_LOCKED || aeState.intValue() == CameraMetadata.CONTROL_AE_STATE_PRECAPTURE
                    || aeState.intValue() == CameraMetadata.CONTROL_AE_STATE_FLASH_REQUIRED || aeState.intValue() == CameraMetadata.CONTROL_AE_STATE_CONVERGED) {
                Log.i("JpegSessionCallback", "进去了两层");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
                    }
                });
            } else {
//                mMainHanler.sendEmptyMessage(DisplayFragment.FOCUS_AGAIN);
            }
        }
    }
}
