package com.example.myapplication.Model;

import java.util.LinkedList;
import java.util.List;

public class Model {

    List<User> userLst = new LinkedList<>();

    public static final Model instance = new Model();

    private Model(){
        for(int i=0; i<10; i++){
            User u = new User("jessi"+i, "noa"+i);
            userLst.add(u);
        }
    }

    public List<User> getUserLst() {
        return userLst;
    }
}
