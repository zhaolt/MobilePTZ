package com.ziguang.ptz.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.ziguang.ptz.R;


public class CustomBaseDialog extends Dialog {

    protected static final int DEFAULT_GRAVITY = Gravity.BOTTOM;

    protected static final AnimationDirection DEFAULT_ANIMATION_DIRECTION = AnimationDirection.VERTICLE;

    public CustomBaseDialog(Context context, int gravity, float marginVerticle,
                            AnimationDirection animationDirection) {
        super(context, R.style.CustomDialog_ptz);
        init(gravity, marginVerticle, animationDirection);
    }

    private void init(int gravity, float marginVerticle,
                      AnimationDirection animationDirection) {
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        Window dialogWindow = this.getWindow();
        if (animationDirection == AnimationDirection.VERTICLE) {
            dialogWindow.setWindowAnimations(R.style.DialogVerticleWindowAnim);
        } else if (animationDirection == AnimationDirection.HORIZONAL) {
            dialogWindow.setWindowAnimations(R.style.DialogHorizonalWindowAnim);
        } else if (animationDirection == AnimationDirection.RIGHT_IN_RIGHT_OUT) {
            dialogWindow.setWindowAnimations(R.style.DialogRightHorizonalWindowAnim);
        }
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.gravity = gravity;
        layoutParams.verticalMargin = marginVerticle;
        dialogWindow.setAttributes(layoutParams);
    }

    public enum AnimationDirection {
        HORIZONAL, VERTICLE, RIGHT_IN_RIGHT_OUT
    }

    public static class Builder {

        protected Context context;
        protected int gravity = DEFAULT_GRAVITY;
        protected AnimationDirection animationDirection = DEFAULT_ANIMATION_DIRECTION;
        protected float marginVerticle;

        public Builder(Context context) {
            super();
            this.context = context;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setAnimationDirection(AnimationDirection animationDirection) {
            this.animationDirection = animationDirection;
            return this;
        }

        public Builder setMarginVerticle(float marginVerticle) {
            this.marginVerticle = marginVerticle;
            return this;
        }

        public CustomBaseDialog build() {
            return new CustomBaseDialog(context, gravity, marginVerticle,
                    animationDirection);
        }

    }

}
