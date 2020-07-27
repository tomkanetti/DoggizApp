package com.example.myapplication.fragments.Drawer.MyProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;

public class MyProfileViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    LiveData<User> liveData;

    public MyProfileViewModel() {

    }
    //public LiveData<User> getData( )
//    public LiveData<User> getDataa() {
//        if (liveData == null) {
//            liveData = UserModel.instance.getCurrentUserDetails();
//        }
//        return liveData;
//    }


    public LiveData<String> getText() {
        return mText;
    }
}