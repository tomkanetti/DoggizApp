package com.example.myapplication.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Comment;
import com.example.myapplication.model.CommentModel;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;

import java.util.List;

public class PostDetailsViewModel extends ViewModel {

    LiveData<List<Comment>> liveData;

    public PostDetailsViewModel() {
    }

//    public LiveData<List<Comment>> getData() {
//        if (liveData == null) {
//            liveData = CommentModel.instance.getAllComments();
//        }
//        return liveData;
//    }
//
//    public void refresh(CommentModel.CompListener listener) {
//        CommentModel.instance.refreshCommentList(listener);
//    }


}
