package com.example.myapplication.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.MyApplication;

@Database(entities = {User.class}, version = 8)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
}

// this class is our DataBase
// context provide all the running environment of the app
// we will need it to access files, and enable doing global operations on the app
public class AppLocalDb {

    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}