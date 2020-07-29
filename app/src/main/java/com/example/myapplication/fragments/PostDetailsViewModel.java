package com.example.myapplication.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Comment;
import com.example.myapplication.model.CommentModel;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;

import java.util.List;

public class PostDetailsViewModel extends ViewModel {
    LiveData<User> UserLiveData;

    LiveData<List<Comment>> liveData;

    public PostDetailsViewModel() {
    }



//    public LiveData<User> getCurrentUser() {
//        if (UserLiveData == null) {
//            UserLiveData = UserModel.instance.getCurrentUserDetails();
//        }
//        return liveData;
//    }

    public LiveData<List<Comment>> getData(String postId) {
        if (liveData == null) {
            liveData = CommentModel.instance.getAllPostComments(postId);
        }
        return liveData;
    }
//
//    public void refresh(CommentModel.CompListener listener) {
//        CommentModel.instance.refreshCommentList(listener);
//    }


}
