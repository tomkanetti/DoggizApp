package com.example.myapplication.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CommentDao {

    @Query("select * from Comment")
    LiveData<List<Comment>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment... comments);

    @Delete
    void delete(Comment comment);

}
