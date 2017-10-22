package com.ziguang.ptz.ui.fast_setting;

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
 * Created by zhaoliangtai on 2017/9/18.
 */

public class FastSettingArrowsItemView extends RelativeLayout {

    private static final int VISIBLE = 0;

    private static final int GONE = 1;

    private static final int INVISIBLE = 2;

    private OnItemClickListener mOnItemClickListener;

    private TextView mItemName;

    private ImageView mRightIcon;

    private View mRoot;

    public FastSettingArrowsItemView(Context context) {
        this(context, null);
    }

    public FastSettingArrowsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_common_setting_item, this, true);
        mItemName = (TextView) findViewById(R.id.tv_item_name);
        mRightIcon = (ImageView) findViewById(R.id.iv_right_icon);
        mRoot = findViewById(R.id.root);
        mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener)
                    mOnItemClickListener.onClick(v);
            }
        });
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FastSettingArrowsItemView, 0, 0);
        String itemName = typedArray.getString(R.styleable.FastSettingArrowsItemView_item_name);
        mItemName.setText(itemName);
        Drawable drawable = typedArray.getDrawable(R.styleable.FastSettingArrowsItemView_right_icon);
        mRightIcon.setImageDrawable(drawable);
        int visible = typedArray.getInt(R.styleable.FastSettingArrowsItemView_right_icon_visibility,
                VISIBLE);
        switch (visible) {
            case VISIBLE:
                mRightIcon.setVisibility(View.VISIBLE);
                break;
            case GONE:
                mRightIcon.setVisibility(View.GONE);
                break;
            case INVISIBLE:
                mRightIcon.setVisibility(View.INVISIBLE);
                break;
        }
        typedArray.recycle();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setRightIcon(Drawable drawable) {
        mRightIcon.setImageDrawable(drawable);
    }

    public void setRightIconVisibility(int visibility) {
        mRightIcon.setVisibility(visibility);
    }

    public interface OnItemClickListener {
        void onClick(View v);
    }
}
