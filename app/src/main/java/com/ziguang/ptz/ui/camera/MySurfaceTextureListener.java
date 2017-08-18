package com.ziguang.ptz.ui.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import com.ziguang.ptz.App;
import com.ziguang.ptz.core.camera.CameraHelper;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class MySurfaceTextureListener implements TextureView.SurfaceTextureListener {


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        CameraHelper.getInstance().checkCameraInfo(App.INSTANCE);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


}
