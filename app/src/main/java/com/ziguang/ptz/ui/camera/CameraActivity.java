package com.ziguang.ptz.ui.camera;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.utils.ActivityUtils;
import com.ziguang.ptz.utils.PermissionUtils;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class CameraActivity extends FullScreenActivity {

    private static final String CAMERA_LENS_TAG = "cameraLens";


    private static final String TAG = CameraActivity.class.getSimpleName();

    private ImageButton mCameraSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        requestPermissions();
    }


    private void requestPermissions() {
        getPermissions(new PermissionUtils.PermissionCallback() {
            @Override
            public void onPermissionsGranted() {
                loadUI();
            }

            @Override
            public void onPermissionsDeniedForever(String[] permissions) {
                permissionDeniedForeverCallback(permissions);
            }
        });
    }

    private void loadUI() {
        PreviewDisplayFragment fragment = (PreviewDisplayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_fragment);
        if (fragment == null) {
            fragment = new PreviewDisplayFragment();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment,
                R.id.layout_fragment, CAMERA_LENS_TAG);
        mCameraSetting = (ImageButton) findViewById(R.id.iv_camera_setting);
        final PreviewDisplayFragment finalFragment = fragment;
        mCameraSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CameraHelper.getInstance().takePicture(getWindowManager().getDefaultDisplay().getRotation());
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
//                CameraHelper.getInstance().startRecordingVideo(finalFragment.getSurfaceTexture(), rotation);
                CameraHelper.getInstance().chooseVideoMode(finalFragment.getSurfaceTexture(), rotation);
            }
        });

        RadioGroup whiteBalance = (RadioGroup) findViewById(R.id.white_balance);
        whiteBalance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.white_balance_auto:
                        CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                        break;
                    case R.id.white_balance_daylight:
                        CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);
                        break;
                    case R.id.white_balance_fluorescent:
                        CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
                        break;
                    case R.id.white_balance_incandescent:
                        CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_INCANDESCENT);
                        break;
                    case R.id.white_balance_cloudy_daylight:
                        CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
                        break;
                    default:
                        CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                        break;
                }
            }
        });
        RadioGroup flashGroup = (RadioGroup) findViewById(R.id.flash_group);
        flashGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.flash_auto:
                        CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        break;
                    case R.id.flash_off:
                        CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        break;
                    case R.id.flash_on:
                        CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                        break;
                    case R.id.flash_all_on:
                        CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        break;
                    default:
                        CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        break;
                }
            }
        });

        CheckBox frontCamera = (CheckBox) findViewById(R.id.checkbox);
        frontCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                CameraHelper.getInstance().switchCamera(finalFragment.getSurfaceTexture(), rotation);
            }
        });
    }

    public void permissionDeniedForeverCallback(String[] permissions) {
        if (permissions != null && permissions.length > 0) {
            StringBuilder permissionNames = new StringBuilder();
            StringBuilder permissionValues = new StringBuilder();
            for (String permission : permissions) {
                switch (permission) {
                    case Manifest.permission.READ_EXTERNAL_STORAGE:
                        permissionNames.append(getString(R.string.permission_storage));
                        permissionNames.append("、");
                        permissionValues.append(getString(R.string.permission_sdcard));
                        permissionValues.append("、");
                        break;
                    case Manifest.permission.CAMERA:
                        permissionNames.append(getString(R.string.permission_camera_lens));
                        permissionNames.append("、");
                        permissionValues.append(getString(R.string.permission_camera));
                        permissionValues.append("、");
                        break;
                    case Manifest.permission.RECORD_AUDIO:
                        permissionNames.append(getString(R.string.permission_recording_audio));
                        permissionNames.append("、");
                        permissionValues.append(getString(R.string.permission_recording_mic));
                        permissionValues.append("、");
                        break;
                }
            }
            Log.i(TAG, permissionNames.toString());
            Log.i(TAG, permissionValues.toString());
            permissionNames.deleteCharAt(permissionNames.length() - 1);
            permissionValues.deleteCharAt(permissionValues.length() - 1);
            showTipsDialog(permissionNames.toString(), permissionValues.toString());
        }
    }

}
