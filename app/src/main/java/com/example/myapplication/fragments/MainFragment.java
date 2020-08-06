package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;

public class MainFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_main,container,false);

        View loginBtn = view.findViewById(R.id.main_login_btn);
        View singUpBtn=view.findViewById(R.id.main_singUp_btn);

        loginBtn.setOnClickListener(Navigation.createNavigateOnClickListener(LoginFragmentDirections.actionGlobalLoginFragment()));
        singUpBtn.setOnClickListener(Navigation.createNavigateOnClickListener(SingUpFragmentDirections.actionGlobalSingUpFragment()));

        return view;
    }

}
