package com.ziguang.ptz.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhaoliangtai on 2017/9/18.
 */

public class LengthwaysSwitch extends View {

    private Bitmap mSwitchBottom, mSwitchThumb, mSwitchFrame, mSwitchMask;

    private float mCurrentX = 0;

    private boolean mSwitchOn = true;

    private int mMoveLength;

    private float mLastX = 0;

    private Rect mDest = null;

    private Rect mSrc = null;

    private int mDeltX = 0;

    private Paint mPaint = null;

    private boolean mFlag = false;


    public LengthwaysSwitch(Context context) {
        super(context);
    }

    public LengthwaysSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mSwitchBottom = BitmapFactory.decodeResource()
    }

    public void setSwitchOn(boolean switchOn) {
        mFlag = switchOn;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
