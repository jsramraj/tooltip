package com.ramaraj.tooltip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Constructing the component highlight view for tooltip.
 */
public class SeeThroughViewGroup extends ViewGroup {

    private static final int STROKE_LINE_WIDTH = 4;
    private static final int STROKE_LINE_HEIGHT = 4;

    private float cornerRadius;

    private Paint strokePaint = null;
    private Paint eraserPaint = null;

    private Path paintPath = null;

    private RectF rectFrame = new RectF();

    public SeeThroughViewGroup(Context context) {
        this(context, null);
    }

    public SeeThroughViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeeThroughViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {

        paintPath = new Path();

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(STROKE_LINE_WIDTH);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);

        eraserPaint = new Paint();
        eraserPaint.setAntiAlias(true);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        rectFrame.set(
                STROKE_LINE_WIDTH,
                STROKE_LINE_HEIGHT,
                width - STROKE_LINE_WIDTH,
                height - STROKE_LINE_WIDTH
        );

        paintPath.addRoundRect(
                rectFrame,
                cornerRadius,
                cornerRadius,
                Path.Direction.CW
        );

        canvas.drawPath(paintPath, strokePaint);

        canvas.drawRoundRect(
                rectFrame,
                cornerRadius,
                cornerRadius,
                eraserPaint
        );
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}