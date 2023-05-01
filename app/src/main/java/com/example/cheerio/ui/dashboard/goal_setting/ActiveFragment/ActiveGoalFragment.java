package com.example.cheerio.ui.dashboard.goal_setting.ActiveFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheerio.R;
import com.example.cheerio.adapter.ActiveGoalAdapter;
import com.example.cheerio.common.Common;
import com.example.cheerio.databinding.FragmentActiveGoalBinding;
import com.example.cheerio.models.Goal_Data_Model;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActiveGoalFragment extends Fragment {

    private FragmentActiveGoalBinding binding;
    private ActiveGoalViewModel activeGoalViewModel;

    private ImageView img_add_goals;
    private TextView txt_no_active_goals;
    private RecyclerView recycler_active_goals;
    ActiveGoalAdapter adapter;

    String todays_date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activeGoalViewModel = new ViewModelProvider(this).get(ActiveGoalViewModel.class);
        // Inflate the layout for this fragment
        binding = FragmentActiveGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recycler_active_goals = binding.recyclerActiveGoals;
        txt_no_active_goals = binding.txtNoActiveGoals;
        img_add_goals = binding.imgAddGoals;

        img_add_goals.setOnClickListener(add_goals);

        activeGoalViewModel.getErrorMessage().observe(getViewLifecycleOwner(),s -> Snackbar.make(root,""+s,Snackbar.LENGTH_SHORT).show());

        activeGoalViewModel.getGoalMutableLiveData(todays_date).observe(getViewLifecycleOwner(),goalDataModels -> {
            if (goalDataModels != null)
            {
                txt_no_active_goals.setVisibility(View.GONE);
                adapter = new ActiveGoalAdapter(getContext(),goalDataModels);
                recycler_active_goals.setAdapter(adapter);
            }
        });
        recycler_active_goals.setHasFixedSize(true);
        recycler_active_goals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        return root;

    }

    View.OnClickListener add_goals = v -> {
        Goal_Data_Model goal_data_model = new Goal_Data_Model();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.add_goals_dialog, null);
        EditText edt_enter_goals = (EditText)itemView.findViewById(R.id.edt_enter_goals);
        TimePicker goal_time_picker = (TimePicker)itemView.findViewById(R.id.goal_time);
        Button btn_finish_goal = (Button)itemView.findViewById(R.id.btn_finish_goal);
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        int currentHour = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
        int currentMinute = Integer.parseInt(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));

        goal_time_picker.setIs24HourView(true);
        goal_time_picker.setCurrentHour(currentHour);
        goal_time_picker.setCurrentMinute(currentMinute);

        btn_finish_goal.setOnClickListener(v1 -> {

            if (edt_enter_goals.getText().toString().equals("")) {
                edt_enter_goals.setError("Field can't be empty !");
            }
            else {
                goal_data_model.setGoal(edt_enter_goals.getText().toString().trim());
                int selectedHour = goal_time_picker.getHour();
                int selectedMinute = goal_time_picker.getMinute();
                int hourDif = selectedHour-currentHour;
                int minDif = selectedMinute-currentMinute;

                if ((hourDif+minDif)<0)
                    Snackbar.make(v1,"Please select a time in the future.",Snackbar.LENGTH_SHORT).show();
                else {
                    goal_data_model.setTime(""+selectedHour+":"+selectedMinute);
                    goal_data_model.setStatus(false);
                    goal_data_model.setDate(todays_date);

                    FirebaseFirestore.getInstance().collection("Users")
                            .document(Common.user_model.getUid())
                            .collection("Goals")
                            .add(goal_data_model)
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            })
                            .addOnSuccessListener(documentReference -> {
                                dialog.dismiss();
                            });


                }
            }


        });


    };
}