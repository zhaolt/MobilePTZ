package com.ziguang.ptz.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ziguang.ptz.R;
import com.ziguang.ptz.utils.UIUtils;

/**
 * Created by zhaoliangtai on 2017/9/18.
 */

public class LengthwaysSwitch extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private static final float ANIM_MAX_FRACTION = 1.0f;
    private static final String TAG = LengthwaysSwitch.class.getSimpleName();

    private Bitmap mSwitchBg;
    private Bitmap mSwitchVideoNormal;
    private Bitmap mSwitchPhotoNormal;
    private Bitmap mSwitchButton;


    private Rect mSwitchBgSrc;
    private Rect mSwitchButtonSrc;
    private Rect mSwitchVideoNormalSrc;
    private Rect mSwitchPhotoNormalSrc;
    private Paint mPaint;

    private boolean isOpen = false;
    private boolean isDuringAnimation = false;
    private float mTransitionLength;
    private long mAnimationDuration = 300L;
    private float mAnimationFraction = 0.0f;
    private ValueAnimator mValueAnimator;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private int mCenterX;
    private int mCenterY;


    private float mButtonX;
    private int mButtonStartY;
    private int mButtonEndY;

    private OnSwitchChangeListener mOnSwitchChangeListener;


    public LengthwaysSwitch(Context context) {
        this(context, null);
    }

    public LengthwaysSwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LengthwaysSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener listener) {
        mOnSwitchChangeListener = listener;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mSwitchBg = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_bg);
        mSwitchPhotoNormal = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_photo_normal);
        mSwitchVideoNormal = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_video_normal);
        mSwitchButton = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_photo);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mSwitchBgSrc = new Rect(0, 0, mSwitchBg.getWidth(), mSwitchBg.getHeight());
        mSwitchButtonSrc = new Rect(0, 0, mSwitchButton.getWidth(), mSwitchButton.getHeight());
        mSwitchVideoNormalSrc = new Rect(0, 0, mSwitchVideoNormal.getWidth(), mSwitchVideoNormal.getHeight());
        mSwitchPhotoNormalSrc = new Rect(0, 0, mSwitchPhotoNormal.getWidth(), mSwitchPhotoNormal.getHeight());

        mCenterX = mSwitchBg.getWidth() / 2;
        mCenterY = mSwitchBg.getHeight() / 2;

        mButtonX = mCenterX - mSwitchButton.getWidth() / 2;
        mButtonStartY = mCenterY - mCenterY / 2 - mSwitchButton.getHeight() / 2
                + UIUtils.dip2px(getContext(), 1);
        mButtonEndY = mCenterY + mCenterY / 2 - mSwitchButton.getHeight() / 2
                - UIUtils.dip2px(getContext(), 1);
        mTransitionLength = mButtonEndY - mButtonStartY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchBg.getWidth(), mSwitchBg.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawBitmapByLocation(canvas, mSwitchButton, mSwitchButtonSrc,
                mButtonX, mButtonStartY + getForegroundTransitionValue());
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(mSwitchBg, mSwitchBgSrc, mSwitchBgSrc, null);
        drawStationaryBitmap(canvas, mSwitchPhotoNormal, mSwitchPhotoNormalSrc, true);
        drawStationaryBitmap(canvas, mSwitchVideoNormal, mSwitchVideoNormalSrc, false);
    }

    private void drawBitmapByLocation(Canvas canvas, Bitmap bitmap, Rect rect, float x, float y) {
        canvas.save();
        canvas.translate(x, y);
        canvas.drawBitmap(bitmap, rect, rect, null);
        canvas.restore();
    }

    private void drawStationaryBitmap(Canvas canvas, Bitmap bitmap, Rect rect, boolean isUpper) {
        canvas.save();
        int[] locations = calculateOffset(bitmap, isUpper);
        canvas.translate(locations[0], locations[1]);
        canvas.drawBitmap(bitmap, rect, rect, null);
        canvas.restore();
    }

    private int[] calculateOffset(Bitmap bitmap, boolean isUpper) {
        int[] result = new int[2];
        result[0] = mCenterX - bitmap.getWidth() / 2;
        if (isUpper) {
            result[1] = mCenterY - mCenterY / 2 -
                    bitmap.getHeight() / 2 + UIUtils.dip2px(getContext(), 1);
        } else {
            result[1] = mCenterY + mCenterY / 2 -
                    bitmap.getHeight() / 2 - UIUtils.dip2px(getContext(), 1);
        }
        return result;
    }


    private float getForegroundTransitionValue() {
        float result;
        if (isOpen) {
            if (isDuringAnimation) {
                result = mAnimationFraction > ANIM_MAX_FRACTION ?
                        mTransitionLength : mTransitionLength * mAnimationFraction;
            } else {
                result = mTransitionLength;
            }
        } else {
            if (isDuringAnimation) {
                result = mTransitionLength * mAnimationFraction;
            } else {
                result = 0;
            }
        }
        return result;
    }

    private void startOpenAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0.0f, ANIM_MAX_FRACTION);
        mValueAnimator.setDuration(mAnimationDuration);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.start();
    }

    private void startCloseAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(ANIM_MAX_FRACTION, 0);
        mValueAnimator.setDuration(mAnimationDuration);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isDuringAnimation = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isDuringAnimation = false;
        if (isOpen) {
            mSwitchButton = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_video);
        } else {
            mSwitchButton = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_photo);
        }
        if (null != mOnSwitchChangeListener)
            mOnSwitchChangeListener.onChanged(isOpen);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isDuringAnimation = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        isDuringAnimation = true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Log.w(TAG, "animation value = " + animation.getAnimatedValue());
        mAnimationFraction = (float) animation.getAnimatedValue();
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isOpen = this.isOpen ? 1 : 0;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);
        boolean result = ss.isOpen == 1;
        setState(result);
    }

    public void setState(boolean open) {
        isOpen = open;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                if (isDuringAnimation) {
                    return true;
                }
                if (isOpen) {
                    startCloseAnimation();
                    isOpen = false;
                } else {
                    startOpenAnimation();
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public interface OnSwitchChangeListener {
        void onChanged(boolean isOpen);
    }

    static class SavedState extends BaseSavedState {
        int isOpen;
        public SavedState(Parcel source) {
            super(source);
            isOpen = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isOpen);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[0];
            }
        };
    }
}
