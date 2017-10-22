package com.ziguang.ptz.ui.fast_setting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.utils.SharedPrefUtils;

/**
 * Created by zhaoliangtai on 2017/9/25.
 */

public class VideoResolutionSelectFragment extends Fragment {

    public static final String KEY_VIDEO_RESOLUTION = "key-video-resolution";
    public static final String VALUE_RESOLUTION_1080P = "value-resolution-1080p";
    public static final String VALUE_RESOLUTION_720P = "value-resolution-720p";
    public static final String VALUE_RESOLUTION_480P = "value-resolution-480p";

    private FastSettingRadioItemView mResolution1080, mResolution720, mResolution480;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_resolution_select, null);
        initView(view);
        initResolutionStatus();
        return view;
    }

    private void initView(View view) {
        mResolution1080 = (FastSettingRadioItemView) view.findViewById(R.id.video_resolution_1080);
        mResolution720 = (FastSettingRadioItemView) view.findViewById(R.id.video_resolution_720);
        mResolution480 = (FastSettingRadioItemView) view.findViewById(R.id.video_resolution_480);
        mResolution1080.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                chooseResolution1080();
                SharedPrefUtils.setParam(KEY_VIDEO_RESOLUTION, VALUE_RESOLUTION_1080P);
            }
        });
        mResolution720.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                chooseResolution720();
                SharedPrefUtils.setParam(KEY_VIDEO_RESOLUTION, VALUE_RESOLUTION_720P);
            }
        });
        mResolution480.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                chooseResolution480();
                SharedPrefUtils.setParam(KEY_VIDEO_RESOLUTION, VALUE_RESOLUTION_480P);
            }
        });
    }

    private void initResolutionStatus() {
        String videoResolution = (String) SharedPrefUtils.getParam(KEY_VIDEO_RESOLUTION, VALUE_RESOLUTION_1080P);
        switch (videoResolution) {
            case VALUE_RESOLUTION_1080P:
                chooseResolution1080();
                break;
            case VALUE_RESOLUTION_720P:
                chooseResolution720();
                 break;
            case VALUE_RESOLUTION_480P:
                chooseResolution480();
                break;
            default:
                chooseResolution1080();
                break;
        }
    }

    private void chooseResolution1080() {
        mResolution1080.selectedItem(true);
        mResolution720.selectedItem(false);
        mResolution480.selectedItem(false);
    }

    private void chooseResolution720() {
        mResolution1080.selectedItem(false);
        mResolution720.selectedItem(true);
        mResolution480.selectedItem(false);
    }

    private void chooseResolution480() {
        mResolution1080.selectedItem(false);
        mResolution720.selectedItem(false);
        mResolution480.selectedItem(true);
    }
}
