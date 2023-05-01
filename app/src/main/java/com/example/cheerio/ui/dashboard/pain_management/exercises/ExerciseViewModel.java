package com.example.cheerio.ui.dashboard.pain_management.exercises;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheerio.callback.Exercise_Data_Listener;
import com.example.cheerio.common.Common;
import com.example.cheerio.models.Data_model;
import com.example.cheerio.models.Exercise_Data_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ExerciseViewModel extends ViewModel implements Exercise_Data_Listener {
    private MutableLiveData<List<Exercise_Data_Model>> mutableDataList;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Exercise_Data_Listener exercise_data_listener;

    public ExerciseViewModel() {
        exercise_data_listener=this;
    }

    public MutableLiveData<List<Exercise_Data_Model>> getMutableDataList() {
        if (mutableDataList == null)
        {
            mutableDataList = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadData();
        }

        return mutableDataList;
    }

    private void loadData() {
        List<Exercise_Data_Model> tempList  = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Exercises")
                .addSnapshotListener((value, error) -> {
                    if (value != null)
                    {
                        tempList.clear();
                        List<Exercise_Data_Model> dataModels = value.toObjects(Exercise_Data_Model.class);
                        tempList.addAll(dataModels);

                        if (tempList.size() > 0) {
                            Common.ExerciseClass = tempList;
                            exercise_data_listener.onDataLoadSuccess(tempList);
                        }
                        else
                            exercise_data_listener.onDataLoadFailed("An Error Occurred");
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void onDataLoadSuccess(List<Exercise_Data_Model> exerciseDataModelList) {
        mutableDataList.setValue(exerciseDataModelList);
    }

    @Override
    public void onDataLoadFailed(String error) {
errorMessage.setValue(error);
    }
}