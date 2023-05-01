package com.example.cheerio.ui.dashboard.goal_setting.AllFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cheerio.R;
import com.example.cheerio.adapter.AllGoalAdapter;
import com.example.cheerio.databinding.FragmentAllGoalBinding;
import com.example.cheerio.ui.dashboard.goal_setting.ActiveFragment.ActiveGoalViewModel;
import com.google.android.material.snackbar.Snackbar;

public class AllGoalFragment extends Fragment {

    private FragmentAllGoalBinding binding;
    private AllGoalViewModel allGoalViewModel;

    private AllGoalAdapter adapter;

    RecyclerView recycler_all_goals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        allGoalViewModel = new ViewModelProvider(this).get(AllGoalViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentAllGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recycler_all_goals = binding.recyclerAllGoals;

        allGoalViewModel.getErrorMessage().observe(getViewLifecycleOwner(),s -> Snackbar.make(root,s,Snackbar.LENGTH_SHORT).show());

        allGoalViewModel.getGoalMutableLiveData().observe(getViewLifecycleOwner(),goalDataModels -> {
            if (goalDataModels!=null)
            {
                adapter = new AllGoalAdapter(getContext(),goalDataModels);
                recycler_all_goals.setAdapter(adapter);
            }
        });
        recycler_all_goals.setHasFixedSize(true);
        recycler_all_goals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        return root;
    }
}