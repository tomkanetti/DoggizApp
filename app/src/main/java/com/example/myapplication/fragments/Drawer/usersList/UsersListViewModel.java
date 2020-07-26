package com.example.myapplication.fragments.Drawer.usersList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;

import java.util.List;

public class UsersListViewModel extends ViewModel {
    LiveData<List<User>> liveData;

    public LiveData<List<User>> getData() {
        if (liveData == null) {
            liveData = UserModel.instance.getAllUsers();
        }
        return liveData;
    }

    public void refresh(UserModel.CompListener listener) {
        UserModel.instance.refreshUserList(listener);
    }
}
