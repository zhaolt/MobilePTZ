package com.ziguang.ptz.ui.camera;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.ui.setting.CameraFastSettingFragment;
import com.ziguang.ptz.utils.ActivityUtils;
import com.ziguang.ptz.utils.PermissionUtils;
import com.ziguang.ptz.utils.UIUtils;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class CameraActivity extends FullScreenActivity implements View.OnClickListener {

    private static final String CAMERA_LENS_TAG = "cameraLens";


    private static final String TAG = CameraActivity.class.getSimpleName();

    private ImageButton mCameraSetting;

    private View mRightMenuBg;

    private LinearLayout mFastSettingMenuLayout;

    private TextView mFastSettingMenuTtile;

    private boolean isCameraFastMenuShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        requestPermissions();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCameraFastMenuShow) {
            if (!checkMenuTouched(ev) || checkIconTouched(mCameraSetting, ev)) {
                showCameraFastSettingMenu(false);
                mCameraSetting.setSelected(false);
                return true;
            }
            return super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private boolean checkMenuTouched(MotionEvent ev) {
        float x = mFastSettingMenuLayout.getX() - ev.getX();
        return x <= 0;
    }

    private boolean checkIconTouched(View view, MotionEvent ev) {
        if (view == null) return false;
        return UIUtils.isPointInsideView(ev.getX(), ev.getY(), view);
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
        mCameraSetting.setOnClickListener(this);
        mRightMenuBg = findViewById(R.id.right_menu_bg);
        mFastSettingMenuLayout = (LinearLayout) findViewById(R.id.menu_fast_setting);
        mFastSettingMenuTtile = (TextView) findViewById(R.id.tv_fast_setting_title);
        final PreviewDisplayFragment finalFragment = fragment;
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

        RadioGroup antibandingGroup = (RadioGroup) findViewById(R.id.antibanding_group);
        antibandingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.antibanding_50hz:
                        CameraHelper.getInstance().setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
                        break;
                    case R.id.antibanding_auto:
                        CameraHelper.getInstance().setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
                        break;
                }
            }
        });

        ImageView shutter = (ImageView) findViewById(R.id.iv_shutter);
        shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper.getInstance().takePicture(getWindowManager()
                        .getDefaultDisplay().getRotation());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera_setting:
                mCameraSetting.setSelected(!mCameraSetting.isSelected());
                showCameraFastSettingMenu(mCameraSetting.isSelected());
                break;
        }
    }

    private void showCameraFastSettingMenu(boolean isOpen) {
        if (isOpen) {
            mRightMenuBg.setVisibility(View.VISIBLE);
            mFastSettingMenuLayout.setVisibility(View.VISIBLE);
            changeToCameraFastSettingFragment(getFragmentManager());
            isCameraFastMenuShow = true;
        } else {
            mRightMenuBg.setVisibility(View.GONE);
            mFastSettingMenuLayout.setVisibility(View.GONE);
            removeFragmentByTag(getFragmentManager(),
                    CameraFastSettingFragment.class.toString());
            isCameraFastMenuShow = false;
        }
    }

    private void changeToCameraFastSettingFragment(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        changeFragment(ft, new CameraFastSettingFragment());
    }

    public void changeToFragmentWithAnim(FragmentManager fm, Fragment f) {

        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        changeFragment(ft, f);
    }

    private void changeFragment(FragmentTransaction ft, Fragment f) {
        ft.replace(R.id.frame_fast_setting, f, f.getClass().toString());
        ft.commit();
    }

    private void removeFragmentByTag(FragmentManager fm, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) return;
        ft.remove(fragment);
    }
}
