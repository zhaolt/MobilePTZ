package com.ziguang.ptz.ui.setting;

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
 * Created by zhaoliangtai on 2017/9/19.
 */

public class FastSettingRadioItemView extends RelativeLayout {

    private TextView mItemName;

    private ImageView mRightIcon, mSelectedFlag;

    private View mRoot;

    private OnItemSelectedListener mOnItemSelectedListener;

    public FastSettingRadioItemView(Context context) {
        this(context, null);
    }

    public FastSettingRadioItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_fast_setting_radio, this, true);
        mItemName = (TextView) findViewById(R.id.tv_item_name);
        mRightIcon = (ImageView) findViewById(R.id.icon_right);
        mSelectedFlag = (ImageView) findViewById(R.id.iv_selected_flag);
        mRoot = findViewById(R.id.root);
        mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemSelectedListener)
                    mOnItemSelectedListener.onItemSelected(v);
            }
        });
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FastSettingRadioItemView, 0, 0);
        String itemName = typedArray.getString(R.styleable.FastSettingRadioItemView_radio_item_name);
        mItemName.setText(itemName);
        Drawable drawable = typedArray.getDrawable(R.styleable.FastSettingRadioItemView_radio_right_icon);
        mRightIcon.setImageDrawable(drawable);
        typedArray.recycle();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public boolean isItemSelected() {
        return mSelectedFlag.getVisibility() == VISIBLE;
    }

    public void selectedItem(boolean isVisible) {
        mSelectedFlag.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(View v);
    }
}
