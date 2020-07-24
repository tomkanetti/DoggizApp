package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class UserModel {
    public static final UserModel instance = new UserModel();

    private UserModel() {
//        for(int i=0; i<10; i++){
//            User u = new User("jessi"+i, "noa"+i, " "+i, null, null, null);
//            addUser(u,null);
//        }
    }

    public void addUser(User user, Listener<Boolean> listener) {
        UserFirebase.addUser(user, listener);
        //AppLocalDb.db.userDao().insertAll(user);
    }

    public interface getAllUsersListener {
        void onComplete(List<User> data);
    }

    public void signUp(String email, String password,Listener<String> listener) {
        UserFirebase.signUp(email,password,listener);

    }

    public void login(String email, String password, Listener<Boolean> listener) {
        UserFirebase.login(email, password, listener);
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    public interface CompListener{
        void onComplete();
    }

    public boolean isLoggedIn() {
        return UserFirebase.isSignedIn();
    }

    public void refreshUserList(final CompListener listener) {
        UserFirebase.getAllUsers(new Listener<List<User>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<User> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for(User u : data) {
                            AppLocalDb.db.userDao().insertAll(u);
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null){
                            listener.onComplete();
                        }

                    }
                }.execute("");
            }
        });
    }

    // local dataBase
    public LiveData<List<User>> getAllUsers() {
//        // MutableLiveData can be update on progress
//        final MutableLiveData<List<User>> ldata = new MutableLiveData<List<User>>();
//        // initiate data
//        ldata.setValue(new LinkedList<User>());
        LiveData<List<User>> liveData = AppLocalDb.db.userDao().getAll();
        refreshUserList(null);
//        UserFirebase.getAllUsers(new Listener<List<User>>() {
//            @Override
//            public void onComplete(List<User> data) {
//                ldata.setValue(data);
//            }
//        });

        return liveData;

//        UserFirebase.getAllUsers(listener);

        // local dataBase
//        @SuppressLint("StaticFieldLeak")
//        AsyncTask<String, String, List<User>> taskA = new AsyncTask<String, String, List<User>>(){
//            @Override
//            protected List<User> doInBackground(String... strings) {
//                return AppLocalDb.db.userDao().getAll();
//            }
//            @Override
//            protected void onPostExecute(List<User> users) {
//                super.onPostExecute(users);
//                listener.onComplete(users);
//            }
//        };
//        taskA.execute();
    }
}
