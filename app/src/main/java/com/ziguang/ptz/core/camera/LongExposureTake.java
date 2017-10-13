package com.ziguang.ptz.core.camera;

import com.ziguang.ptz.ui.camera.CameraView;

/**
 * 重影模式、光轨模式
 * Created by zhaoliangtai on 2017/10/9.
 */

public class LongExposureTake extends CameraState {


    public LongExposureTake(CameraView cameraView) {
        super(cameraView);
    }

    @Override
    protected void preSet() {
        setCameraMode(MODE_PHOTO);
    }

    @Override
    protected void take() {

    }
}
