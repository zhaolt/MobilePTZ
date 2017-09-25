package com.ziguang.ptz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ziguang.ptz.R;
import com.ziguang.ptz.utils.UIUtils;

public class Grid extends View {

    public static final int MODE_LINES = 101;
    public static final int MODE_WITH_DIAGONAL = 102;
    public static final int MODE_CENTER_POINT = 103;

    private Paint paint;

    private Paint mGunsightPaint;

    private float lineWidth = 2;

    private int mGridMode = MODE_LINES;

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
        paint.setColor(context.getResources().getColor(R.color.color4));
        paint.setStrokeWidth(UIUtils.dp2px(0.3f));

        mGunsightPaint = new Paint();
        mGunsightPaint.setAntiAlias(true);
        mGunsightPaint.setStyle(Paint.Style.STROKE);
        mGunsightPaint.setAlpha(100);
        mGunsightPaint.setColor(context.getResources().getColor(R.color.color4));

        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.Grid, 0, 0);
        mGridMode = typedArray.getInt(R.styleable.Grid_grid_mode, MODE_LINES);
        typedArray.recycle();
    }

    public void showWithDiagonal(int gridMode) {
        mGridMode = gridMode;
        invalidate();
    }

    /**
     * 绘制网格线
     */
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        int systemScreenWidth = UIUtils.getScreenMetrics(getContext()).x;
        int systemScreenHeight = UIUtils.getScreenMetrics(getContext()).y;
        switch (mGridMode) {
            case MODE_LINES:
                drawGridLines(canvas, systemScreenWidth, systemScreenHeight);
                break;
            case MODE_WITH_DIAGONAL:
                drawDiagonal(canvas, systemScreenWidth, systemScreenHeight);
                drawGridLines(canvas, systemScreenWidth, systemScreenHeight);
                break;
            case MODE_CENTER_POINT:
                drawGunsight(canvas, systemScreenWidth, systemScreenHeight);
                break;
        }
    }

    private void drawGridLines(Canvas canvas, int systemScreenWidth, int systemScreenHeight) {
        for (int i = 0; i < 2; i++) {
            canvas.drawLine(0, (i + 1) * systemScreenHeight / 3 - lineWidth / 2, systemScreenWidth, (i + 1) * systemScreenHeight / 3 + lineWidth / 2, paint);
            canvas.drawLine((i + 1) * systemScreenWidth / 3 - lineWidth / 2, 0, (i + 1) * systemScreenWidth / 3 + lineWidth / 2, systemScreenHeight, paint);
        }
    }

    private void drawDiagonal(Canvas canvas, int systemScreenWidth, int systemScreenHeight) {
        canvas.drawLine(0, 0, systemScreenWidth, systemScreenHeight, paint);
        canvas.drawLine(0, systemScreenHeight, systemScreenWidth, 0, paint);
    }

    private void drawGunsight(Canvas canvas, int systemScreenWidth, int systemScreenHeight) {
        int radius = UIUtils.dip2px(getContext(), 8.5f);
        int centerX = systemScreenWidth / 2;
        int centerY = systemScreenHeight / 2;
        mGunsightPaint.setStrokeWidth(UIUtils.dp2px(0.6f));
        canvas.drawCircle(centerX, centerY, radius, mGunsightPaint);

        float gunsightLineLength = UIUtils.dp2px(2.8f);
        canvas.drawLine(centerX - radius - gunsightLineLength, centerY, centerX - radius, centerY, mGunsightPaint);
        canvas.drawLine(centerX, centerY - radius - gunsightLineLength, centerX, centerY - radius, mGunsightPaint);
        canvas.drawLine(centerX + radius, centerY, centerX + radius + gunsightLineLength, centerY, mGunsightPaint);
        canvas.drawLine(centerX, centerY + radius, centerX, centerY + radius + gunsightLineLength, mGunsightPaint);

        mGunsightPaint.setStrokeWidth(UIUtils.dp2px(1.2f));
        int rectR = UIUtils.dip2px(getContext(), 4f) / 2;
        int degree = UIUtils.dip2px(getContext(), 1f);
        RectF rectF = new RectF(centerX - rectR, centerY - rectR, centerX + rectR, centerY + rectR);
        canvas.drawRoundRect(rectF, degree, degree, mGunsightPaint);
    }
}
