package com.example.cheerio.ui.profile.my_profile;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheerio.callback.User_load_listener;
import com.example.cheerio.common.Common;
import com.example.cheerio.models.User_model;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MyProfileViewModel extends ViewModel implements User_load_listener {
    private MutableLiveData<User_model> mutableLiveData;
    private MutableLiveData<String> mutableError = new MutableLiveData<>();
    private User_load_listener load_listener;

    public MyProfileViewModel() {
        load_listener = this;
    }

    public MutableLiveData<User_model> getMutableLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableError = new MutableLiveData<>();
            loadUser();
        }
        return mutableLiveData;
    }

    private void loadUser() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(Common.user_model.getUid())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        User_model user_model = value.toObject(User_model.class);
                        if (user_model != null)
                            load_listener.onUserLoadSuccess(user_model);
                        else {
                            assert error != null;
                            load_listener.onUserLoadFailed(error.getMessage());
                        }
                    }
                });
    }

    public MutableLiveData<String> getMutableError() {
        return mutableError;
    }

    @Override
    public void onUserLoadSuccess(User_model user_model) {
        mutableLiveData.setValue(user_model);
    }

    @Override
    public void onUserLoadFailed(String error) {
        mutableError.setValue(error);
    }
}
