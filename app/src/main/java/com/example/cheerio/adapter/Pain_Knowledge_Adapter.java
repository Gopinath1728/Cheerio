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

public class Pain_Knowledge_Adapter extends RecyclerView.Adapter<Pain_Knowledge_Adapter.MyViewHolder> {

    Context context;
    List<Data_model> data_modelList;

    public Pain_Knowledge_Adapter(Context context, List<Data_model> data_modelList) {
        this.context = context;
        this.data_modelList = data_modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Pain_Knowledge_Adapter.MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.data_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.data_name.setText(new StringBuilder(data_modelList.get(position).getName()));
        Bundle bundle = new Bundle();
        bundle.putString("link",data_modelList.get(position).getLink());
        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_youtube_fragment,bundle));
    }

    @Override
    public int getItemCount() {
        return data_modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView data_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            data_name = itemView.findViewById(R.id.txt_data_name);
        }
    }
}
