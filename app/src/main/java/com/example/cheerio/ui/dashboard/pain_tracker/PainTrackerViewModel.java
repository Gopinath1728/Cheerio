package com.example.cheerio.ui.dashboard.pain_tracker;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheerio.callback.Pain_load_listener;
import com.example.cheerio.common.Common;
import com.example.cheerio.models.Pain_data_model;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PainTrackerViewModel extends ViewModel implements Pain_load_listener {
    private MutableLiveData<List<Pain_data_model>> painMutableList;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Pain_load_listener pain_load_listener;

    public PainTrackerViewModel() {
        pain_load_listener=this;
    }

    public MutableLiveData<List<Pain_data_model>> getPainMutableList() {
        if (painMutableList == null)
        {
            painMutableList = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadData();
        }
        return painMutableList;
    }

    private void loadData() {

        FirebaseFirestore.getInstance().collection("Users")
                .document(Common.user_model.getUid())
                .collection("Pain Dairy")
                .addSnapshotListener((value, error) -> {
                    if (value!= null)
                    {
                        List<Pain_data_model> tempList = value.toObjects(Pain_data_model.class);
                        if (tempList.size()>0)
                            pain_load_listener.onPainLoadSuccess(tempList);
                        else
                            pain_load_listener.onPainLoadFailed("An error occurred !");
                    }
                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void onPainLoadSuccess(List<Pain_data_model> pain_data_modelList) {
        painMutableList.setValue(pain_data_modelList);
    }

    @Override
    public void onPainLoadFailed(String error) {
        errorMessage.setValue(error);
    }
}