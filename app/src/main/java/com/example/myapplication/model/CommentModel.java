package com.example.myapplication.model;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CommentModel {
    public static final CommentModel instance = new CommentModel();


    private void refreshCommentsList(Object o) {
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    public interface CompListener{
        void onComplete();
    }

}
