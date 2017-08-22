package com.ziguang.ptz.ui.camera;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.utils.UIUtils;
import com.ziguang.ptz.widget.AutoFitTextureView;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class PreviewDisplayFragment extends Fragment implements TextureView.SurfaceTextureListener {

    private View mRootView;

    private AutoFitTextureView mDisplayView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = UIUtils.inflate(R.layout.fragment_camera_preview);
        mDisplayView = (AutoFitTextureView) mRootView.findViewById(R.id.preview_surface);
        mDisplayView.setSurfaceTextureListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CameraHelper.getInstance().initShutter();
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 2017/8/19 GLSurfaceView onResume
    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO: 2017/8/19 GLSurfaceView onPause disconnect camera device
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                CameraHelper.getInstance().openCamera(mDisplayView.getWidth(),
                        mDisplayView.getHeight(), getActivity(), mDisplayView);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
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
