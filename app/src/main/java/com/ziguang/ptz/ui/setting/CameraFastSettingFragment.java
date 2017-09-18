package com.ziguang.ptz.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;

/**
 * Created by zhaoliangtai on 2017/9/18.
 */

public class CameraFastSettingFragment extends Fragment {



    private FastSettingArrowsItemView mItemVideoSize, mItemBeaut, mItemWhiteBlance,
            mItemFlash, mItemGrid;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_fast_setting, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mItemVideoSize = (FastSettingArrowsItemView) view.findViewById(R.id.item_video_size);
        mItemBeaut = (FastSettingArrowsItemView) view.findViewById(R.id.item_beauty);
        mItemWhiteBlance = (FastSettingArrowsItemView) view.findViewById(R.id.item_white_blance);
        mItemFlash = (FastSettingArrowsItemView) view.findViewById(R.id.item_flash);
        mItemGrid = (FastSettingArrowsItemView) view.findViewById(R.id.item_grid);
    }

    private void initData() {
    }
}
