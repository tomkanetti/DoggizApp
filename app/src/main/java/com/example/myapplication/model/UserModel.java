package com.example.myapplication.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

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

    // THIS IS OTHER THREAD (ASYNC) - ALL DATABASE ACTIONS WILL BE RUN IN THIS THREAD (AND NOT IN THE MAIN ONE)
//    public void getAllUsers(final getAllUsersListener listener) {
//        // params - pass parameters to the function that runs in the second thread (like user id that will be filtered by it)
//        // progress - update main thread as progress
//        // result - pass the result from the second thread to the main thread
//        class MyAsyncTask extends AsyncTask<String,String,String> {
//            List<User> data;
//
//            // run on the second thread
//            @Override
//            protected String doInBackground(String... strings) {
////                for (int i=0; i<12; i++) {
////                    AppLocalDb.db.userDao().insertAll(new User("jessi"+i, "noa"+i,i+"" , null,null,null));
////                }
//                data = AppLocalDb.db.userDao().getAll();
//                return null;
//            }
//
//            // will be run after doInBackground() finish
//            // run on the main thread
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                listener.onComplete(data);
//            }
//        }
//        MyAsyncTask task = new MyAsyncTask();
//        task.execute();
//    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    public interface CompListener{
        void onComplete();
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
