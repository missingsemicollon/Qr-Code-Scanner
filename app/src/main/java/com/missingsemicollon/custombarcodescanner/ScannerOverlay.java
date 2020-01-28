package com.missingsemicollon.custombarcodescanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

public class ScannerOverlay extends ViewGroup {

    private float left, top, endY;
    private int rectWidth, rectHeight;
    private int frames;
    private boolean revAnimation;
    private Paint eraser;
    private PorterDuffXfermode porter;
    private RectF rect;
    private Paint line;


    public ScannerOverlay(Context context) {
        super(context);
    }

    public ScannerOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerOverlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        eraser = new Paint();
        porter = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        int lineColor;
        float lineWidth;
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int w = displayMetrics.widthPixels * 90 / 100;
            rectWidth = w;
            rectHeight = w;
            lineColor = Color.RED;
            lineWidth = 5;
            frames = 6;
        } catch (Exception e) {
            rectWidth = 400;
            rectHeight = 400;
            lineColor = Color.RED;
            lineWidth = 5;
            frames = 6;
        }

        line = new Paint();
        line.setColor(lineColor);
        line.setStrokeWidth(lineWidth);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {

        left = (w - rectWidth) / 2;
        top = (h - rectHeight) / 2;
        endY = top;

        rect = new RectF(left, top, rectWidth + left, rectHeight + top);
        super.onSizeChanged(w, h, oldW, oldH);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cornerRadius = 0;
        eraser.setAntiAlias(true);
        eraser.setXfermode(porter);

        canvas.drawRoundRect(rect, (float) cornerRadius, (float) cornerRadius, eraser);

        if (endY >= top + rectHeight + frames) {
            revAnimation = true;
        } else if (endY == top + frames) {
            revAnimation = false;
        }

        if (revAnimation) {
            endY -= frames;
        } else {
            endY += frames;
        }
        canvas.drawLine(left, endY, left + rectWidth, endY, line);
        invalidate();
    }
}