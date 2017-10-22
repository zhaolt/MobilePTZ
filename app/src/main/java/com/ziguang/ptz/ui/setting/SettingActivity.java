package com.ziguang.ptz.ui.setting;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.widget.TitleBar;

/**
 * Created by zhaoliangtai on 2017/10/21.
 */

public class SettingActivity extends FullScreenActivity {

    private TitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mTitleBar = (TitleBar) findViewById(R.id.tool_bar);
        mTitleBar.setActionMode(TitleBar.DISPLAY_STYLE_ACTION_TEXT_WITH_BACK_TEXT);
        loadFragment();
    }

    private void loadFragment() {
        SettingFragment settingFragment = (SettingFragment) getFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (null == settingFragment) {
            settingFragment = SettingFragment.createInstance();
        }
        changeFragment(getFragmentManager().beginTransaction(), settingFragment);
    }

    private void changeFragment(FragmentTransaction ft, Fragment f) {
        ft.replace(R.id.frame_fast_setting, f, f.getClass().toString());
        ft.commit();
    }

    private void changeToFragmentWithAnim(FragmentManager fm, Fragment f) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        changeFragment(ft, f);
    }
}
