package com.example.cheerio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheerio.R;
import com.example.cheerio.common.Common;
import com.example.cheerio.models.Goal_Data_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ActiveGoalAdapter extends RecyclerView.Adapter<ActiveGoalAdapter.MyViewHolder> {

    Context context;
    List<Goal_Data_Model> goal_data_models;

    public ActiveGoalAdapter(Context context, List<Goal_Data_Model> goal_data_models) {
        this.context = context;
        this.goal_data_models = goal_data_models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActiveGoalAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.goal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_goal.setText(new StringBuilder(goal_data_models.get(position).getGoal()));
        holder.txt_goal_time.setText(new StringBuilder(goal_data_models.get(position).getTime()));
        if (goal_data_models.get(position).getStatus())
            holder.goal_check.setChecked(true);

    }

    @Override
    public int getItemCount() {
        return goal_data_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox goal_check;
        TextView txt_goal,txt_goal_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            goal_check = itemView.findViewById(R.id.goal_check);
            txt_goal = itemView.findViewById(R.id.txt_goal);
            txt_goal_time = itemView.findViewById(R.id.txt_goal_time);
        }
    }
}
