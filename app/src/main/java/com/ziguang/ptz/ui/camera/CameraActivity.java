package com.ziguang.ptz.ui.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.FullScreenActivity;
import com.ziguang.ptz.core.camera.CameraHelper;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class CameraActivity extends FullScreenActivity {


    private ImageButton mCameraSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        PreviewDisplayFragment fragment = new PreviewDisplayFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, fragment).commit();
        mCameraSetting = (ImageButton) findViewById(R.id.iv_camera_setting);
        mCameraSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraHelper.getInstance().takePicture(getWindowManager().getDefaultDisplay().getRotation());
            }
        });
    }


}
