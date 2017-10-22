package com.ziguang.ptz.ui.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;

/**
 * Created by zhaoliangtai on 2017/10/21.
 */

public class SettingFragment extends Fragment {

    private IconWithArrowsItemView mCameraSettingItem;
    private IconWithArrowsItemView mPtzSettingItem;
    private IconWithArrowsItemView mCommonSettingItem;

    public static SettingFragment createInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, null);
        mCameraSettingItem = (IconWithArrowsItemView) root.findViewById(R.id.camera_setting);
        mPtzSettingItem = (IconWithArrowsItemView) root.findViewById(R.id.ptz_setting);
        mCommonSettingItem = (IconWithArrowsItemView) root.findViewById(R.id.common_setting);
        mCameraSettingItem.setItemClickListener(new IconWithArrowsItemView.OnItemClickListener() {
            @Override
            public void onItemClick() {

            }
        });
        mPtzSettingItem.setItemClickListener(new IconWithArrowsItemView.OnItemClickListener() {
            @Override
            public void onItemClick() {

            }
        });
        mCommonSettingItem.setItemClickListener(new IconWithArrowsItemView.OnItemClickListener() {
            @Override
            public void onItemClick() {

            }
        });
        return root;
    }




}
