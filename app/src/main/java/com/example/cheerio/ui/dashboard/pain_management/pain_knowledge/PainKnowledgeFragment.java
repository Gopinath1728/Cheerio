package com.example.cheerio.ui.dashboard.pain_management.pain_knowledge;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
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
import com.example.cheerio.adapter.Pain_Knowledge_Adapter;
import com.example.cheerio.databinding.FragmentPainKnowledgeBinding;
import com.example.cheerio.databinding.FragmentPainTrackerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class PainKnowledgeFragment extends Fragment {

    private PainKnowledgeViewModel painKnowledgeViewModel;

    private FragmentPainKnowledgeBinding binding;

    private ImageView img_pain_knowledge_back;
    private RecyclerView recycler_pain_knowledge;

    private Pain_Knowledge_Adapter adapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        painKnowledgeViewModel = new ViewModelProvider(this).get(PainKnowledgeViewModel.class);
        binding = FragmentPainKnowledgeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        img_pain_knowledge_back = binding.imgPainKnowledgeBack;
        recycler_pain_knowledge = binding.recyclerPainKnowledge;




        painKnowledgeViewModel.getErrorMessage().observe(getViewLifecycleOwner(),s -> Snackbar.make(root, "" + s, Snackbar.LENGTH_SHORT).show());

        painKnowledgeViewModel.getMutableDataList().observe(getViewLifecycleOwner(),data_modelList -> {
            if (data_modelList != null && data_modelList.size()>0)
            {
                adapter = new Pain_Knowledge_Adapter(getContext(),data_modelList);
                recycler_pain_knowledge.setAdapter(adapter);
            }
        });
        recycler_pain_knowledge.setHasFixedSize(true);
        recycler_pain_knowledge.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));



        //onclick to navigate back
        img_pain_knowledge_back.setOnClickListener(backClicked);

        return root;
    }


    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
    };

}