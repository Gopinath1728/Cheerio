package com.example.cheerio.ui.dashboard.goal_setting.AllFragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheerio.callback.Goal_load_listener;
import com.example.cheerio.common.Common;
import com.example.cheerio.models.Goal_Data_Model;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AllGoalViewModel extends ViewModel implements Goal_load_listener {
    private MutableLiveData<List<Goal_Data_Model>> goalMutableLiveData;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Goal_load_listener goalLoadListener;

    public AllGoalViewModel() {
        goalLoadListener=this;
    }

    public MutableLiveData<List<Goal_Data_Model>> getGoalMutableLiveData() {
        if (goalMutableLiveData == null)
        {
            goalMutableLiveData = new MutableLiveData<>();
            errorMessage = new MutableLiveData<>();
            loadData();
        }

        return goalMutableLiveData;
    }

    private void loadData() {

        FirebaseFirestore.getInstance().collection("Users")
                .document(Common.user_model.getUid())
                .collection("Goals")
                .orderBy("date")
                .addSnapshotListener((value, error) -> {
                    if (value!=null){
                        List<Goal_Data_Model> tempList = value.toObjects(Goal_Data_Model.class);
                        if (tempList.size()>0)
                            goalLoadListener.onGoalLoadSuccess(tempList);
                        else
                            goalLoadListener.onGoalLoadFailure("Error fetching data");
                    }


                });
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void onGoalLoadSuccess(List<Goal_Data_Model> goalDataModels) {
        goalMutableLiveData.setValue(goalDataModels);
    }

    @Override
    public void onGoalLoadFailure(String error) {
        errorMessage.setValue(error);
    }
}
