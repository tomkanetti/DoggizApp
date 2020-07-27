package com.example.myapplication.fragments.Drawer.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;

public class FeedViewModel extends ViewModel {
    LiveData<User> userLiveData;

    private MutableLiveData<String> mText;

    public FeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }


    public LiveData<User> getUserLiveData(String userEmail) {
        if(userLiveData==null)
            userLiveData= UserModel.instance.getUser(userEmail);
        return userLiveData;
    }
    public LiveData<String> getText() {
        return mText;
    }
}