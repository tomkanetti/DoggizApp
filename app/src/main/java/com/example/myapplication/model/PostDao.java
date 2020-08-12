package com.example.myapplication.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.myapplication.model.Post;

import java.util.List;

@Dao
public interface PostDao {

    // return all (*) usere from list<User>
    @Query("select * from Posts WHERE isDelete not like 1")
    LiveData<List<Post>> getAll();

    // if i have this User in this ame name -> replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);

    @Query("select * from Posts WHERE userEmail LIKE :uEmail AND isDelete not like 1")
    LiveData<List<Post>> getMyPosts(String uEmail);


}
