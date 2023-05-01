package com.example.cheerio.callback;

import com.example.cheerio.models.Data_model;

import java.util.List;

public interface Data_load_listener {
    void onDataLoadSuccess(List<Data_model> data_modelList);
    void onDataLoadFailed(String error);
}
