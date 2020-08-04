package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapplication.MyApplication;

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

    public void logout() {
        UserFirebase.logout();

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
        return UserFirebase.isLoggedIn();
    }


    public void refreshUserList(final CompListener listener){
        long lastUpdated = MyApplication.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE)
                .getLong("ReportsLastUpdateDate", 0);
        UserFirebase.getAllUsersSince(lastUpdated, new Listener<List<User>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<User> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        if (data != null) {
                            for (User user : data) {
                                AppLocalDb.db.userDao().insertAll(user);
                                if (user.getLastUpdated() > lastUpdated)
                                    lastUpdated = user.getLastUpdated();
                            }
                            SharedPreferences.Editor editor = MyApplication.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE).edit();
                            editor.putLong("ReportsLastUpdateDate", lastUpdated).commit();
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

    public String getCurrentUserEmail() {
        return UserFirebase.getCurrentUserEmail();
    }

//    public LiveData<User> getCurrentUserDetails() {
//        LiveData<User> liveData = AppLocalDb.db.userDao().getAll();
//        getCurrentUserDetails(null);
//        return liveData;
//    }




    // local dataBase
    public LiveData<List<User>> getAllUsers() {
        LiveData<List<User>> liveData = AppLocalDb.db.userDao().getAll();
        refreshUserList(null);
        return liveData;
    }
    public void getCurrentUserDetails(Listener<User> listener) {
        UserFirebase.getCurrentUserDetails(listener);
    }

    //TODO - FIX
    public LiveData<User> getUser(String userEmail) {
        LiveData<User> userLiveData = AppLocalDb.db.userDao().getUser(userEmail);
        refreshUserDetails(userEmail);
        return userLiveData;
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshUserDetails(String spotName) {
        UserFirebase.getUserByEmail(spotName, new Listener<User>() {
            @Override
            public void onComplete(final User data) {
                if (data != null) {
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            AppLocalDb.db.userDao().insertAll(data);
                            return null;
                        }
                    }.execute();
                }
            }
        });
    }

}
