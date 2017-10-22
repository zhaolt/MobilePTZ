package com.ziguang.ptz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ziguang.ptz.R;

/**
 * Created by zhaoliangtai on 2017/9/10.
 */

public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private static final int DISPLAY_NO_TITLE = 2;
    private static final int DISPLAY_NO_BACK_WITH_TEXT = 4;
    private static final int DISPLAY_NO_ACTION_IMAGE = 8;
    private static final int DISPLAY_NO_ACTION_TEXT = 16;
    private static final int DISPLAY_NO_BACK_IMAGE = 32;

    public static final int DISPLAY_STYLE_ACTION_TEXT_WITH_BACK_TEXT = DISPLAY_NO_ACTION_IMAGE
            | DISPLAY_NO_BACK_IMAGE;

    public static final int DISPLAY_STYLE_ACTION_TEXT_WITH_BACK_IMAGE =  DISPLAY_NO_ACTION_IMAGE
            | DISPLAY_NO_BACK_WITH_TEXT;

    public static final int DISPLAY_STYLE_ACTION_ONLY_BACK_IMAGE = DISPLAY_NO_ACTION_IMAGE
            | DISPLAY_NO_BACK_WITH_TEXT
            | DISPLAY_NO_TITLE;

    private View mView;

    private ImageView mBackImg;

    private TextView mBackText;

    private TextView mTitle;

    private TextView mRightText;

    private RelativeLayout mRootView;

    private OnActionBackClickListener mActionBackClickListener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setActionBackClickListener(OnActionBackClickListener listener) {
        mActionBackClickListener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        mBackImg = (ImageView) mView.findViewById(R.id.iv_back);
        mBackText = (TextView) mView.findViewById(R.id.tv_back);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mRightText = (TextView) mView.findViewById(R.id.tv_right);
        mRootView = (RelativeLayout) mView.findViewById(R.id.rl_root);
        mBackImg.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mBackText.setOnClickListener(this);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TitleBar, 0, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.TitleBar_back_image);
        mBackImg.setImageDrawable(drawable);
        String title = typedArray.getString(R.styleable.TitleBar_title_text);
        mTitle.setText(title);
        int background = typedArray.getColor(R.styleable.TitleBar_bg_color, context.getResources()
                .getColor(R.color.color2));
        String backText = typedArray.getString(R.styleable.TitleBar_back_text);
        Drawable backTextImg = typedArray.getDrawable(R.styleable.TitleBar_back_text_img);
        mBackText.setText(backText);
        mBackText.setCompoundDrawables(backTextImg, null, null, null);
        mRootView.setBackgroundColor(background);
        typedArray.recycle();
    }

    public void setActionMode(int mode) {
        mBackImg.setVisibility(VISIBLE);
        mBackText.setVisibility(VISIBLE);
        mRightText.setVisibility(VISIBLE);
        if ((mode & DISPLAY_NO_ACTION_TEXT) == DISPLAY_NO_ACTION_TEXT) {
            mRightText.setVisibility(GONE);
        }
        if ((mode & DISPLAY_NO_BACK_WITH_TEXT) == DISPLAY_NO_BACK_WITH_TEXT) {
            mBackText.setVisibility(GONE);
        }
        if ((mode & DISPLAY_NO_BACK_IMAGE) == DISPLAY_NO_BACK_IMAGE) {
            mBackImg.setVisibility(GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (null != mActionBackClickListener)
                    mActionBackClickListener.onActionBackClick();
                break;
        }
    }

    public interface OnActionBackClickListener {
        void onActionBackClick();
    }
}
