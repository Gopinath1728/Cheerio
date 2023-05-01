package com.example.cheerio.ui.dashboard.pain_management.music_therapy;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheerio.callback.Data_load_listener;
import com.example.cheerio.models.Data_model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MusicTherapyViewModel extends ViewModel implements Data_load_listener {
    private MutableLiveData<List<Data_model>> mutableDataList;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Data_load_listener data_load_listener;

    public MusicTherapyViewModel() {
        data_load_listener=this;
    }

    public MutableLiveData<List<Data_model>> getMutableDataList() {
        if (mutableDataList == null)
        {
            mutableDataList = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadData();
        }
        return mutableDataList;
    }

    private void loadData() {
        List<Data_model> tempList  = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Music Therapy")
                .addSnapshotListener((value, error) -> {
                    if (value != null)
                    {
                        tempList.clear();
                        List<Data_model> dataModels = value.toObjects(Data_model.class);
                        tempList.addAll(dataModels);

                        if (tempList.size() > 0)
                            data_load_listener.onDataLoadSuccess(tempList);
                        else
                            data_load_listener.onDataLoadFailed("An Error Occurred");
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void onDataLoadSuccess(List<Data_model> data_modelList) {
        mutableDataList.setValue(data_modelList);
    }

    @Override
    public void onDataLoadFailed(String error) {
        errorMessage.setValue(error);
    }
}