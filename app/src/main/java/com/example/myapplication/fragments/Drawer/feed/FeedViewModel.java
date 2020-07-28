package com.example.myapplication.fragments.Drawer.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;

import java.util.List;

public class FeedViewModel extends ViewModel {
    LiveData<List<Post>> liveData;

    public FeedViewModel() {
    }

    public LiveData<List<Post>> getData() {
        if (liveData == null) {
            liveData = PostModel.instance.getAllPosts();
        }
        return liveData;
    }

    public void refresh(PostModel.CompListener listener) {
        PostModel.instance.refreshPostList(listener);
    }


}