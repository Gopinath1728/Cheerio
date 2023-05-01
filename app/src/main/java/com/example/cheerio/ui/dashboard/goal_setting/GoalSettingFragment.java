package com.example.cheerio.ui.dashboard.goal_setting;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheerio.R;
import com.example.cheerio.common.Common;
import com.example.cheerio.databinding.FragmentGoalSettingBinding;
import com.example.cheerio.models.Goal_Data_Model;
import com.example.cheerio.ui.dashboard.goal_setting.ActiveFragment.ActiveGoalFragment;
import com.example.cheerio.ui.dashboard.goal_setting.AllFragment.AllGoalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoalSettingFragment extends Fragment {

    private GoalSettingViewModel goalSettingViewModel;

    private ViewPagerAdapter adapter;

    private ImageView img_goal_back;

    private FragmentGoalSettingBinding binding;

    private BottomNavigationView nav_view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        goalSettingViewModel = new ViewModelProvider(this).get(GoalSettingViewModel.class);
        binding = FragmentGoalSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nav_view = getActivity().findViewById(R.id.nav_view);
        nav_view.setVisibility(View.GONE);

        img_goal_back = binding.imgGoalBack;

        //onclick to navigate back
        img_goal_back.setOnClickListener(backClicked);

        //setting up view pager
        setUpViewPager(binding.viewPager);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    tab.setText(adapter.mFragmentTitleList.get(position));
//                tab.setCustomView(R.layout.custom_tab);
                }).attach();

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {

            TextView tv = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_tab, null);

            binding.tabLayout.getTabAt(i).setCustomView(tv);
        }


        return root;
    }

    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
        nav_view.setVisibility(View.VISIBLE);
    };

    private void setUpViewPager(ViewPager2 viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity().getLifecycle());
        adapter.addFragment(new ActiveGoalFragment(), "Active");
        adapter.addFragment(new AllGoalFragment(), "All");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }
}