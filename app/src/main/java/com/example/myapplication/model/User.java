package com.example.myapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    public Integer id;
    public String dogName;
    public String ownerName;
    public String email;
    public String password;
    public String imgUrl;

    public User(){}

    public User(String ownerName, String dogName, String email, String password, String imgUrl) {
        this.dogName = dogName;
        this.ownerName = ownerName;
        this.email=email;
        this.password=password;
        this.imgUrl=imgUrl;
        this.id = Integer.parseInt(ownerName);
        //this.id = UserFirebase.numOfUsers+1;
    }

    @NotNull
    public Integer getId() {
        return id;
    }

//    public void setId(@NotNull Integer id) {
//        this.id = id;
//    }
}
