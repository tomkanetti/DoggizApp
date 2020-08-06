package com.example.myapplication.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Comment;
import com.example.myapplication.model.CommentModel;

import java.util.List;

public class PostDetailsViewModel extends ViewModel {

    LiveData<List<Comment>> liveData;

    public PostDetailsViewModel() { }

    public LiveData<List<Comment>> getData(String postId) {
        if (liveData == null) {
            liveData = CommentModel.instance.getAllPostComments(postId);
        }
        return liveData;
    }

    public void refresh(String postId,CommentModel.CompListener listener) {
        CommentModel.instance.refreshPostCommentList(postId,listener);
    }


}
