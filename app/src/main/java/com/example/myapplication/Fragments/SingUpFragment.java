package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SingUpFragment extends Fragment {

    Button singUpBtn;
    ImageView imageView;
    FloatingActionButton button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    TextView firstNameTv;
    TextView lastNameTv;
    TextView dogNameTv;
    TextView emailTv;
    TextView passwordTv;
    TextView confirmPasswordTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sing_up,container,false);

        imageView = view.findViewById(R.id.singup_profile_image);
        button = view.findViewById(R.id.app_bar_writePost_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



        firstNameTv=view.findViewById(R.id.singup_firstName_text);
        lastNameTv=view.findViewById(R.id.singup_lastName_text);
        dogNameTv=view.findViewById(R.id.singup_dogName_text);
        emailTv=view.findViewById(R.id.singup_email_text);
        passwordTv=view.findViewById(R.id.singup_password_text);
        confirmPasswordTv=view.findViewById(R.id.singup_confirmPassword_text);

        singUpBtn=view.findViewById((R.id.singup_singup_btn));
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
                Navigation.findNavController(v).navigate(R.id.action_singUpFragment_to_postActivity);

            }
        });

        return view;
    }

    private void singUp() {
        Log.d("TAG", firstNameTv+"singUp: ");

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }


}
