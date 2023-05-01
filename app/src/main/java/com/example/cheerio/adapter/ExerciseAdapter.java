package com.example.cheerio.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheerio.R;
import com.example.cheerio.models.Exercise_Data_Model;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {

    Context context;
    List<Exercise_Data_Model> exerciseDataModelList;

    public ExerciseAdapter(Context context, List<Exercise_Data_Model> exerciseDataModelList) {
        this.context = context;
        this.exerciseDataModelList = exerciseDataModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseAdapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.exercise_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_exercise_name.setText(new StringBuilder(exerciseDataModelList.get(position).getName()));
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        holder.crd_exercise_item.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_exercise_data,bundle));
    }

    @Override
    public int getItemCount() {
        return exerciseDataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView crd_exercise_item;
        TextView txt_exercise_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_exercise_name = itemView.findViewById(R.id.txt_exercise_name);
            crd_exercise_item = itemView.findViewById(R.id.crd_exercise_item);
        }
    }
}
