package com.example.myapplication.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Comment implements Serializable {
    @NonNull
    String postId;
    @NonNull
    String authorName;
    @NonNull
    String commentContent;
    @PrimaryKey
    @NonNull
    String commentId;
    String authorImg;
    Long time;

    public Comment() {
    }

    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    @NonNull
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(@NonNull String authorName) {
        this.authorName = authorName;
    }

    @NonNull
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(@NonNull String commentContent) {
        this.commentContent = commentContent;
    }

    @NonNull
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(@NonNull String commentId) {
        this.commentId = commentId;
    }

    public String getAuthorImg() {
        return authorImg;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
