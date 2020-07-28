package com.example.myapplication.model;

public class PostModel {
    public static final PostModel instance = new PostModel();

    private PostModel() {}

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
