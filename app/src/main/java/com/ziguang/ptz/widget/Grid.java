package com.ziguang.ptz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ziguang.ptz.R;
import com.ziguang.ptz.utils.UIUtils;

public class Grid extends View {
    private Paint paint;
    private float lineWidth = 2;

    private boolean mWithDiagonal;

    public Grid(Context context) {
        this(context, null);
    }

    public Grid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Grid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAlpha(100);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.Grid, 0, 0);
        mWithDiagonal = typedArray.getBoolean(R.styleable.Grid_with_diagonal, false);
    }

    public void showWithDiagonal(boolean withDiagonal) {
        mWithDiagonal = withDiagonal;
    }

    /**
     * 绘制网格线
     */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        int systemScreenWidth = UIUtils.getScreenMetrics(getContext()).x;
        int systemScreenHeight = UIUtils.getScreenMetrics(getContext()).y;

        if (mWithDiagonal) {
            canvas.drawLine(0, 0, systemScreenWidth, systemScreenHeight, paint);
            canvas.drawLine(0, systemScreenHeight, systemScreenWidth, 0, paint);
        }

        for (int i = 0; i < 2; i++) {
            canvas.drawLine(0, (i + 1) * systemScreenHeight / 3 - lineWidth / 2, systemScreenWidth, (i + 1) * systemScreenHeight / 3 + lineWidth / 2, paint);
            canvas.drawLine((i + 1) * systemScreenWidth / 3 - lineWidth / 2, 0, (i + 1) * systemScreenWidth / 3 + lineWidth / 2, systemScreenHeight, paint);
        }

    }
}
