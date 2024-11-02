package com.example.djj;

import android.view.View;

import android.view.animation.AccelerateDecelerateInterpolator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import java.util.Arrays;

public class WeatherBackgroundView extends View {
    private Paint paint;
    private int[] currentColors;
    private int[] targetColors;
    private float interpolation = 1f;
    private ValueAnimator animator;

    public WeatherBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        currentColors = new int[]{
                ContextCompat.getColor(getContext(), R.color.sunny_start),
                ContextCompat.getColor(getContext(), R.color.sunny_end)
        };
        targetColors = currentColors.clone();
    }

    public void transitionToWeather(String condition, boolean isDaytime) {
        int[] newColors = getColorsForWeather(condition, isDaytime);

        if (Arrays.equals(targetColors, newColors)) {
            return;
        }

        targetColors = newColors;

        if (animator != null) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            interpolation = (float) animation.getAnimatedValue();
            invalidate();
        });

        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create interpolated colors
        int[] colors = new int[currentColors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorUtils.blendARGB(
                    currentColors[i],
                    targetColors[i],
                    interpolation
            );
        }

        LinearGradient gradient = new LinearGradient(
                0, 0,
                0, getHeight(),
                colors,
                null,
                Shader.TileMode.CLAMP
        );

        paint.setShader(gradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private int[] getColorsForWeather(String condition, boolean isDaytime) {
        int startColor, endColor;

        condition = condition.toLowerCase();

        if (!isDaytime) {
            startColor = R.color.night_start;
            endColor = R.color.night_end;
        } else if (condition.contains("rain")) {
            startColor = R.color.rainy_start;
            endColor = R.color.rainy_end;
        } else if (condition.contains("cloud")) {
            startColor = R.color.cloudy_start;
            endColor = R.color.cloudy_end;
        } else {
            startColor = R.color.sunny_start;
            endColor = R.color.sunny_end;
        }

        return new int[]{
                ContextCompat.getColor(getContext(), startColor),
                ContextCompat.getColor(getContext(), endColor)
        };
    }
}