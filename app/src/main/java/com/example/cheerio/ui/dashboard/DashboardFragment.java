package com.example.cheerio.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cheerio.Home_Activity;
import com.example.cheerio.MainActivity;
import com.example.cheerio.R;
import com.example.cheerio.common.Common;
import com.example.cheerio.databinding.FragmentDashboardBinding;
import com.example.cheerio.models.User_model;
import com.example.cheerio.ui.dashboard.pain_management.PainManagementFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private CardView crd_pain_tracker, crd_pain_management, crd_goal_setting;

    private TextView txt_user_name_dash;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        String uid = user.getUid();

        crd_goal_setting = binding.crdGoalSetting;
        crd_pain_management = binding.crdPainManagement;
        crd_pain_tracker = binding.crdPainTracker;
        txt_user_name_dash = binding.txtUserNameDash;


        crd_goal_setting.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_goal_setting));
        crd_pain_management.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_pain_management));
        crd_pain_tracker.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_pain_tracker));

        dashboardViewModel.getMutableError().observe(getViewLifecycleOwner(), s -> {
            Snackbar.make(root, s, Snackbar.LENGTH_SHORT).show();
        });

        dashboardViewModel.getMutableLiveData(uid).observe(getViewLifecycleOwner(), user_model -> {
            if (user_model != null) {
                binding.txtUserNameDash.setText(new StringBuilder(user_model.getName()));
                if (user_model.getImage() != null)
                    Glide.with(getActivity()).load(Common.user_model.getImage()).into(binding.imgUserImage);
                Common.user_model = user_model;
            }

        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}