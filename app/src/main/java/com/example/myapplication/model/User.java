package com.example.myapplication.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @NonNull
    public String dogName;
    public String ownerName;
    @PrimaryKey
    @NonNull
    public String email;
    public String imgUrl;
    public long lastUpdated;

    public User(User newU){
        this.dogName = newU.dogName;
        this.ownerName = newU.ownerName;
        this.email=newU.email;
        this.imgUrl=newU.imgUrl;
    }
    public User(String ownerName, @NonNull String dogName, @NonNull String email,String password, String imgUrl) {
        this.dogName = dogName;
        this.ownerName = ownerName;
        this.email=email;
        this.imgUrl=imgUrl;
    }

    public User() {
    }

    @NonNull
    public String getDogName() {
        return dogName;
    }

    public void setDogName(@NonNull String dogName) {
        this.dogName = dogName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }



    public String getOwnerName() {
        return ownerName;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public long getLastUpdated() {
        return lastUpdated;
    }


}
