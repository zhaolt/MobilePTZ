package com.ziguang.ptz.ui.fast_setting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.ui.camera.CameraActivity;
import com.ziguang.ptz.utils.SharedPrefUtils;

import java.lang.ref.WeakReference;

/**
 * Created by zhaoliangtai on 2017/9/25.
 */

public class GridSelectFragment extends Fragment {

    public static final String KEY_GRID = "key-grid";
    public static final String VALUE_GRID_NONE = "value-grid-none";
    public static final String VALUE_GRID_LINES = "value-grid-lines";
    public static final String VALUE_GRID_DIAGONAL = "value-grid-diagonal";
    public static final String VALUE_GRID_CENTER_POINT = "value-grid-center-point";

    private FastSettingRadioItemView mGridNone, mGridLines, mGridDiagonal, mGridCenterPoint;

    private WeakReference<CameraActivity> mCameraActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_select, null);
        mCameraActivity = new WeakReference<>((CameraActivity) getActivity());
        initView(view);
        initGridStatus();
        return view;
    }

    private void initView(View view) {
        mGridNone = (FastSettingRadioItemView) view.findViewById(R.id.grid_none);
        mGridLines = (FastSettingRadioItemView) view.findViewById(R.id.grid_lines);
        mGridDiagonal = (FastSettingRadioItemView) view.findViewById(R.id.grid_diagonal);
        mGridCenterPoint = (FastSettingRadioItemView) view.findViewById(R.id.grid_center_point);
        mGridNone.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                SharedPrefUtils.setParam(KEY_GRID, VALUE_GRID_NONE);
                chooseGridModeNone();
                mCameraActivity.get().updateGrid(VALUE_GRID_NONE);
            }
        });
        mGridLines.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                SharedPrefUtils.setParam(KEY_GRID, VALUE_GRID_LINES);
                chooseGridModeLines();
                mCameraActivity.get().updateGrid(VALUE_GRID_LINES);
            }
        });
        mGridDiagonal.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                SharedPrefUtils.setParam(KEY_GRID, VALUE_GRID_DIAGONAL);
                chooseGridModeDiagonal();
                mCameraActivity.get().updateGrid(VALUE_GRID_DIAGONAL);
            }
        });
        mGridCenterPoint.setOnItemSelectedListener(new FastSettingRadioItemView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v) {
                SharedPrefUtils.setParam(KEY_GRID, VALUE_GRID_CENTER_POINT);
                chooseGridModeCenterPoint();
                mCameraActivity.get().updateGrid(VALUE_GRID_CENTER_POINT);
            }
        });
    }

    private void initGridStatus() {
        String gridMode = (String) SharedPrefUtils.getParam(KEY_GRID, VALUE_GRID_NONE);
        switch (gridMode) {
            case VALUE_GRID_NONE:
                chooseGridModeNone();
                break;
            case VALUE_GRID_LINES:
                chooseGridModeLines();
                break;
            case VALUE_GRID_DIAGONAL:
                chooseGridModeDiagonal();
                break;
            case VALUE_GRID_CENTER_POINT:
                chooseGridModeCenterPoint();
                break;
        }
    }

    private void chooseGridModeNone() {
        mGridNone.selectedItem(true);
        mGridLines.selectedItem(false);
        mGridDiagonal.selectedItem(false);
        mGridCenterPoint.selectedItem(false);
    }

    private void chooseGridModeLines() {
        mGridNone.selectedItem(false);
        mGridLines.selectedItem(true);
        mGridDiagonal.selectedItem(false);
        mGridCenterPoint.selectedItem(false);
    }

    private void chooseGridModeDiagonal() {
        mGridNone.selectedItem(false);
        mGridLines.selectedItem(false);
        mGridDiagonal.selectedItem(true);
        mGridCenterPoint.selectedItem(false);
    }

    private void chooseGridModeCenterPoint() {
        mGridNone.selectedItem(false);
        mGridLines.selectedItem(false);
        mGridDiagonal.selectedItem(false);
        mGridCenterPoint.selectedItem(true);
    }
}
