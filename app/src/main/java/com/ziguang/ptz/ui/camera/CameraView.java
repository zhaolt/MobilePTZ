package com.ziguang.ptz.ui.camera;

/**
 * Created by zhaoliangtai on 2017/10/9.
 */

public interface CameraView {

    void updateCameraShutter(int resourceId);

    void updateCameraTakeModeIcon(int resourceId);

    void updatePhotoTakeModeMenu(int resId0, int resId1, int resId2);

    void updateVideoTakeModeMenu(int resId0, int resId1);

}
