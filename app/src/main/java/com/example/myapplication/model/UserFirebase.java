package com.example.myapplication.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class UserFirebase {
    final static String USER_COLLECTION = "users";

//    public UserFirebase() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//    }

    public static void getAllUsers(final UserModel.Listener<List<User>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> userData = null;
                if (task.isSuccessful()) {
                    userData = new LinkedList<User>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        User user = doc.toObject(User.class);
                        userData.add(user);
                    }
                }
                listener.onComplete(userData);
            }
        });
    }

    public static void addUser(User user, final UserModel.Listener<Boolean> listener) {
        Log.d("TAG","UserFirebase - addUser");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String id = String.valueOf(user.getId());
            db.collection(USER_COLLECTION).document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("TAG","UserFirebase - addUser - onComplete");
                    if (listener != null) {
                        listener.onComplete(task.isSuccessful());
                    }
                }
            });
       // }
    }

    public static void signUp(String email, String password, final UserModel.Listener<String> listener) {
        Log.d("TAG","UserFirebase - signUp");
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG","UserFirebase - signUp - onComplete");
                        String id = getCurrentUserId();
                        listener.onComplete(id);
                    }
                });
    }

    public static String getCurrentUserId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            return firebaseUser.getUid();
        return null;
    }

}

