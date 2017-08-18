package com.ziguang.ptz.core.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Size;

import com.ziguang.ptz.App;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class CameraHelper {

    private static class SingleTon {
        public static final CameraHelper INSTANCE = new CameraHelper();
    }

    private CameraHelper() {}

    public static CameraHelper getInstance() {
        return SingleTon.INSTANCE;
    }

    public void openCamera(int width, int height) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        } else {
            // TODO: 2017/8/18 call Camera1 API
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpCameraOutputs(int width, int height) {
        CameraManager cameraManager = (CameraManager) App.INSTANCE.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    // 前置摄像头 跳过
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }
                // 照片最大尺寸
                Size largets = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
