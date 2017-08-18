package com.ziguang.ptz.ui.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.utils.UIUtils;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class PreviewDisplayFragment extends Fragment {

    private View mRootView;

    private TextureView mTextureView;

    private MySurfaceTextureListener mSurfaceTextureListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = UIUtils.inflate(R.layout.fragment_camera_preview);
        mTextureView = (TextureView) mRootView.findViewById(R.id.preview_texture);
        mSurfaceTextureListener = new MySurfaceTextureListener();
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        return mRootView;
    }
}
