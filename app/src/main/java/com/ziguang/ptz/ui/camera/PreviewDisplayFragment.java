package com.ziguang.ptz.ui.camera;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.opengl.PreviewRenderer;
import com.ziguang.ptz.utils.UIUtils;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class PreviewDisplayFragment extends Fragment implements PreviewRenderer.OnGLSurfaceCreatedListener, SurfaceTexture.OnFrameAvailableListener {

    private View mRootView;


    private GLSurfaceView mDisplayView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = UIUtils.inflate(R.layout.fragment_camera_preview);
        mDisplayView = (GLSurfaceView) mRootView.findViewById(R.id.preview_surface);
        // choose OpenGL ES 2.0
        mDisplayView.setEGLContextClientVersion(2);
        PreviewRenderer renderer = new PreviewRenderer();
        renderer.setGLSurfaceCreatedListener(this);
        mDisplayView.setRenderer(renderer);
        mDisplayView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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
    public void onGLSurfaceCreated(SurfaceTexture surfaceTexture) {
        surfaceTexture.setOnFrameAvailableListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                CameraHelper.getInstance().openCamera2(mDisplayView.getWidth(),
                        mDisplayView.getHeight(), getActivity(), surfaceTexture);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mDisplayView.requestRender();
    }
}
