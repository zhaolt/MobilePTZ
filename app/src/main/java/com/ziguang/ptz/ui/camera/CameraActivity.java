package com.ziguang.ptz.ui.camera;

import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
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
        mCameraSetting.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int rotation = getWindowManager().getDefaultDisplay().getRotation();
                        try {
                            CameraHelper.getInstance().takePicture(rotation);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        CameraHelper.getInstance().continuePreview();
                        break;
                }
                return true;
            }
        });
    }


}
