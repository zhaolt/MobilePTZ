package com.ziguang.ptz.ui.fast_setting;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.core.camera.CameraHelper;

/**
 * Created by zhaoliangtai on 2017/9/24.
 */

public class FlashSelectFragment extends Fragment {

    private FastSettingRadioItemView mFlashModeAuto, mFlashModeOn, mFlashModeOff, mFlashModeTorch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flash_select, null);
        initView(view);
        initFlashModeStatus();
        return view;
    }

    private void initView(View view) {
        mFlashModeAuto = (FastSettingRadioItemView) view.findViewById(R.id.flash_mode_auto);
        mFlashModeOn = (FastSettingRadioItemView) view.findViewById(R.id.flash_mode_on);
        mFlashModeOff = (FastSettingRadioItemView) view.findViewById(R.id.flash_mode_off);
        mFlashModeTorch = (FastSettingRadioItemView) view.findViewById(R.id.flash_mode_torch);
        mFlashModeAuto.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                chooseFlashModeAuto();
            }
        });
        mFlashModeOn.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                chooseFlashModeOn();
            }
        });
        mFlashModeOff.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                chooseFlashModeOff();
            }
        });
        mFlashModeTorch.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                chooseFlashModeTorch();
            }
        });
    }

    private void initFlashModeStatus() {
        String flashMode = CameraHelper.getInstance().getChooseFlashMode();
        switch (flashMode) {
            case Camera.Parameters.FLASH_MODE_AUTO:
                chooseFlashModeAuto();
                break;
            case Camera.Parameters.FLASH_MODE_ON:
                chooseFlashModeOn();
                break;
            case Camera.Parameters.FLASH_MODE_OFF:
                chooseFlashModeOff();
                break;
            case Camera.Parameters.FLASH_MODE_TORCH:
                chooseFlashModeTorch();
                break;
            default:
                break;
        }
    }

    private void chooseFlashModeAuto() {
        mFlashModeAuto.selectedItem(true);
        mFlashModeOn.selectedItem(false);
        mFlashModeOff.selectedItem(false);
        mFlashModeTorch.selectedItem(false);
    }

    private void chooseFlashModeOn() {
        mFlashModeAuto.selectedItem(false);
        mFlashModeOn.selectedItem(true);
        mFlashModeOff.selectedItem(false);
        mFlashModeAuto.selectedItem(false);
    }

    private void chooseFlashModeOff() {
        mFlashModeAuto.selectedItem(false);
        mFlashModeOn.selectedItem(false);
        mFlashModeOff.selectedItem(true);
        mFlashModeTorch.selectedItem(false);
    }

    private void chooseFlashModeTorch() {
        mFlashModeAuto.selectedItem(false);
        mFlashModeOn.selectedItem(false);
        mFlashModeOff.selectedItem(false);
        mFlashModeTorch.selectedItem(true);
    }
}
