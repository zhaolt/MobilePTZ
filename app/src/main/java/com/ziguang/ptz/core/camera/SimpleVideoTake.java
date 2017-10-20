package com.ziguang.ptz.core.camera;

import com.ziguang.ptz.R;
import com.ziguang.ptz.ui.camera.CameraView;

/**
 * Created by zhaoliangtai on 2017/9/5.
 */

public class SimpleVideoTake extends CameraState {

    private static final int TAKE_STATE_IDLE = 0;
    private static final int TAKE_STATE_WORKING = 1;

    private int mTakeState = TAKE_STATE_IDLE;

    public SimpleVideoTake(CameraView cameraView) {
        super(cameraView);
    }

    @Override
    protected void preSet() {
        setCameraMode(MODE_VIDEO);
    }

    @Override
    public void take() {
//        if (mTakeState == TAKE_STATE_IDLE) {
//            cameraView.get().startRecording();
//            onWorking();
//        } else {
//            onIdle();
//        }

    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onIdle() {
        super.onIdle();
        mTakeState = TAKE_STATE_IDLE;
        cameraView.get().updateCameraShutter(R.mipmap.video_shutter_normal);
    }

    @Override
    public void onWorking() {
        super.onWorking();
        mTakeState = TAKE_STATE_WORKING;
        cameraView.get().updateCameraShutter(R.drawable.video_shutter_recording_selector);
    }
}
