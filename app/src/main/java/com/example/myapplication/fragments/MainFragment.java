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
//    Button loginBtn;
//    Button singUpBtn;

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



//        setContentView(R.layout.activity_main);
//
//        Button loginBtn=findViewById(R.id.main_login_btn);
//        Button singUpBtn=findViewById(R.id.main_singUp_btn);
//
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        singUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SingUpActivity.class);
//                startActivity(intent);
//            }
//        });

}
