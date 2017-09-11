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

public class TitleBar extends RelativeLayout {

    private View mView;

    private ImageView mBackImg;

    private TextView mBackText;

    private TextView mTitle;

    private TextView mRightText;

    private RelativeLayout mRootView;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        mBackImg = (ImageView) mView.findViewById(R.id.iv_back);
        mBackText = (TextView) mView.findViewById(R.id.tv_back);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mRightText = (TextView) mView.findViewById(R.id.tv_right);
        mRootView = (RelativeLayout) mView.findViewById(R.id.rl_root);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TitleBar, 0, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.TitleBar_back_image);
        mBackImg.setImageDrawable(drawable);
        String title = typedArray.getString(R.styleable.TitleBar_title_text);
        mTitle.setText(title);
        int background = typedArray.getColor(R.styleable.TitleBar_bg_color, context.getResources()
                .getColor(R.color.md_btn_selected));
        mRootView.setBackgroundColor(background);
        typedArray.recycle();
    }

}
