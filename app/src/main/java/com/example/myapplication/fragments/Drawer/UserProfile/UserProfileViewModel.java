package com.example.myapplication.fragments.Drawer.UserProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.User;

import java.util.List;

public class UserProfileViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    LiveData<List<Post>> liveData;

    public UserProfileViewModel() {

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

    public LiveData<List<Post>> getData(User user) {
        if (liveData == null ) {
            liveData = PostModel.instance.getMyPosts(user);
        }
        return liveData;
    }

    public void refresh(User u, PostModel.CompListener listener) {
        PostModel.instance.refreshMyPostList(u, listener);
    }
}