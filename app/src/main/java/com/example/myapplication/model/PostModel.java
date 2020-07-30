package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.MyApplication;

import java.util.List;

public class PostModel {
    public static final PostModel instance = new PostModel();

    private PostModel() {}

    public LiveData<List<Post>> getAllPosts() {
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getAll();
        refreshPostList(null);
        return liveData;
    }


    public void refreshPostList(final CompListener listener) {
        long lastUpdated = MyApplication.context.getSharedPreferences("last updated", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdateDate", 0);
       PostFirebase.getAllPostsSince(lastUpdated, new PostModel.Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        if (data != null) {
                            for (Post post : data) {
                                AppLocalDb.db.postDao().insertAll(post);
                                if (post.getLastUpdate() > lastUpdated)
                                    lastUpdated = post.getLastUpdate();
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

    public void addPost(Post post, Listener<Boolean> listener) {
        PostFirebase.addPost(post,listener);
    }

}
