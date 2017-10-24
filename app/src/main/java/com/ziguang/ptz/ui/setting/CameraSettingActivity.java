package com.ziguang.ptz.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.widget.TitleBar;

/**
 * Created by zhaoliangtai on 2017/10/23.
 */

public class CameraSettingActivity extends FullScreenActivity {

    private TitleBar mTitleBar;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CameraSettingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_setting);
        mTitleBar = (TitleBar) findViewById(R.id.tool_bar);
        mTitleBar.setActionMode(TitleBar.DISPLAY_STYLE_ACTION_TEXT_WITH_BACK_TEXT);
        mTitleBar.setActionText("完成");

    }
}
