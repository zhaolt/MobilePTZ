package com.ziguang.ptz.core.camera;

import com.ziguang.ptz.ui.camera.CameraView;

/**
 * 180° 330° 九宫格
 * Created by zhaoliangtai on 2017/10/9.
 */

public class PanShotTake extends CameraState {
    public PanShotTake(CameraView cameraView) {
        super(cameraView);
    }

    @Override
    protected void preSet() {
        setCameraMode(MODE_PHOTO);
    }

    @Override
    public void take() {

    }

    @Override
    public void onInit() {
        super.onInit();

    }
}
