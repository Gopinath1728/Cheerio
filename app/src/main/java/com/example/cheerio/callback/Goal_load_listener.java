package com.example.cheerio.callback;

import com.example.cheerio.models.Goal_Data_Model;

import java.util.List;

public interface Goal_load_listener {
    void onGoalLoadSuccess(List<Goal_Data_Model> goalDataModels);
    void onGoalLoadFailure(String error);
}
