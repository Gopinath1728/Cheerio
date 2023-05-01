package com.example.cheerio.callback;

import com.example.cheerio.models.User_model;

public interface User_load_listener {
    void onUserLoadSuccess(User_model user_model);
    void onUserLoadFailed(String error);
}
