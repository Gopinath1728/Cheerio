package com.example.cheerio.callback;

import com.example.cheerio.models.Pain_data_model;

import java.util.List;

public interface Pain_load_listener {
    void onPainLoadSuccess(List<Pain_data_model> pain_data_modelList);
    void onPainLoadFailed(String error);
}
