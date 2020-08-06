package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
                                if(!post.getDelete()){
                                    AppLocalDb.db.postDao().insertAll(post);
                                    if (post.getLastUpdate() > lastUpdated)
                                        lastUpdated = post.getLastUpdate();}
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

    @SuppressLint("StaticFieldLeak")
    public void updatePostChanges(final Post p, final CompListener listener) {
        PostFirebase.updatePostChanges(p, new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if (data) {
                    new AsyncTask<String,String,String>(){
                        @Override
                        protected String doInBackground(String... strings) {
                            AppLocalDb.db.postDao().insertAll(p);
                            return null;
                        }
                    }.execute("");
                }
                listener.onComplete();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void deletePost(final Post p, Listener<Boolean> listener) {
        PostFirebase.deletePost(p, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.postDao().delete(p);
                return "";
            }
        }.execute();

    }

    public LiveData<List<Post>> getMyPosts(User u) {
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getMyPosts(u.getEmail());
        refreshMyPostList(u, null);
        return liveData;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshMyPostList(User u, final CompListener listener) {
        PostFirebase.getAllMyPosts(u.getEmail(), new Listener<List<Post>>() {
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String,String,String>()  {
                    @Override
                    protected String doInBackground(String... strings) {
                        if (data != null) {
                            for (Post p: data) {
                                AppLocalDb.db.postDao().insertAll(p);
                            }
                        }
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null)
                            listener.onComplete();
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

    public void addPost(Post post, Listener<Post> listener) {
        PostFirebase.addPost(post, new Listener<Post>() {
            @Override
            public void onComplete(Post data) {
                AppLocalDb.db.postDao().insertAll(data);
            }
        });
    }

}
