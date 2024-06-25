package com.s22010189.pil_pal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
// customization and functionality of progress bar of step Tracker. Created with the help of Github resources.
public class CurvedProgressBar extends View {

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private RectF rectF;
    private float progress = 0;

    public CurvedProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.background));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(50);
        backgroundPaint.setAntiAlias(true);

        foregroundPaint = new Paint();
        foregroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.foreground));
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(50);
        foregroundPaint.setAntiAlias(true);

        rectF = new RectF(100, 100, 500, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectF, 180, 180, false, backgroundPaint);
        canvas.drawArc(rectF, 180, 180 * progress / 100, false, foregroundPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
