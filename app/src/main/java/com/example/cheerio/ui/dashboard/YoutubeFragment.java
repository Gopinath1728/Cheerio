package com.example.cheerio.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cheerio.R;
import com.example.cheerio.databinding.FragmentYoutubeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class YoutubeFragment extends Fragment {

    private FragmentYoutubeBinding binding;
    private YouTubePlayerView youtube_player;

    ImageView img_pain_manage_back;
    private BottomNavigationView nav_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentYoutubeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nav_view = getActivity().findViewById(R.id.nav_view);
        nav_view.setVisibility(View.GONE);

        String link = getArguments().getString("link");

        youtube_player = binding.youtubePlayer;
        getLifecycle().addObserver(youtube_player);

        img_pain_manage_back = binding.imgPainManageBack;



        youtube_player.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);
            }

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youtube_player.enterFullScreen();
                youTubePlayer.loadVideo(link,0);

            }
        });




//onclick to navigate back
        img_pain_manage_back.setOnClickListener(backClicked);

        return root;
    }

    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
        nav_view.setVisibility(View.VISIBLE);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}