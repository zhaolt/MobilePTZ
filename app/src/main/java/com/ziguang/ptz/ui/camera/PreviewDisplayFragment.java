package com.ziguang.ptz.ui.camera;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.rx.NCSubscriber;
import com.ziguang.ptz.utils.UIUtils;
import com.ziguang.ptz.widget.AutoFitTextureView;
import com.ziguang.ptz.widget.FocusImageView;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class PreviewDisplayFragment extends Fragment implements TextureView.SurfaceTextureListener,
        View.OnTouchListener {

    private View mRootView;

    private AutoFitTextureView mDisplayView;

    private FocusImageView mFocusImageView;

    private GestureDetector mGestureDetector;

    private boolean isFocusing = false;

    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    private Runnable mDefaultFocusModeTask = new Runnable() {
        @Override
        public void run() {
            CameraHelper.getInstance().setUpFocusMode(
                    CameraHelper.getInstance().getCameraMode() ==
                            CameraHelper.PHOTO_CAMERA ?
                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE :
                            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            CameraHelper.getInstance().setUpSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            CameraHelper.getInstance().applyParameters();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = UIUtils.inflate(R.layout.fragment_camera_preview);
        mDisplayView = (AutoFitTextureView) mRootView.findViewById(R.id.preview_surface);
        mFocusImageView = (FocusImageView) mRootView.findViewById(R.id.focus_image);
        mDisplayView.setSurfaceTextureListener(this);
        mGestureDetector = new GestureDetector(getContext(), new MyGestureDetector());
        mDisplayView.setOnTouchListener(this);
        return mRootView;
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        CameraHelper.getInstance().openCamera(surface, rotation, CameraHelper.PHOTO_CAMERA)
                .subscribe();
    }

    public SurfaceTexture getSurfaceTexture() {
        return mDisplayView.getSurfaceTexture();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        CameraHelper.getInstance().closeCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public SurfaceTexture getDisplaySurfaceTexture() {
        return mDisplayView.getSurfaceTexture();
    }


    public void chooseVideoCamera() {
        CameraHelper.getInstance().chooseVideoMode(mDisplayView.getSurfaceTexture(),
                getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .subscribe(new NCSubscriber<Void>() {
                    @Override
                    public void doOnNext(Void aVoid) {
                        // TODO: 2017/10/12 enable shutters and more
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public String startRecordingVideo() {
        return CameraHelper.getInstance().startRecordingVideo(mDisplayView.getSurfaceTexture());
    }


    public void stopRecordingVideo(String filePath) {
        CameraHelper.getInstance().stopRecordingVideo(filePath);
    }

    public void areaFocus(Point point, int width, int height) {
        if (isFocusing) return;
        mMainThreadHandler.removeCallbacks(mDefaultFocusModeTask);
        isFocusing = true;
        float x = (float) point.x / (float) width;
        float y = (float) point.y / (float) height;
        mFocusImageView.startFocus(point);
        CameraHelper.getInstance().areaFocus(x, y, 1.0f, new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                isFocusing = false;
                if (success) {
                    mFocusImageView.onFocusSuccess();
                } else {
                    mFocusImageView.onFocusFailed();
                }
                // 2秒后恢复默认模式
                mMainThreadHandler.postDelayed(mDefaultFocusModeTask, 2000);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("Touch", "AutoFitTextureView onTouche");
        return mGestureDetector.onTouchEvent(event);
    }

    class MyGestureDetector implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {}

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Point point = new Point((int) e.getX(), (int) e.getY());
            areaFocus(point, mDisplayView.getWidth(), mDisplayView.getHeight());
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Point point = new Point((int) e.getX(), (int) e.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
