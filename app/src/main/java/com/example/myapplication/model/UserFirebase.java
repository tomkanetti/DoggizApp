package com.example.myapplication.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;


public class UserFirebase {
    final static String USER_COLLECTION = "users";

//    public UserFirebase() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//    }

    public static void getAllUsers(final  UserModel.Listener<List<User>> listener) {
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).document(user.getId()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

}


//public class ModelFirebase {
//
//    public ModelFirebase() {
//        // Access a Cloud Firestore instance from your Activity
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//
//// Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error adding document", e);
//                    }
//                });
//
//    }
//
//    public interface GetAllUsersListener {
//        void onComplete(List<User> data);
//    }
//
//    public void getAllUsere(GetAllUsersListener listener) {
//
//    }
//}
