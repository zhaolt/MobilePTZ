package com.ziguang.ptz.ui.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class CameraActivity extends FullScreenActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        PreviewDisplayFragment fragment = new PreviewDisplayFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, fragment).commit();
    }


}
