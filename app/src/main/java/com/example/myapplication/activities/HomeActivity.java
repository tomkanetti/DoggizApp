package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;

import com.example.myapplication.NavGraphDirections;
import com.example.myapplication.R;
import com.example.myapplication.fragments.Drawer.feed.FeedFragment;
import com.example.myapplication.fragments.Drawer.feed.FeedFragmentDirections;
import com.example.myapplication.model.Post;

public class  HomeActivity extends AppCompatActivity   {
    static NavController navControllerS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navControllerS = Navigation.findNavController(this, R.id.home_nav_host);
//        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }











}