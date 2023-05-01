package com.example.cheerio.callback;


import com.example.cheerio.models.Exercise_Data_Model;

import java.util.List;

public interface Exercise_Data_Listener {
    void onDataLoadSuccess(List<Exercise_Data_Model> exerciseDataModelList);
    void onDataLoadFailed(String error);
}
