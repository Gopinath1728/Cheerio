package com.example.cheerio.ui.dashboard.pain_management.exercises.exerciseData;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cheerio.R;
import com.example.cheerio.adapter.Pain_Knowledge_Adapter;
import com.example.cheerio.common.Common;
import com.example.cheerio.databinding.FragmentExerciseDataBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;


public class ExerciseDataFragment extends Fragment {

    private FragmentExerciseDataBinding binding;

    private ExerciseDataViewModel exerciseDataViewModel;

    private ImageView img_exercise_data_back;
    private TextView txt_exercise_title;
    private RecyclerView recycler_exercise_data;

    private Pain_Knowledge_Adapter adapter;

    private BottomNavigationView nav_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        exerciseDataViewModel = new ViewModelProvider(this).get(ExerciseDataViewModel.class);
        binding = FragmentExerciseDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nav_view = getActivity().findViewById(R.id.nav_view);

        img_exercise_data_back = binding.imgExerciseDataBack;
        txt_exercise_title = binding.txtExerciseTitle;
        recycler_exercise_data = binding.recyclerExerciseData;

        int position = getArguments().getInt("position");

        exerciseDataViewModel.getErrorMessage().observe(getViewLifecycleOwner(),s -> Snackbar.make(root,""+s,Snackbar.LENGTH_SHORT).show());

        exerciseDataViewModel.getMutableDataList(Common.ExerciseClass.get(position).getName()).observe(getViewLifecycleOwner(),data_modelList -> {
            if (data_modelList!=null & data_modelList.size()>0)
            {
                txt_exercise_title.setText(new StringBuilder(Common.ExerciseClass.get(position).getName()));
                adapter = new Pain_Knowledge_Adapter(getContext(),data_modelList);
                recycler_exercise_data.setAdapter(adapter);
            }
        });
        recycler_exercise_data.setHasFixedSize(true);
        recycler_exercise_data.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        recycler_exercise_data.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && nav_view.isShown()) {
                    nav_view.setVisibility(View.GONE);
                } else if (dy < 0 ) {
                    nav_view.setVisibility(View.VISIBLE);

                }
            }
        });


        //click to navigate back
        img_exercise_data_back.setOnClickListener(backClicked);

        return root;
    }

    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
    };
}