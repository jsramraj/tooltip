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

public class SeeThroughViewGroup extends ViewGroup {
    float cornerRadius = 20;

    public SeeThroughViewGroup(Context context) {
        super(context);
    }

    public SeeThroughViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeeThroughViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

        int lineWidth = 4;
        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float width = (float) getWidth();
        float height =  getHeight();
        RectF frame = new RectF(lineWidth, lineWidth, width-lineWidth, height-lineWidth);
        Path path = new Path();
        Paint stroke = new Paint();
        stroke.setAntiAlias(true);
        stroke.setStrokeWidth(lineWidth);
        stroke.setColor(Color.WHITE);
        stroke.setStyle(Paint.Style.STROKE);
        path.addRoundRect(frame, this.cornerRadius, this.cornerRadius, Path.Direction.CW);
        canvas.drawPath(path, stroke);
        canvas.drawRoundRect(frame, this.cornerRadius, this.cornerRadius, eraser);
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}