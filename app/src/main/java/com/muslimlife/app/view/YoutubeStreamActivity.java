package com.muslimlife.app.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentYoutubeStreamBinding;

import android.os.Bundle;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

public class YoutubeStreamActivity extends AppCompatActivity {

    private FragmentYoutubeStreamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentYoutubeStreamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getLifecycle().addObserver(binding.youtubePlayerView);

        if(getIntent() != null) {
            String youtubeId = getIntent().getStringExtra("youtube_id");

            if(youtubeId != null && !youtubeId.isEmpty()) {
                binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(youtubeId, 0);
                    }
                });
            }
        }
    }
}