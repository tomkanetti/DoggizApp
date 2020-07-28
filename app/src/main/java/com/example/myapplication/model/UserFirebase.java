package com.example.myapplication.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class UserFirebase {
    final static String USER_COLLECTION = "users";

    public static void getAllUsers(final UserModel.Listener<List<User>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> userData = null;
                if (task.isSuccessful()) {
                    //userData = new LinkedList<User>();

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        User user = doc.toObject(User.class);
                        userData.add(user);
                    }
                }
                listener.onComplete(userData);
            }
        });
    }
    public static void getAllUsersSince(long since, final UserModel.Listener<List<User>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(new Date(since));
        db.collection(USER_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> users = null;
                if (task.isSuccessful()) {
                    users = new LinkedList<User>();
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            User user = factory(json);
                            users.add(user);
                        }
                    }
                }
                listener.onComplete(users);
                Log.d("TAG","refresh " + users.size());
            }
        });
    }

    public static void addUser(User user, final UserModel.Listener<Boolean> listener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(USER_COLLECTION).document(firebaseUser.getUid()).set(toJson(user)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (listener != null) {
                        listener.onComplete(task.isSuccessful());
                    }
                }
            });
        }
    }


    private static Map<String, Object> toJson(User user) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("dog name", user.getDogName());
        json.put("owner name", user.getOwnerName());
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("imgUrl", user.getImgUrl());
        json.put("lastUpdated", FieldValue.serverTimestamp());
        return json;
    }

    private static User factory( Map<String, Object> json) {
        User user = new User();
        user.setDogName((String) json.get("dog name"));
        user.setOwnerName((String) json.get("owner name"));
        user.setEmail((String) json.get("email"));
        user.setPassword((String) json.get("password"));
        user.setImgUrl((String) json.get("imgUrl"));
        Timestamp timestamp = (Timestamp) json.get("lastUpdated");
        if (timestamp != null) user.setLastUpdated(timestamp.toDate().getTime());
        return user;
    }

    public static void signUp(String email, String password, final UserModel.Listener<String> listener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String id = getCurrentUserId();
                        listener.onComplete(id);
                    }
                });
    }

    public static void login(String email, String password, final UserModel.Listener<Boolean> listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                        }
                        listener.onComplete(task.isSuccessful());
                    }
                });
    }

    public static String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            return firebaseUser.getUid();
        return null;
    }

    public static boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void getCurrentUserDetails(final UserModel.Listener<User> listener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            final String id = firebaseUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(USER_COLLECTION).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot taskResult = task.getResult();
                    if (taskResult != null) {
                        Map<String, Object> data = taskResult.getData();
                        if (data != null) {
                            listener.onComplete(factory(data));
                        } else {
                            listener.onComplete(null);
                        }
                    }
                }
            });
        }
    }

    public static void getUserByEmail(final String UserEmail, final UserModel.Listener<User> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).document(UserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();
                if (res != null && res.getData()!=null)
                    listener.onComplete(factory(res.getData()));
            }
        });
    }

    public static String getCurrentUserEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            return firebaseUser.getEmail();
        return null;
    }
}

