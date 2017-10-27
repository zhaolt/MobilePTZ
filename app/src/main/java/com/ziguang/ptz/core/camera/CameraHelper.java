package com.ziguang.ptz.core.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.SparseIntArray;
import android.view.Surface;

import com.ziguang.ptz.App;
import com.ziguang.ptz.core.media.MediaRecorderWrapper;
import com.ziguang.ptz.rx.NCSubscriber;
import com.ziguang.ptz.utils.FileUtils;
import com.ziguang.ptz.utils.ImageUtils;
import com.ziguang.ptz.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhaoliangtai on 2017/8/23.
 */

public class CameraHelper {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    public static final int PHOTO_CAMERA = 1001;

    public static final int VIDEO_CAMERA = 1007;

    private static final int REAR_CAMERA = 0;

    private static final int FRONT_CAMERA = 1;

    public static final int M_BIT_UNIT = 1024 * 1024 * 8;

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

    private boolean isSupportedAutoFocus = true;

    private int mCameraMode = PHOTO_CAMERA;

    private MediaRecorderWrapper mMediaRecorderWrapper;

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
                isPreview = false;
            }
            //保存图片到sdcard
            if (null != b) {
                final Bitmap finalB = b;
                if (FileUtils.checkJpegDir()) {
                    saveBitmap(finalB);
                } else {
                    App.INSTANCE.createDirs()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new NCSubscriber() {
                                @Override
                                public void doOnNext(Object o) {
                                    saveBitmap(finalB);
                                }

                                @Override
                                public void doOnError(Throwable e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }
            //再次进入预览
            mCamera.startPreview();
            isPreview = true;
        }
    };

    private void saveBitmap(final Bitmap finalB) {
        Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                Bitmap rotaBitmap = ImageUtils.getRotateBitmap(finalB, ORIENTATIONS.get(mRotation));
                File file = FileUtils.createJpeg();
                if (FileUtils.saveBitmap(file, rotaBitmap)) {
                    subscriber.onNext(file);
                } else {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NCSubscriber<File>() {

                    @Override
                    public void doOnNext(File file) {
                        if (null != file) {
                            ImageUtils.addToMediaStore(App.INSTANCE, file);
                        }
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


    private CameraHelper() {
    }

    private static class SingleTon {
        public static final CameraHelper INSTANCE = new CameraHelper();
    }

    public static CameraHelper getInstance() {
        return SingleTon.INSTANCE;
    }

    public Observable<Void> openCamera(final SurfaceTexture surfaceTexture, final int rotation,
                                       final int cameraType) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
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
                printSupportSceneModes(mParameters);
                printSupportAntibanding(mParameters);
                isSupportedAutoFocus = mParameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO);
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
                subscriber.onNext(null);
            }
        }).subscribeOn(Schedulers.io());

    }

    private void setUpPhotoCamera(SurfaceTexture surfaceTexture, int rotation) {
        mParameters.setPictureFormat(PixelFormat.JPEG);
//        Camera.Size pictureSize = Collections.max(mParameters.getSupportedPictureSizes(), new CameraSizeComparator());
        Camera.Size pictureSize = mParameters.getSupportedPictureSizes().get(0);
        mParameters.setPictureSize(pictureSize.width, pictureSize.height);
        LogUtils.i(TAG, "choose picture size width: " + pictureSize.width + ", height: " + pictureSize.height);
        Camera.Size previewSize = Collections.max(mParameters.getSupportedPreviewSizes(), new CameraSizeComparator());
        mParameters.setPreviewSize(previewSize.width, previewSize.height);
        LogUtils.i(TAG, "choose preview size width: " + previewSize.width + ", height: " + previewSize.height);
        if (!isFrontCamera()) {
            setUpFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            setUpSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
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
        mCameraMode = PHOTO_CAMERA;
    }

    private void setUpVideoCamera(SurfaceTexture surfaceTexture, int rotation) {
        mVideoSize = Collections.max(mParameters.getSupportedVideoSizes(), new CompareSizesByArea());
        LogUtils.i(TAG, "choose video size width: " + mVideoSize.width + ", height: " + mVideoSize.height);
        if (!isFrontCamera()) {
            setUpFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            setUpSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        mCamera.setDisplayOrientation(ORIENTATIONS.get(rotation));
        mParameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        applyParameters();
        mCamera.startPreview();
        isPreview = true;
        mCameraMode = VIDEO_CAMERA;
    }

    public int getCameraMode() {
        return mCameraMode;
    }

    public void applyParameters() {
        if (null == mCamera) return;
        mCamera.setParameters(mParameters);
        mParameters = mCamera.getParameters();
    }

    public void setUpFocusMode(String focusMode) {
        List<String> focusModes = mParameters.getSupportedFocusModes();
        if (focusModes.contains(focusMode)) {
            mParameters.setFocusMode(focusMode);
        } else {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
    }

    public void setUpSceneMode(String sceneMode) {
        List<String> sceneModes = mParameters.getSupportedSceneModes();
        if (sceneModes.contains(sceneMode)) {
            mParameters.setSceneMode(sceneMode);
        }
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
    public String startRecordingVideo(SurfaceTexture surfaceTexture) {
        Surface surface = new Surface(surfaceTexture);
        mCamera.unlock();
        mMediaRecorderWrapper = MediaRecorderWrapper.setUpMediaRecord(mCamera,
                mVideoSize.width, mVideoSize.height, 30, 2 * M_BIT_UNIT,
                ORIENTATIONS.get(mRotation), surface);
        String filePath = FileUtils.createMP4().getAbsolutePath();
        mMediaRecorderWrapper.startRecordingVideo(filePath);
        return filePath;
    }

    public int getVolume() {
        if (null != mMediaRecorderWrapper) {
            return mMediaRecorderWrapper.getVolumeLevel();
        }
        return 0;
    }

    public void stopRecordingVideo(String filePath) {
        if (null != mMediaRecorderWrapper) {
            mMediaRecorderWrapper.stopRecordingVideo();
        }
        mCamera.startPreview();
        mMediaRecorderWrapper = null;
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        ImageUtils.addToMediaStore(App.INSTANCE, file);
    }

    public Observable<Void> chooseVideoMode(SurfaceTexture surfaceTexture,
                                            int rotation) {
        return openCamera(surfaceTexture, rotation, VIDEO_CAMERA);
    }

    public Observable<Void> choosePhotoMode(SurfaceTexture surfaceTexture, int rotation) {
        return openCamera(surfaceTexture, rotation, PHOTO_CAMERA);
    }


    public void takePicture(int rotation) {
        mRotation = rotation;
        mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
    }

    private void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            LogUtils.i(TAG, "previewSizes:width = " + size.width + " height = " + size.height);
        }
    }

    private void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            LogUtils.i(TAG, "pictureSizes:width = " + size.width
                    + " height = " + size.height);
        }
    }

    private void printSupportVideoSize(Camera.Parameters params) {
        List<Camera.Size> videoSize = params.getSupportedVideoSizes();
        for (int i = 0; i < videoSize.size(); i++) {
            Camera.Size size = videoSize.get(i);
            LogUtils.i(TAG, "videoSize:width = " + size.width + " height = " + size.height);
        }
    }

    private void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            LogUtils.i(TAG, "focusModes--" + mode);
        }
    }

    private void printSupportWhiteBalance(Camera.Parameters params) {
        List<String> whiteBalances = params.getSupportedWhiteBalance();
        for (String whiteBalance : whiteBalances) {
            LogUtils.i(TAG, "whiteBalance--" + whiteBalance);
        }
    }

    private void printSupportSceneModes(Camera.Parameters params) {
        List<String> sceneModes = params.getSupportedSceneModes();
        for (String sceneMode : sceneModes) {
            LogUtils.i(TAG, "sceneMode--" + sceneMode);
        }
    }

    private void printSupportAntibanding(Camera.Parameters params) {
        List<String> antibandings = params.getSupportedAntibanding();
        for (String antibanding : antibandings) {
            LogUtils.i(TAG, "antibanding--" + antibanding);
        }
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
        applyParameters();
    }

    public void setFlashMode(String flashMode) {
        if (isFrontCamera()) {
            return;
        }
        mParameters.setFlashMode(flashMode);
        applyParameters();
    }

    public void setAntibanding(String antibanding) {
        List<String> antibandings = mParameters.getSupportedAntibanding();
        if (antibandings.contains(antibanding)) {
            mParameters.setAntibanding(antibanding);
        }
        applyParameters();
    }

    public void switchCamera(SurfaceTexture surfaceTexture, int rotation) {
        mCameraID++;
        mCameraID %= 2;
        openCamera(surfaceTexture, rotation, PHOTO_CAMERA).subscribe();
    }

    public boolean isFrontCamera() {
        return mCameraID == FRONT_CAMERA;
    }

    public void areaFocus(float x, float y, float coefficient, Camera.AutoFocusCallback cb) {
        if (!isSupportedAutoFocus) {
            return;
        }
        Rect rect = calculateTapArea(x, y, coefficient);
        focusOnRect(rect, cb);
    }

    private void focusOnRect(Rect rect, Camera.AutoFocusCallback cb) {
        setUpFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        if (mParameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusArea = new ArrayList<>();
            focusArea.add(new Camera.Area(rect, 300));
            mParameters.setFocusAreas(focusArea);
        } else {
            return;
        }
        mCamera.cancelAutoFocus();
        applyParameters();
        mCamera.autoFocus(cb);
    }


    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 200;
        int areaSize = Float.valueOf(focusAreaSize + coefficient).intValue();
        int centerX = (int) (x * 2000 - 1000);
        int centerY = (int) (y * 2000 - 1000);
        int left = clamp(centerX - (areaSize / 2), -1000, 1000 - areaSize);
        int top = clamp(centerY - (areaSize / 2), -1000, 1000 - areaSize);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top),
                Math.round(rectF.right), Math.round(rectF.bottom));
    }

    public String getChooseWhiteBalance() {
        return mParameters.getWhiteBalance();
    }

    public String getChooseFlashMode() {
        return mParameters.getFlashMode();
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }
}
