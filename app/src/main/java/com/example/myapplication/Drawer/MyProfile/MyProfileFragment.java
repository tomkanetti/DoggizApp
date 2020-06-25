package com.example.myapplication.Drawer.MyProfile;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(MyProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

        return root;
    }

}