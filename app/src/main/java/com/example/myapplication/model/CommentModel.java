package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.MyApplication;

import java.util.List;

public class CommentModel {
    public static final CommentModel instance = new CommentModel();




    public LiveData<List<Comment>> getAllPostComments(String postId) {
        LiveData<List<Comment>> liveData = AppLocalDb.db.commentDao().getAll(postId);
        refreshPostCommentList(postId, null);
        return liveData;
    }

    public void refreshPostCommentList(String postId,final CompListener listener) {
        long lastUpdated = MyApplication.context.getSharedPreferences("last updated", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdateDate", 0);
        CommentFirebase.getAllPostComments(postId, new Listener<List<Comment>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Comment> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        if (data != null) {
                            for (Comment comment : data) {
                                AppLocalDb.db.commentDao().insertAll(comment);
                                if (comment.getTime() > lastUpdated)
                                    lastUpdated = comment.getTime();
                            }
                            SharedPreferences.Editor editor = MyApplication.context.getSharedPreferences("last updated", Context.MODE_PRIVATE).edit();
                            editor.putLong("PostsLastUpdateDate", lastUpdated).commit();
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null) listener.onComplete();
                    }
                }.execute();
            }
        });
    }




    public interface Listener<T>{
        void onComplete(T data);
    }

    public interface CompListener{
        void onComplete();
    }


    public void addComment(Comment comment, CommentModel.Listener<Boolean> listener){
        CommentFirebase.addComment(comment,listener);
    }
}
