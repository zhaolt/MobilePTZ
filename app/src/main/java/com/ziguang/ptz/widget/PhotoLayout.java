package com.ziguang.ptz.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ziguang.ptz.R;

/**
 * Created by zhaoliangtai on 2017/9/14.
 */

public class PhotoLayout extends FrameLayout {

    private FrameLayout mContainer;

    private SimpleDraweeView mPhoto;

    private ImageView mVideoType;

    private FrameLayout mShader;

    private TextView mDuration;

    public PhotoLayout(@NonNull Context context) {
        this(context, null);
    }

    public PhotoLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContainer = (FrameLayout) inflate(context, R.layout.layout_photo_album, this);
        mPhoto = (SimpleDraweeView) mContainer.findViewById(R.id.iv_photo);
        mShader = (FrameLayout) mContainer.findViewById(R.id.shader);
        mVideoType = (ImageView) mContainer.findViewById(R.id.iv_video_type);
        mDuration = (TextView) mContainer.findViewById(R.id.tv_duration);
    }

    public SimpleDraweeView getPhotoImageView() {
        return mPhoto;
    }

    public void showAsVideo(String duration) {
        mVideoType.setVisibility(VISIBLE);
        mDuration.setVisibility(VISIBLE);
        mDuration.setText(duration);
    }

    public void showAsPhoto() {
        mVideoType.setVisibility(GONE);
        mDuration.setVisibility(GONE);
    }
}
