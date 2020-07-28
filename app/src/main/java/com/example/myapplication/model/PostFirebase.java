package com.example.myapplication.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostFirebase {
    final static String POST_COLLECTION = "posts";



    public static void addPost(final Post post, final PostModel.Listener<Boolean> listener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(POST_COLLECTION).add(toJson(post)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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

    private static Map<String,Object> toJson(Post post) {
        HashMap<String,Object> json = new HashMap<>();
        json.put("post id", FieldValue.serverTimestamp());
        json.put("title", post.getTitle());
        json.put("description", post.getDescription());
        json.put("image", post.getImage());
        json.put("user email", post.getUserEmail());
        json.put("user image", post.getUserImage());
        json.put("last update", FieldValue.serverTimestamp());
        json.put("is delete", post.getDelete());
        return json;
    }

    private static Post factory (Map<String,Object> json) {
        Post post = new Post();
        post.setId((String)json.get("post id"));
        post.setTitle((String)json.get("title"));
        post.setDescription((String)json.get("description"));
        post.setImage((String)json.get("image"));
        post.setUserEmail((String)json.get("user email"));
        post.setDelete((Boolean) json.get("is delete"));
        Timestamp timestamp = (Timestamp)json.get("last update");
        if (timestamp != null) {
            post.setLastUpdate(timestamp.toDate().getTime());
        }
        return post;
    }
}
