package com.example.djj;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.Intent;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2000;
    private VideoView splashVideo;
    private ImageView splashIcon;
    private TextView splashTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashVideo = findViewById(R.id.splashVideo);
        splashIcon = findViewById(R.id.splashIcon);
        splashTitle = findViewById(R.id.splashTitle);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash_animation;
        Uri videoUri = Uri.parse(videoPath);
        splashVideo.setVideoURI(videoUri);

        splashVideo.setOnCompletionListener(mp -> {
            splashVideo.setVisibility(View.GONE);
            splashIcon.setVisibility(View.VISIBLE);
            startIconAnimation();
        });


        splashVideo.start();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, SPLASH_DELAY);
    }

    private void startIconAnimation() {

        ObjectAnimator iconScaleX = ObjectAnimator.ofFloat(splashIcon, "scaleX", 0.3f, 1f);
        ObjectAnimator iconScaleY = ObjectAnimator.ofFloat(splashIcon, "scaleY", 0.3f, 1f);
        ObjectAnimator titleAlpha = ObjectAnimator.ofFloat(splashTitle, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(iconScaleX, iconScaleY, titleAlpha);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (splashVideo != null && splashVideo.isPlaying()) {
            splashVideo.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashVideo != null) {
            splashVideo.stopPlayback();
        }
    }
}