package com.example.cheerio.ui.dashboard.pain_management.exercises;

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
import com.example.cheerio.adapter.ExerciseAdapter;
import com.example.cheerio.databinding.FragmentExerciseBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ExerciseFragment extends Fragment {

    private ExerciseViewModel exerciseViewModel;

    private FragmentExerciseBinding binding;

    private ImageView img_exercise_back;
    private RecyclerView recycler_exercise;

    private ExerciseAdapter adapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        img_exercise_back = binding.imgExerciseBack;
        recycler_exercise = binding.recyclerExercise;

        exerciseViewModel.getErrorMessage().observe(getViewLifecycleOwner(),s -> Snackbar.make(root,""+s,Snackbar.LENGTH_SHORT).show());

        exerciseViewModel.getMutableDataList().observe(getViewLifecycleOwner(),exerciseDataModelList -> {
            if (exerciseDataModelList != null && exerciseDataModelList.size()>0)
            {
                adapter = new ExerciseAdapter(getContext(),exerciseDataModelList);
                recycler_exercise.setAdapter(adapter);
            }
        });
        recycler_exercise.setHasFixedSize(true);
        recycler_exercise.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        //click to navigate back
        img_exercise_back.setOnClickListener(backClicked);

        return  root;
    }


    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
    };
}