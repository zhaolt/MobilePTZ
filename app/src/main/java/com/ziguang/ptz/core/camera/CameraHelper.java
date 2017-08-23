package com.ziguang.ptz.core.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

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

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final String TAG = CameraHelper.class.getSimpleName();

    private Camera mCamera;

    private Camera.Parameters mParameters;

    private boolean isPreview = false;

    private int mRotation = -1;

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

    public void openCamera(float previewRate, SurfaceTexture surfaceTexture, int rotation) {
        mCamera = Camera.open();
        if (isPreview) {
            mCamera.stopPreview();
        }
        mParameters = mCamera.getParameters();
        mParameters.setPictureFormat(PixelFormat.JPEG);
        printSupportPreviewSize(mParameters);
        printSupportPictureSize(mParameters);
        printSupportFocusMode(mParameters);

        Camera.Size pictureSize = getPropPictureSize(mParameters.getSupportedPictureSizes(),
                previewRate, 800);
        mParameters.setPictureSize(pictureSize.width, pictureSize.height);
        Camera.Size previewSize = getPropPreviewSize(mParameters.getSupportedPreviewSizes(),
                previewRate, 800);
        mParameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setDisplayOrientation(ORIENTATIONS.get(rotation));
        mRotation = rotation;
        List<String> focusModes = mParameters.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        isPreview = true;
        mParameters = mCamera.getParameters();
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

    private void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i(TAG, "focusModes--" + mode);
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


}
