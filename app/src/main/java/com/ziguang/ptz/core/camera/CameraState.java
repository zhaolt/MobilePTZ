package com.ziguang.ptz.core.camera;

import android.support.annotation.IntDef;
import android.util.Log;

import com.ziguang.ptz.R;
import com.ziguang.ptz.ui.camera.CameraView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * Created by zhaoliangtai on 2017/9/5.
 */

public abstract class CameraState {

    private static final String TAG = CameraState.class.getSimpleName();

    public static final int MODE_PHOTO = 0;
    public static final int MODE_VIDEO = 1;

    /**
     * 这里用IntDef限制代码编写时输入类型
     */
    @IntDef({MODE_PHOTO, MODE_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraMode {}

    public @CameraState.CameraMode int cameraMode = MODE_PHOTO;

    public WeakReference<CameraView> cameraView;

    protected abstract void preSet();

    public abstract void take();

    public void setCameraMode(@CameraMode int cameraMode) {
        this.cameraMode = cameraMode;
    }

    public @CameraMode int getCameraMode() {
        return cameraMode;
    }

    public CameraState(CameraView cameraView) {
        this.cameraView = new WeakReference<>(cameraView);
        preSet();
        onInit();
    }

    /**
     * 0.重置状态
     * 1.初始化
     * 2.闲置状态
     * 3.工作状态
     */

    public void onInit() {
        Log.d(TAG, "onInit()");
        CameraView view = cameraView.get();
        switch (cameraMode) {
            case MODE_PHOTO:
                view.updateCameraShutter(R.mipmap.photo_shutter);
                break;
            case MODE_VIDEO:
                view.updateCameraShutter(R.mipmap.video_shutter_normal);
                break;
            default:
                throw new IllegalStateException("cameraMode : " + cameraMode);
        }
    }

    /**
     * 闲置状态
     */
    public void onIdle() {

    }

    /**
     * 工作状态
     */
    public void onWorking() {

    }

    public void onCameraModeBtnClicked() {

    }
}
