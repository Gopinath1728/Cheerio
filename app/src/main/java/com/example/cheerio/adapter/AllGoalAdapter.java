package com.example.cheerio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheerio.R;
import com.example.cheerio.models.Goal_Data_Model;

import java.util.List;

public class AllGoalAdapter extends RecyclerView.Adapter<AllGoalAdapter.MyViewHolder> {

    Context context;
    List<Goal_Data_Model> goal_data_models;

    public AllGoalAdapter(Context context, List<Goal_Data_Model> goal_data_models) {
        this.context = context;
        this.goal_data_models = goal_data_models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllGoalAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.all_goal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_all_goal_date.setText(new StringBuilder(goal_data_models.get(position).getDate()));
        holder.txt_all_goal.setText(new StringBuilder(goal_data_models.get(position).getGoal()));
        if (goal_data_models.get(position).getStatus())
            holder.all_goal_check.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return goal_data_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox all_goal_check;
        TextView txt_all_goal,txt_all_goal_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            all_goal_check = itemView.findViewById(R.id.all_goal_check);
            txt_all_goal = itemView.findViewById(R.id.txt_all_goal);
            txt_all_goal_date = itemView.findViewById(R.id.txt_all_goal_date);
        }
    }
}
