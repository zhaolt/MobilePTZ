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
 * Created by zhaoliangtai on 2017/10/22.
 */

public class IconWithArrowsItemView extends RelativeLayout {

    private TextView mItemTitle;
    private ImageView mItemIcon;
    private RelativeLayout mRoot;
    private OnItemClickListener mItemClickListener;


    public IconWithArrowsItemView(Context context) {
        this(context, null);
    }

    public IconWithArrowsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconWithArrowsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_icon_with_arrows, this, true);
        mItemTitle = (TextView) findViewById(R.id.tv_title);
        mItemIcon = (ImageView) findViewById(R.id.iv_icon);
        mRoot = (RelativeLayout) findViewById(R.id.root);
        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.IconWithArrowsItemView, 0, 0);
        String itemTitle = typedArray.getString(R.styleable.IconWithArrowsItemView_item_title);
        Drawable itemIcon = typedArray.getDrawable(R.styleable.IconWithArrowsItemView_item_icon);
        mItemTitle.setText(itemTitle);
        mItemIcon.setImageDrawable(itemIcon);
        typedArray.recycle();
        mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemClickListener)
                    mItemClickListener.onItemClick();
            }
        });
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }
}
