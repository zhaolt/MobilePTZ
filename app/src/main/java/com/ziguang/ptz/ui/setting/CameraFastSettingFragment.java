package com.ziguang.ptz.ui.setting;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.core.camera.CameraHelper;
import com.ziguang.ptz.ui.camera.CameraActivity;

import java.lang.ref.WeakReference;

/**
 * Created by zhaoliangtai on 2017/9/18.
 */

public class CameraFastSettingFragment extends Fragment {


    private WeakReference<CameraActivity> mActivityWeakReference;

    private FastSettingArrowsItemView mItemVideoSize, mItemBeaut, mItemWhiteBalance,
            mItemFlash, mItemGrid;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_fast_setting, null);
        initView(view);
        initData();
        mActivityWeakReference = new WeakReference<>((CameraActivity) getActivity());
        return view;
    }

    private void initView(View view) {
        mItemVideoSize = (FastSettingArrowsItemView) view.findViewById(R.id.item_video_size);
        mItemBeaut = (FastSettingArrowsItemView) view.findViewById(R.id.item_beauty);
        mItemWhiteBalance = (FastSettingArrowsItemView) view.findViewById(R.id.item_white_blance);
        mItemFlash = (FastSettingArrowsItemView) view.findViewById(R.id.item_flash);
        mItemGrid = (FastSettingArrowsItemView) view.findViewById(R.id.item_grid);
        mItemVideoSize.setOnItemClickListener(new FastSettingArrowsItemView.OnItemClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mItemBeaut.setOnItemClickListener(new FastSettingArrowsItemView.OnItemClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mItemWhiteBalance.setOnItemClickListener(new FastSettingArrowsItemView.OnItemClickListener() {
            @Override
            public void onClick(View v) {
                mActivityWeakReference.get().changeToFragmentWithAnim(getFragmentManager(),
                        new WhiteBalanceFragment());
            }
        });
        mItemFlash.setOnItemClickListener(new FastSettingArrowsItemView.OnItemClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mItemGrid.setOnItemClickListener(new FastSettingArrowsItemView.OnItemClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        initWhiteBalanceStatus();
        initFlashModeStatus();
    }

    private void initFlashModeStatus() {
        String flashMode = CameraHelper.getInstance().getChooseFlashMode();
        switch (flashMode) {
            case Camera.Parameters.FLASH_MODE_AUTO:
                mItemFlash.setRightIcon(getResources().getDrawable(R.mipmap.flash_mode_auto));
                break;
            case Camera.Parameters.FLASH_MODE_ON:
                mItemFlash.setRightIcon(getResources().getDrawable(R.mipmap.flash_mode_on));
                break;
            case Camera.Parameters.FLASH_MODE_OFF:
                mItemFlash.setRightIcon(getResources().getDrawable(R.mipmap.flash_mode_off));
                break;
            case Camera.Parameters.FLASH_MODE_TORCH:
                mItemFlash.setRightIcon(getResources().getDrawable(R.mipmap.flash_mode_torch));
                break;
            default:
                mItemFlash.setRightIcon(getResources().getDrawable(R.mipmap.flash_mode_off));
                break;
        }
    }

    private void initWhiteBalanceStatus() {
        String whiteBalance = CameraHelper.getInstance().getChooseWhiteBalance();
        switch (whiteBalance) {
            case Camera.Parameters.WHITE_BALANCE_AUTO:
                mItemWhiteBalance.setRightIcon(getResources()
                        .getDrawable(R.mipmap.white_balance_auto));
                break;
            case Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT:
                mItemWhiteBalance.setRightIcon(getResources()
                        .getDrawable(R.mipmap.white_balance_cloudy_daylight));
                break;
            case Camera.Parameters.WHITE_BALANCE_DAYLIGHT:
                mItemWhiteBalance.setRightIcon(getResources()
                        .getDrawable(R.mipmap.white_balance_daylight));
                break;
            case Camera.Parameters.WHITE_BALANCE_FLUORESCENT:
                mItemWhiteBalance.setRightIcon(getResources()
                        .getDrawable(R.mipmap.white_balance_fluorescent));
                break;
            case Camera.Parameters.WHITE_BALANCE_INCANDESCENT:
                mItemWhiteBalance.setRightIcon(getResources()
                        .getDrawable(R.mipmap.white_balance_incandescent));
                break;
            default:
                mItemWhiteBalance.setRightIcon(getResources()
                        .getDrawable(R.mipmap.white_balance_auto));
                break;
        }
    }

}
