package com.example.cheerio.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheerio.R;
import com.example.cheerio.models.Data_model;

import java.util.List;

public class Music_Therapy_Adapter extends RecyclerView.Adapter<Music_Therapy_Adapter.MyViewHolder> {

    Context context;
    List<Data_model> data_modelList;

    public Music_Therapy_Adapter(Context context, List<Data_model> data_modelList) {
        this.context = context;
        this.data_modelList = data_modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Music_Therapy_Adapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.music_data_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_data_name.setText(new StringBuilder(data_modelList.get(position).getName()));
        Bundle bundle = new Bundle();
        bundle.putString("link",data_modelList.get(position).getLink());
        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_youtube_fragment,bundle));
    }

    @Override
    public int getItemCount() {
        return data_modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_data_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_data_name = itemView.findViewById(R.id.txt_data_name);
        }
    }
}
