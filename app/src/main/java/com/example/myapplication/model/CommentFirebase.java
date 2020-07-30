package com.example.myapplication.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommentFirebase {

    final static String COMMENTS_COLLECTION = "comments";


    public static void addComment(Comment comment, final CommentModel.Listener<Boolean> listener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(COMMENTS_COLLECTION).add(toJson(comment)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        DocumentReference result = task.getResult();
                        if (result != null) {
                        }
                        if (listener != null)
                            listener.onComplete(task.isSuccessful());
                    } else {
                        listener.onComplete(null);
                    }
                }
            });
        }
    }


    private static Map<String,Object> toJson(Comment comment) {
        HashMap<String,Object> json = new HashMap<>();
        json.put("post id", comment.getPostId());
        json.put("author name", comment.getAuthorName());
        json.put("comment", comment.getCommentContent());
        json.put("comment id", comment.getCommentId());
        json.put("author image", comment.getAuthorImg());
        json.put("last update", FieldValue.serverTimestamp());
        return json;
    }

    private static Comment factory (Map<String,Object> json) {
        Comment comment = new Comment();
        comment.setPostId((String)json.get("post id"));
        comment.setAuthorName((String)json.get("author name"));
        comment.setCommentContent((String)json.get("comment"));
        comment.setCommentId((String)json.get("comment id"));
        comment.setAuthorImg((String)json.get("author image"));
        Timestamp timestamp = (Timestamp)json.get("last update");
        if (timestamp != null) {
            comment.setTime(timestamp.toDate().getTime());
        }
        return comment;
    }




    public static void getAllPostComments(String postId, final CommentModel.Listener<List<Comment>> listListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COMMENTS_COLLECTION).whereEqualTo("post id", postId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Comment> comments;
                if (task.isSuccessful()) {
                    comments = new LinkedList<>();
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Comment comment = factory(json);
                            Log.d("TAG", "CommentFirebase -> getAllComents -> onComplete -> comments: "+ comment.getCommentContent());
                            comments.add(comment);
                        }
                    listListener.onComplete(comments);
                } else {
                    throw new RuntimeException(task.getException());
                }
            }
        });
    }
}
