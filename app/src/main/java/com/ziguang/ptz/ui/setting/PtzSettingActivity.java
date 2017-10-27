package com.ziguang.ptz.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;

/**
 * Created by zhaoliangtai on 2017/10/26.
 */

public class PtzSettingActivity extends FullScreenActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PtzSettingActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptz_setting);
    }
}
