package com.example.foodzarella.meal_details;

import android.os.Bundle;
import android.view.View;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoAdapter {
    private YouTubePlayerView youTubePlayerView;
    private boolean isVideoPlaying;

    public VideoAdapter(YouTubePlayerView playerView) {
        this.youTubePlayerView = playerView;
        this.isVideoPlaying = false; // Initially, video is not playing
    }

    public void initializeWithUrl(String videoUrl) {
        if (videoUrl != null && !videoUrl.isEmpty()) {
            String videoId = getVideoIdFromUrl(videoUrl);

            initializeYouTubePlayer(videoId);
        }
    }

    public void pauseVideo() {
        if (isVideoPlaying) {
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.pause());
            isVideoPlaying = false;
        }
    }

    public void resumeVideo() {
        if (!isVideoPlaying) {
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.play());
            isVideoPlaying = true;
        }
    }

    public boolean isVideoPlaying() {
        return isVideoPlaying;
    }

    private void initializeYouTubePlayer(String videoId) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0);
                isVideoPlaying = true;
            }
        });
    }

    private String getVideoIdFromUrl(String videoUrl) {
        return videoUrl.substring(videoUrl.lastIndexOf("=") + 1);
    }
}
