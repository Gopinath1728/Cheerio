package com.example.cheerio.ui.dashboard.pain_management.music_therapy;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cheerio.R;
import com.example.cheerio.adapter.Music_Therapy_Adapter;
import com.example.cheerio.databinding.FragmentMusicTherapyBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MusicTherapyFragment extends Fragment {

    private MusicTherapyViewModel musicTherapyViewModel;

    private FragmentMusicTherapyBinding binding;

    private RecyclerView recycler_music;
    private ImageView img_music_back;
    private Music_Therapy_Adapter adapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        musicTherapyViewModel = new ViewModelProvider(this).get(MusicTherapyViewModel.class);
        binding = FragmentMusicTherapyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        img_music_back = binding.imgMusicBack;
        recycler_music = binding.recyclerMusic;

        musicTherapyViewModel.getErrorMessage().observe(getViewLifecycleOwner(),s -> Snackbar.make(root,""+s,Snackbar.LENGTH_SHORT).show());

        musicTherapyViewModel.getMutableDataList().observe(getViewLifecycleOwner(),data_modelList -> {
            if (data_modelList != null && data_modelList.size()>0)
            {
                adapter = new Music_Therapy_Adapter(getContext(),data_modelList);
                recycler_music.setAdapter(adapter);
            }
        });

        recycler_music.setHasFixedSize(true);
        recycler_music.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));



        //click to navigate back
        img_music_back.setOnClickListener(backClicked);


        return root;
    }

    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
    };

}