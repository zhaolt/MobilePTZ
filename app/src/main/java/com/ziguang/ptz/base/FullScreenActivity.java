package com.ziguang.ptz.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class FullScreenActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            hideSystemUI();
        }
        return super.dispatchTouchEvent(ev);
    }
}
