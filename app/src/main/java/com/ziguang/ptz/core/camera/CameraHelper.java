package com.ziguang.ptz.core.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import com.ziguang.ptz.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraHelper {

    private static final String TAG = CameraHelper.class.getSimpleName();

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static class SingleTon {
        public static final CameraHelper INSTANCE = new CameraHelper();
    }

    private ImageReader mImageReader;

    private CaptureRequest.Builder mPreviewBuilder;


    private HandlerThread mBackgroundThread;

    private Handler mBackgroundHandler;

    private String mCameraId = "0";

    private CameraCharacteristics mCameraCharacteristics;

    private Size mPreviewSize;


    // 回调
    private CameraCaptureSession.CaptureCallback mPreviewSessionCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }
    };

    private Surface mSurface;

    // 拍录音效
    private MediaActionSound mMediaActionSound;

    private CameraManager mCameraManager;

    private Size mlargest;

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    // TODO: 2017/8/19 保存照片
                }
            };


    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            startPreview(camera);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    private void startPreview(CameraDevice cameraDevice) {
        try {
            mPreviewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewBuilder.addTarget(mSurface);
            mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            //3A
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
            mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);
            cameraDevice.createCaptureSession(Arrays.asList(mSurface), mCameraCaptureSessionStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraCaptureSession.StateCallback mCameraCaptureSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            try {
                session.setRepeatingRequest(mPreviewBuilder.build(), mPreviewSessionCallback, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };

    private CameraHelper() {
    }

    public static CameraHelper getInstance() {
        return SingleTon.INSTANCE;
    }

    public void openCamera1() {

    }

    public void openCamera2(int width, int height, Activity activity, SurfaceTexture surfaceTexture) throws CameraAccessException {
        initBackgroundThread();
        setUpCameraOutputs(width, height);
        initOutputSurface(surfaceTexture);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mCameraManager.openCamera(mCameraId, mCameraStateCallback, mBackgroundHandler);
    }

    private void initBackgroundThread() {
        mBackgroundThread = new HandlerThread("Android_Lollipop_Camera2");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void initOutputSurface(SurfaceTexture surfaceTexture) {
        mImageReader = ImageReader.newInstance(mlargest.getWidth(), mlargest.getHeight(), ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        mSurface = new Surface(surfaceTexture);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpCameraOutputs(int width, int height) {
        mCameraManager = (CameraManager) App.INSTANCE.getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            //流配置
            StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            // TODO: 2017/8/19 选择jpeg 从数组中选择9:16的size
            Log.i(TAG, "yuv 444 888 " + map.getOutputSizes(ImageFormat.YUV_420_888).length);
            mlargest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)), new CompareSizesByArea());
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, mlargest);
            Log.i(TAG, "choose preview size width: " + mPreviewSize.getWidth() + ", height: " + mPreviewSize.getHeight());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e("nonono", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public void initShutter() {
        mMediaActionSound = new MediaActionSound();
        mMediaActionSound.load(MediaActionSound.SHUTTER_CLICK);
    }


    static class CompareSizesByArea implements Comparator<Size> {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }
}
