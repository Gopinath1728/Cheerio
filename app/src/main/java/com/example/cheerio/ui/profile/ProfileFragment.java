package com.example.cheerio.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cheerio.Home_Activity;
import com.example.cheerio.MainActivity;
import com.example.cheerio.R;
import com.example.cheerio.common.Common;
import com.example.cheerio.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (Common.user_model.getImage() != null){
            Glide.with(getActivity()).load(Common.user_model.getImage()).into(binding.imgUser);
        }


        binding.userLogout.setOnClickListener(logout_clicked);

        binding.btnMyProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_my_profile));


        return root;
    }

    View.OnClickListener logout_clicked = v -> {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    };



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}