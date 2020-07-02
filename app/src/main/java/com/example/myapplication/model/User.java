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

    @NotNull
    public String getId() {
        return id;
    }

    public String getDogName() {
        return dogName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public User(String dogName, String ownerName, String id, String email, String password,String imgUrl) {
        this.dogName = dogName;
        this.ownerName = ownerName;
        this.id = id;
        this.email=email;
        this.password=password;
        this.imgUrl=imgUrl;
    }
}
