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
 * Created by zhaoliangtai on 2017/9/19.
 */

public class WhiteBalanceFragment extends Fragment {

    private FastSettingRadioItemView mWhiteBalanceAuto, mWhiteBalanceIncandescent,
            mWhiteBalanceFluorescent, mWhiteBalanceDaylight, mWhiteBalanceCloudyDaylight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whitebalance, null);
        initView(view);
        initWhiteBalanceStatus();
        return view;
    }

    private void initView(View view) {
        mWhiteBalanceAuto = (FastSettingRadioItemView) view.findViewById(R.id.white_balance_auto);
        mWhiteBalanceIncandescent = (FastSettingRadioItemView) view.findViewById(R.id.white_balance_incandescent);
        mWhiteBalanceFluorescent = (FastSettingRadioItemView) view.findViewById(R.id.white_balance_fluorescent);
        mWhiteBalanceDaylight = (FastSettingRadioItemView) view.findViewById(R.id.white_balance_daylight);
        mWhiteBalanceCloudyDaylight = (FastSettingRadioItemView) view.findViewById(R.id.white_balance_cloudy_daylight);
        mWhiteBalanceAuto.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                chooseWhiteBalanceAuto();
            }
        });
        mWhiteBalanceIncandescent.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_INCANDESCENT);
                chooseWhiteBalanceIncandescent();
            }
        });
        mWhiteBalanceFluorescent.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
                chooseWhiteBalanceFluorescent();
            }
        });
        mWhiteBalanceDaylight.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);
                chooseWhiteBalanceDaylight();
            }
        });
        mWhiteBalanceCloudyDaylight.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                CameraHelper.getInstance().setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
                chooseWhiteBalanceCloudyDaylight();
            }
        });
    }

    private void initWhiteBalanceStatus() {
        String whiteBalance = CameraHelper.getInstance().getChooseWhiteBalance();
        switch (whiteBalance) {
            case Camera.Parameters.WHITE_BALANCE_AUTO:
                chooseWhiteBalanceAuto();
                break;
            case Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT:
                chooseWhiteBalanceCloudyDaylight();
                break;
            case Camera.Parameters.WHITE_BALANCE_DAYLIGHT:
                chooseWhiteBalanceDaylight();
                break;
            case Camera.Parameters.WHITE_BALANCE_FLUORESCENT:
                chooseWhiteBalanceFluorescent();
                break;
            case Camera.Parameters.WHITE_BALANCE_INCANDESCENT:
                chooseWhiteBalanceIncandescent();
                break;
            default:
                chooseWhiteBalanceAuto();
                break;
        }
    }

    private void chooseWhiteBalanceCloudyDaylight() {
        mWhiteBalanceAuto.selectedItem(false);
        mWhiteBalanceIncandescent.selectedItem(false);
        mWhiteBalanceFluorescent.selectedItem(false);
        mWhiteBalanceDaylight.selectedItem(false);
        mWhiteBalanceCloudyDaylight.selectedItem(true);
    }

    private void chooseWhiteBalanceDaylight() {
        mWhiteBalanceAuto.selectedItem(false);
        mWhiteBalanceIncandescent.selectedItem(false);
        mWhiteBalanceFluorescent.selectedItem(false);
        mWhiteBalanceDaylight.selectedItem(true);
        mWhiteBalanceCloudyDaylight.selectedItem(false);
    }

    private void chooseWhiteBalanceFluorescent() {
        mWhiteBalanceAuto.selectedItem(false);
        mWhiteBalanceIncandescent.selectedItem(false);
        mWhiteBalanceFluorescent.selectedItem(true);
        mWhiteBalanceDaylight.selectedItem(false);
        mWhiteBalanceCloudyDaylight.selectedItem(false);
    }

    private void chooseWhiteBalanceIncandescent() {
        mWhiteBalanceAuto.selectedItem(false);
        mWhiteBalanceIncandescent.selectedItem(true);
        mWhiteBalanceFluorescent.selectedItem(false);
        mWhiteBalanceDaylight.selectedItem(false);
        mWhiteBalanceCloudyDaylight.selectedItem(false);
    }

    private void chooseWhiteBalanceAuto() {
        mWhiteBalanceAuto.selectedItem(true);
        mWhiteBalanceIncandescent.selectedItem(false);
        mWhiteBalanceFluorescent.selectedItem(false);
        mWhiteBalanceDaylight.selectedItem(false);
        mWhiteBalanceCloudyDaylight.selectedItem(false);
    }


}
