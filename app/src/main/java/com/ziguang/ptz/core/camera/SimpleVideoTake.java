package com.ziguang.ptz.core.camera;

import com.ziguang.ptz.ui.camera.CameraView;

/**
 * Created by zhaoliangtai on 2017/9/5.
 */

public class SimpleVideoTake extends CameraState {
    public SimpleVideoTake(CameraView cameraView) {
        super(cameraView);
    }

    @Override
    protected void preSet() {
        setCameraMode(MODE_VIDEO);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onIdle() {
        super.onIdle();
    }

    @Override
    public void onWorking() {
        super.onWorking();
    }
}
