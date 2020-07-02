package com.example.myapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey
    @NotNull
    public String id;
    public String dogName;
    public String ownerName;
    public String email;
    public String password;
    public String imgUrl;

    public User(String dogName, String ownerName, String id, String email, String password,String imgUrl) {
        this.dogName = dogName;
        this.ownerName = ownerName;
        this.id = id;
        this.email=email;
        this.password=password;
        this.imgUrl=imgUrl;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }
}
