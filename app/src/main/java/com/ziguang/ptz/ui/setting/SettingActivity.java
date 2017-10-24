package com.ziguang.ptz.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.widget.TitleBar;

/**
 * Created by zhaoliangtai on 2017/10/21.
 */

public class SettingActivity extends FullScreenActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    private TitleBar mTitleBar;

    private IconWithArrowsItemView mCameraSettingItem;
    private IconWithArrowsItemView mPtzSettingItem;
    private IconWithArrowsItemView mCommonSettingItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mTitleBar = (TitleBar) findViewById(R.id.tool_bar);
        mTitleBar.setActionMode(TitleBar.DISPLAY_STYLE_ACTION_BACK_TEXT);
        mTitleBar.setActionBackClickListener(new TitleBar.OnActionBackClickListener() {
            @Override
            public void onActionBackClick() {
                finish();
            }
        });
        mCameraSettingItem = (IconWithArrowsItemView) findViewById(R.id.camera_setting);
        mPtzSettingItem = (IconWithArrowsItemView) findViewById(R.id.ptz_setting);
        mCommonSettingItem = (IconWithArrowsItemView) findViewById(R.id.common_setting);
        mCameraSettingItem.setItemClickListener(new IconWithArrowsItemView.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Intent intent = CameraSettingActivity.getCallingIntent(SettingActivity.this);
                startActivity(intent);
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

    }
}
