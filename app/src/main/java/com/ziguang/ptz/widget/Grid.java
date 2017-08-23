package com.ziguang.ptz.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.ziguang.ptz.utils.UIUtils;

public class Grid extends View {
    private Paint paint;
    private float lineWidth = 2;

    public Grid(Context context) {
        super(context);
        init();
    }

    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Grid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAlpha(100);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
    }

    /**
     * 绘制网格线
     */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        int systemScreenWidth = UIUtils.getScreenMetrics(getContext()).x;
        int systemScreenHeight = UIUtils.getScreenMetrics(getContext()).y;

        for (int i = 0; i < 2; i++) {
            canvas.drawLine(0, (i + 1) * systemScreenHeight / 3 - lineWidth / 2, systemScreenWidth, (i + 1) * systemScreenHeight / 3 + lineWidth / 2, paint);
            canvas.drawLine((i + 1) * systemScreenWidth / 3 - lineWidth / 2, 0, (i + 1) * systemScreenWidth / 3 + lineWidth / 2, systemScreenHeight, paint);
        }

    }
}
