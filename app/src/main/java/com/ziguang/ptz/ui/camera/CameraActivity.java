package com.ziguang.ptz.ui.camera;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.ui.album.AlbumActivity;
import com.ziguang.ptz.ui.setting.CameraFastSettingFragment;
import com.ziguang.ptz.ui.setting.FlashSelectFragment;
import com.ziguang.ptz.ui.setting.GridSelectFragment;
import com.ziguang.ptz.ui.setting.VideoResolutionSelectFragment;
import com.ziguang.ptz.ui.setting.WhiteBalanceFragment;
import com.ziguang.ptz.utils.ActivityUtils;
import com.ziguang.ptz.utils.PermissionUtils;
import com.ziguang.ptz.utils.SharedPrefUtils;
import com.ziguang.ptz.utils.UIUtils;
import com.ziguang.ptz.widget.Grid;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class CameraActivity extends FullScreenActivity implements View.OnClickListener {

    private static final String CAMERA_LENS_TAG = "cameraLens";

    private static final String TAG = CameraActivity.class.getSimpleName();

    private ImageButton mCameraSetting;

    private View mRightMenuBg;

    private LinearLayout mFastSettingMenuLayout;

    private TextView mFastSettingMenuTitle;

    private boolean isCameraFastMenuShow;

    private ImageButton mTakeModeBtn;

    private ImageButton mShutterBtn;

    private ImageButton mCameraSwitchBtn;

    private ImageButton mAlbumBtn;

    private PreviewDisplayFragment mPreviewDisplayFragment;

    private ImageView mCameraSettingMenuBack;

    private Grid mGrid;

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
        mPreviewDisplayFragment = (PreviewDisplayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_fragment);
        if (mPreviewDisplayFragment == null) {
            mPreviewDisplayFragment = new PreviewDisplayFragment();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mPreviewDisplayFragment,
                R.id.layout_fragment, CAMERA_LENS_TAG);
        mCameraSetting = (ImageButton) findViewById(R.id.iv_camera_setting);
        mRightMenuBg = findViewById(R.id.right_menu_bg);
        mFastSettingMenuLayout = (LinearLayout) findViewById(R.id.menu_fast_setting);
        mFastSettingMenuTitle = (TextView) findViewById(R.id.tv_fast_setting_title);
        mTakeModeBtn = (ImageButton) findViewById(R.id.btn_take_mode);
        mShutterBtn = (ImageButton) findViewById(R.id.btn_shutter);
        mCameraSwitchBtn = (ImageButton) findViewById(R.id.btn_camera_switch);
        mAlbumBtn = (ImageButton) findViewById(R.id.btn_album);
        mCameraSettingMenuBack = (ImageView) findViewById(R.id.iv_back);
        mGrid = (Grid) findViewById(R.id.grid);
        updateGrid((String) SharedPrefUtils.getParam(GridSelectFragment.KEY_GRID,
                GridSelectFragment.VALUE_GRID_NONE));
        mCameraSettingMenuBack.setOnClickListener(this);
        mCameraSetting.setOnClickListener(this);
        mTakeModeBtn.setOnClickListener(this);
        mShutterBtn.setOnClickListener(this);
        mCameraSwitchBtn.setOnClickListener(this);
        mAlbumBtn.setOnClickListener(this);
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

    public void updateGrid(String value) {
        switch (value) {
            case GridSelectFragment.VALUE_GRID_NONE:
                mGrid.setVisibility(View.GONE);
                break;
            case GridSelectFragment.VALUE_GRID_LINES:
                mGrid.setVisibility(View.VISIBLE);
                mGrid.showWithDiagonal(Grid.MODE_LINES);
                break;
            case GridSelectFragment.VALUE_GRID_DIAGONAL:
                mGrid.setVisibility(View.VISIBLE);
                mGrid.showWithDiagonal(Grid.MODE_WITH_DIAGONAL);
                break;
            case GridSelectFragment.VALUE_GRID_CENTER_POINT:
                mGrid.setVisibility(View.VISIBLE);
                mGrid.showWithDiagonal(Grid.MODE_CENTER_POINT);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (v.getId()) {
            case R.id.iv_camera_setting:
                mCameraSetting.setSelected(!mCameraSetting.isSelected());
                showCameraFastSettingMenu(mCameraSetting.isSelected());
                break;
            case R.id.btn_shutter:
                CameraHelper.getInstance().takePicture(rotation);
                break;
            case R.id.btn_take_mode:
                break;
            case R.id.btn_camera_switch:
                CameraHelper.getInstance().switchCamera(mPreviewDisplayFragment.getSurfaceTexture(),
                        rotation);
                break;
            case R.id.btn_album:
                Intent intent = new Intent(this, AlbumActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                backToFastSettingFragment(getFragmentManager());
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
            showCameraFastSettingTitleBar();
            mFastSettingMenuLayout.setVisibility(View.GONE);
            removeFragmentByTag(getFragmentManager(),
                    CameraFastSettingFragment.class.toString());
            isCameraFastMenuShow = false;
        }
    }

    public void showWhiteBalanceTitleBar() {
        mCameraSettingMenuBack.setVisibility(View.VISIBLE);
        mFastSettingMenuTitle.setText(R.string.white_balance);
    }

    public void showVideoResolutionTitleBar() {
        mCameraSettingMenuBack.setVisibility(View.VISIBLE);
        mFastSettingMenuTitle.setText(R.string.video_resolution);
    }

    public void showFlashModeTitleBar() {
        mCameraSettingMenuBack.setVisibility(View.VISIBLE);
        mFastSettingMenuTitle.setText(R.string.camera_flash);
    }

    public void showGridModeTitleBar() {
        mCameraSettingMenuBack.setVisibility(View.VISIBLE);
        mFastSettingMenuTitle.setText(R.string.camera_grid);
    }

    private void showCameraFastSettingTitleBar() {
        mCameraSettingMenuBack.setVisibility(View.GONE);
        mFastSettingMenuTitle.setText(R.string.permission_camera);
    }

    private void changeToCameraFastSettingFragment(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        changeFragment(ft, new CameraFastSettingFragment());
    }

    public void changeToWhiteBalanceFragment(FragmentManager fm) {
        showWhiteBalanceTitleBar();
        changeToFragmentWithAnim(fm, new WhiteBalanceFragment());
    }

    public void changeToVideoResolutionFragment(FragmentManager fm) {
        showVideoResolutionTitleBar();
        changeToFragmentWithAnim(fm, new VideoResolutionSelectFragment());
    }

    public void changeToGridFragment(FragmentManager fm) {
        showGridModeTitleBar();
        changeToFragmentWithAnim(fm, new GridSelectFragment());
    }

    public void changeToFlashModeFragment(FragmentManager fm) {
        showFlashModeTitleBar();
        changeToFragmentWithAnim(fm, new FlashSelectFragment());
    }

    private void changeToFragmentWithAnim(FragmentManager fm, Fragment f) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        changeFragment(ft, f);
    }


    private void changeFragment(FragmentTransaction ft, Fragment f) {
        ft.replace(R.id.frame_fast_setting, f, f.getClass().toString());
        ft.commit();
    }

    private void backToFastSettingFragment(FragmentManager fm) {
        showCameraFastSettingTitleBar();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        changeFragment(ft, new CameraFastSettingFragment());
    }

    private void removeFragmentByTag(FragmentManager fm, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) return;
        ft.remove(fragment);
    }
}
