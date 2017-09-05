package com.ziguang.ptz.core.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import com.ziguang.ptz.core.media.MediaRecorderWrapper;
import com.ziguang.ptz.utils.FileUtils;
import com.ziguang.ptz.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhaoliangtai on 2017/8/23.
 */

public class CameraHelper {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    public static final int PHOTO_CAMERA = 1001;

    public static final int VIDEO_CAMERA = 1007;

    private static final int REAR_CAMERA = 0;

    private static final int FRONT_CAMERA = 1;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final String TAG = CameraHelper.class.getSimpleName();

    private Camera mCamera;

    private Camera.Parameters mParameters;

    private Camera.Size mVideoSize;

    private boolean isPreview = false;

    private int mRotation = -1;

    private int mCameraID = 0;

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap b = null;
            if(null != data){
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
                isPreview = false;
            }
            //保存图片到sdcard
            if(null != b) {
                Bitmap rotaBitmap = ImageUtils.getRotateBitmap(b, ORIENTATIONS.get(mRotation));
                FileUtils.checkJpegDir();
                File file = FileUtils.createJpeg();
                FileUtils.saveBitmap(file, rotaBitmap);
            }
            //再次进入预览
            mCamera.startPreview();
            isPreview = true;
        }
    };

    private CameraSizeComparator sizeComparator = new CameraSizeComparator();

    private CameraHelper() {
    }

    private static class SingleTon {
        public static final CameraHelper INSTANCE = new CameraHelper();
    }

    public static CameraHelper getInstance() {
        return SingleTon.INSTANCE;
    }

    public void openCamera(SurfaceTexture surfaceTexture, int rotation,
                           int cameraType) {
        closeCamera();
        mCamera = Camera.open(mCameraID);
        if (isPreview) {
            mCamera.stopPreview();
            isPreview = false;
        }
        mParameters = mCamera.getParameters();
        printSupportPreviewSize(mParameters);
        printSupportPictureSize(mParameters);
        printSupportVideoSize(mParameters);
        printSupportFocusMode(mParameters);
        printSupportWhiteBalance(mParameters);
        switch (cameraType) {
            case PHOTO_CAMERA:
                setUpPhotoCamera(surfaceTexture, rotation);
                break;
            case VIDEO_CAMERA:
                setUpVideoCamera(surfaceTexture, rotation);
                break;
            default:
                setUpPhotoCamera(surfaceTexture, rotation);
                break;
        }

        mRotation = rotation;
    }

    private void setUpPhotoCamera(SurfaceTexture surfaceTexture, int rotation) {
        mParameters.setPictureFormat(PixelFormat.JPEG);
//        Camera.Size pictureSize = Collections.max(mParameters.getSupportedPictureSizes(), new CameraSizeComparator());
        Camera.Size pictureSize = mParameters.getSupportedPictureSizes().get(0);
        mParameters.setPictureSize(pictureSize.width, pictureSize.height);
        Log.i(TAG, "choose picture size width: " + pictureSize.width + ", height: " + pictureSize.height);
        Camera.Size previewSize = Collections.max(mParameters.getSupportedPreviewSizes(), new CameraSizeComparator());
        mParameters.setPreviewSize(previewSize.width, previewSize.height);
        Log.i(TAG, "choose preview size width: " + previewSize.width + ", height: " + previewSize.height);
        if (!isFrontCamera()) {
            setUpFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCamera.setDisplayOrientation(ORIENTATIONS.get(rotation));
        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.setParameters(mParameters);
        mCamera.startPreview();
        isPreview = true;
        mParameters = mCamera.getParameters();
        mCamera.cancelAutoFocus();
    }

    private void setUpVideoCamera(SurfaceTexture surfaceTexture, int rotation) {
        mVideoSize = Collections.max(mParameters.getSupportedVideoSizes(), new CompareSizesByArea());
        Log.i(TAG, "choose video size width: " + mVideoSize.width + ", height: " + mVideoSize.height);
        if (!isFrontCamera()) {
            setUpFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        mCamera.setDisplayOrientation(ORIENTATIONS.get(rotation));
        mParameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.setParameters(mParameters);
        mCamera.startPreview();
        isPreview = true;
        mParameters = mCamera.getParameters();
    }

    private void setUpFocusMode(String focusMode) {
        List<String> focusModes = mParameters.getSupportedFocusModes();
        if (focusModes.contains("focusMode")) {
            mParameters.setFocusMode(focusMode);
        } else {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
    }


    public void setAutoFocus(Camera.AutoFocusCallback callback) {
        mCamera.autoFocus(callback);
    }

    public void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreview = false;
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * stop preview
     * set find video size
     * set up some configure
     * create MediaRecorderWrapper
     * recording video
     */
    public void startRecordingVideo(SurfaceTexture surfaceTexture) {
        Surface surface = new Surface(surfaceTexture);
        mCamera.unlock();
        final MediaRecorderWrapper mediaRecorderWrapper = MediaRecorderWrapper.setUpMediaRecord(mCamera,
                mVideoSize.width, mVideoSize.height, 30, (int) (2.1 * 1024 * 1024 * 8),
                ORIENTATIONS.get(mRotation), surface);
        mediaRecorderWrapper.startRecordingVideo(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "abs.mp4");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaRecorderWrapper.stopRecordingVideo();
                mCamera.startPreview();
            }
        }, 10 * 1000);
    }

    public void chooseVideoMode(SurfaceTexture surfaceTexture,
                                int rotation) {
        openCamera(surfaceTexture, rotation, VIDEO_CAMERA);
    }



    public void takePicture(int rotation) {
        mRotation = rotation;
        mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
    }

    private void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            Log.i(TAG, "previewSizes:width = " + size.width + " height = " + size.height);
        }
    }

    private void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            Log.i(TAG, "pictureSizes:width = " + size.width
                    + " height = " + size.height);
        }
    }

    private void printSupportVideoSize(Camera.Parameters params) {
        List<Camera.Size> videoSize = params.getSupportedVideoSizes();
        for (int i = 0; i < videoSize.size(); i++) {
            Camera.Size size = videoSize.get(i);
            Log.i(TAG, "videoSize:width = " + size.width + " height = " + size.height);
        }
    }

    private void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i(TAG, "focusModes--" + mode);
        }
    }

    private void printSupportWhiteBalance(Camera.Parameters params) {
        List<String> whiteBalances = params.getSupportedWhiteBalance();
        for (String whiteBalance : whiteBalances) {
            Log.i(TAG, "whiteBalance--" + whiteBalance);
        }
    }

    private boolean equalRate(Camera.Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.03) {
            return true;
        } else {
            return false;
        }
    }

    public Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.width >= minWidth) && equalRate(s, th)){
                Log.i(TAG, "PreviewSize:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }

    private Camera.Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth) {
        Collections.sort(list, sizeComparator);
        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width >= minWidth) && equalRate(s, th)) {
                Log.i(TAG, "PictureSize : w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = 0;//如果没找到，就选最小的size
        }
        return list.get(i);
    }


    public class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }


    static class CompareSizesByArea implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.width * lhs.height -
                    (long) rhs.width * rhs.height);
        }

    }

    public void setWhiteBalance(String whiteBalance) {
        mParameters.setWhiteBalance(whiteBalance);
        mCamera.setParameters(mParameters);
        mParameters = mCamera.getParameters();
    }

    public void setFlashMode(String flashMode) {
        if (isFrontCamera()) {
            return;
        }
        mParameters.setFlashMode(flashMode);
        mCamera.setParameters(mParameters);
        mParameters = mCamera.getParameters();
    }

    public void switchCamera(SurfaceTexture surfaceTexture, int rotation) {
        mCameraID++;
        mCameraID %= 2;
        openCamera(surfaceTexture, rotation, PHOTO_CAMERA);
    }

    public boolean isFrontCamera() {
        return mCameraID == FRONT_CAMERA;
    }
}
