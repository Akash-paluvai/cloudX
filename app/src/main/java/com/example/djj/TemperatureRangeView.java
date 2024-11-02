package com.example.djj;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TemperatureRangeView extends View {
    private Paint paint;
    private float minTemp = 0f;
    private float maxTemp = 100f;
    private float currentMin = 0f;
    private float currentMax = 0f;

    public TemperatureRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    public void setProgress(float min, float max) {
        this.currentMin = (min - minTemp) / (maxTemp - minTemp);
        this.currentMax = (max - minTemp) / (maxTemp - minTemp);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float radius = height / 2f;

        paint.setColor(Color.parseColor("#44FFFFFF"));
        canvas.drawRoundRect(0, 0, width, height, radius, radius, paint);

        paint.setColor(Color.WHITE);
        float startX = width * currentMin;
        float endX = width * currentMax;
        canvas.drawRoundRect(startX, 0, endX, height, radius, radius, paint);
    }
}