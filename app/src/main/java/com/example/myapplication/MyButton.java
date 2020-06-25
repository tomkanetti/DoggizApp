package com.example.myapplication;

import android.view.View;

interface MyButtonListener{
    void onClick(View v);
}


public class MyButton{
    private MyButtonListener listener;

    public MyButton(View viewById) {
    }

    void setOnClickListener(MyButtonListener listener){
        this.listener=listener;
    }

    void onClick(View v){
        listener.onClick(v);
    }




}













