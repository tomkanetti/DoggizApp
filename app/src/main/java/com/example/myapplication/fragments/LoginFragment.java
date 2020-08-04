package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.HomeActivity;
import com.example.myapplication.fragments.Drawer.feed.FeedFragmentDirections;
import com.example.myapplication.model.UserModel;
import com.google.android.material.snackbar.Snackbar;


public class LoginFragment extends Fragment {

    private static final String AUTHENTICATION_FAILED_MESSAGE = "Authentication failed.";
    static final String INVALID_FORM_MESSAGE = "Form not valid. Please try again.";
    private static final String INVALID_EMAIL_MESSAGE ="Email not valid.";
    private static final String INVALID_PASSWORD_MESSAGE = "Password must be minimum 4 length.";

    Button loginBtn;
    TextView emailTv;
    TextView passwordTv;
    View view;
    Boolean flag = false;
    ProgressBar progressBar;
    HomeActivity activity;
    public LoginFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_login,container,false);
        loginBtn = view.findViewById(R.id.login_login_btn);
        emailTv=view.findViewById(R.id.login_email_text);
        passwordTv=view.findViewById(R.id.login_password_text);
        progressBar = view.findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        activity= (HomeActivity) getActivity();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check the data
                if (validateForm()) {
                    login();
                } else {
                    loginError(INVALID_FORM_MESSAGE);
                }
            }
        });
        return view;
    }

    void login() {
        progressBar.setVisibility(View.VISIBLE);
        UserModel.instance.login(emailTv.getText().toString(), passwordTv.getText().toString(), new UserModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if (data) {
                    Navigation.findNavController(view).navigate(FeedFragmentDirections.actionGlobalFeedFragment());
                    activity.updateUI();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    loginError(AUTHENTICATION_FAILED_MESSAGE);
                }
                flag = data;
            }
        });
    }

    private void loginError(String errorMsg) {
        Snackbar mySnackbar = Snackbar.make(view, errorMsg, Snackbar.LENGTH_LONG);
        mySnackbar.show();
        loginBtn.setClickable(true);
    }

    private boolean validateForm() {
        return checkEmail(emailTv)
                && checkPassword(passwordTv);
    }

    private boolean checkEmail(TextView emailTv) {
        String email = emailTv.getText().toString();
        if (email.contains("@") && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.trim().isEmpty())
            return true;
        emailTv.setError(INVALID_EMAIL_MESSAGE);
        return false;
    }

    private boolean checkPassword(TextView passwordTv) {
        String pwd = passwordTv.getText().toString();
        if (pwd.trim().length() > 3)
            return true;
        passwordTv.setError(INVALID_PASSWORD_MESSAGE);
        return false;
    }
}