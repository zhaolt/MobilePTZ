package com.ziguang.ptz.core.camera;

import android.app.Activity;
import android.support.annotation.IntDef;

import com.ziguang.ptz.R;
import com.ziguang.ptz.ui.camera.CameraView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 单拍、定时拍、HDR
 * Created by zhaoliangtai on 2017/9/5.
 */

public class SimplePhotoTake extends CameraState {

    private static final String TAG = SimplePhotoTake.class.getSimpleName();

    public static final int MODE_SIMPLE_TAKE = 0;
    public static final int MODE_TIMER_TAKE = 1;
    public static final int MODE_HDR_TAKE = 2;

    @IntDef({MODE_SIMPLE_TAKE, MODE_TIMER_TAKE, MODE_HDR_TAKE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SimplePhotoMode {}

    private @SimplePhotoTake.SimplePhotoMode int mSimplePhotoMode = MODE_SIMPLE_TAKE;

    public SimplePhotoTake(CameraView cameraView) {
        super(cameraView);
    }

    @Override
    protected void preSet() {
        setCameraMode(MODE_PHOTO);
    }

    @Override
    public void take() {
        CameraHelper.getInstance().takePicture(
                ((Activity) cameraView.get()).getWindowManager().getDefaultDisplay().getRotation());
    }

    @Override
    public void onInit() {
        super.onInit();
        CameraView view = cameraView.get();
        switch (mSimplePhotoMode) {
            case MODE_SIMPLE_TAKE:
                view.updateCameraTakeModeIcon(R.drawable.photo_mode_simple_selector);
                break;
            case MODE_TIMER_TAKE:
                break;
            case MODE_HDR_TAKE:
                break;
        }
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
