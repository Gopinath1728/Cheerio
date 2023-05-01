package com.example.cheerio.ui.dashboard.pain_management;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cheerio.R;
import com.example.cheerio.databinding.FragmentPainManagementBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PainManagementFragment extends Fragment {

    ImageView img_pain_manage_back;
    CardView crd_pain_knowledge, crd_exercises, crd_music_therapy;


    private FragmentPainManagementBinding binding;

    private BottomNavigationView nav_view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPainManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nav_view = getActivity().findViewById(R.id.nav_view);
        nav_view.setVisibility(View.GONE);

        img_pain_manage_back = binding.imgPainManageBack;
        crd_exercises = binding.crdExercises;
        crd_music_therapy = binding.crdMusicTherapy;
        crd_pain_knowledge = binding.crdPainKnowledge;

        //navigate to fragments
        crd_pain_knowledge.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_pain_knowledge));
        crd_exercises.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_exercise));
        crd_music_therapy.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_music));

        //onclick to navigate back
        img_pain_manage_back.setOnClickListener(backClicked);

        return root;
    }

    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
        nav_view.setVisibility(View.VISIBLE);
    };

}