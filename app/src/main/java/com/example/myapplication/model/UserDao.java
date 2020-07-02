package com.example.myapplication.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// class that provides the connection to the data base table
@Dao
public interface UserDao {

    // return all (*) usere from list<User>
    @Query("select * from User")
    List<User> getAll();

    // if i have this User in this ame name -> replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... students);

    @Delete
    void delete(User student);
}
