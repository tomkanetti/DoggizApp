package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

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

public class PostFirebase {
    final static String POST_COLLECTION = "posts";

    @SuppressLint("StaticFieldLeak")
    public static void addPost(final Post post, final PostModel.Listener<Post> listener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(POST_COLLECTION).add(toJson(post)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull final Task<DocumentReference> task) {
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            if (task != null) {
                                DocumentReference result = task.getResult();
                                if (result != null) {
                                    post.setId(result.getId());
                                    db.collection(POST_COLLECTION).document(result.getId()).update("post id", result.getId());
                                }
                                if (listener != null)
                                    listener.onComplete(post);
                            } else {
                                listener.onComplete(null);
                            }
                            return "";
                        }
                    }.execute();
                }
            });
        }
    }

    private static Map<String,Object> toJson(Post post) {
        HashMap<String,Object> json = new HashMap<>();
        json.put("post id", post.getId());
        json.put("title", post.getTitle());
        json.put("description", post.getDescription());
        json.put("image", post.getImage());
        json.put("user email", post.getUserEmail());
        json.put("user name", post.getUsername());
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
        post.setUserImage((String)json.get("user image"));
        post.setUsername((String)json.get("user name"));
        post.setDelete((Boolean) json.get("is delete"));
        Timestamp timestamp = (Timestamp)json.get("last update");
        if (timestamp != null) {
            post.setLastUpdate(timestamp.toDate().getTime());
        }
        return post;
    }

    public static void getAllPostsSince(long lastUpdated, final PostModel.Listener<List<Post>> listListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(new Date(lastUpdated));
        db.collection(POST_COLLECTION).whereEqualTo("is delete",false).whereGreaterThanOrEqualTo("last update", ts)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> posts = null;
                if (task.isSuccessful()) {
                    posts = new LinkedList<Post>();
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Post post = factory(json);
                            posts.add(post);
                        }
                    }
                }
                listListener.onComplete(posts);
            }
        });
    }

    public static void updatePostChanges(final Post p, final PostModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(p.getId()).set(toJson(p)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(task.isSuccessful());
            }
        });
    }

    public static void deletePost(final Post p, final PostModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(p.getId()).update("is delete",true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(task.isSuccessful());
            }
        });
    }

    public static void getAllMyPosts(String userEmail, final PostModel.Listener<List<Post>> listListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).whereEqualTo("is delete", false).whereEqualTo("user email",userEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> posts = null;
                if (task.isSuccessful()) {
                    posts = new LinkedList<Post>();
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Post post = factory(json);
                            posts.add(post);
                        }
                    }
                }
                listListener.onComplete(posts);
            }
        });
    }
}
