package com.example.djj;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class AnimationUtils {

    public static AnimatorSet createSplashAnimations(View iconView, View titleView) {
        // Scale animation for icon
        ObjectAnimator iconScaleX = ObjectAnimator.ofFloat(iconView, "scaleX", 0.3f, 1f);
        ObjectAnimator iconScaleY = ObjectAnimator.ofFloat(iconView, "scaleY", 0.3f, 1f);

        // Fade animation for title
        ObjectAnimator titleAlpha = ObjectAnimator.ofFloat(titleView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(iconScaleX, iconScaleY, titleAlpha);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        return animatorSet;
    }
}