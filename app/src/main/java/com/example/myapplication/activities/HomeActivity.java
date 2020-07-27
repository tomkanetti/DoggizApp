package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;

import com.example.myapplication.R;
import com.example.myapplication.fragments.Drawer.usersList.UsersListFragment;
import com.example.myapplication.fragments.Drawer.usersList.UsersListFragmentDirections;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        NavController navController = Navigation.findNavController(this, R.id.home_nav_host);
//        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }



}