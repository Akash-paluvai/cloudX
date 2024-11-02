package com.example.djj;



import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;


public class SplashVideoHelper {
    private VideoView videoView;
    private MediaPlayer.OnCompletionListener completionListener;

    public SplashVideoHelper(VideoView videoView) {
        this.videoView = videoView;
    }

    /**
     * Prepares and starts the splash video
     * @param context Application context
     * @param videoResId Resource ID of the video
     */
    public void setupVideo(Context context, int videoResId) {
        String videoPath = "android.resource://" + context.getPackageName() + "/" + videoResId;
        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            mp.setVolume(0f, 0f); // Mute the video
            mp.setLooping(false);
        });
    }

    /**
     * Sets completion listener for video
     * @param listener Callback for video completion
     */
    public void setOnVideoCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.completionListener = listener;
        videoView.setOnCompletionListener(listener);
    }

    /**
     * Clean up resources
     */
    public void release() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}