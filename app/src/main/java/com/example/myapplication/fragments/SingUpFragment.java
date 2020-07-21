package com.example.myapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;

import com.example.myapplication.R;
import com.example.myapplication.model.StoreModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class SingUpFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    View view;

    Button singUpBtn;
    ImageView imageView;
    FloatingActionButton button;
    static final int PICK_IMAGE = 1;
    Uri imageUri;
    TextView ownerNameTv;
    TextView dogNameTv;
    TextView emailTv;
    TextView passwordTv;
    Bitmap imageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_sing_up,container,false);

        imageView = view.findViewById(R.id.user_list_userImg);
        button = view.findViewById(R.id.app_bar_writePost_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        ownerNameTv=view.findViewById(R.id.singup_ownerName_text);
        dogNameTv=view.findViewById(R.id.singup_dogName_text);
        emailTv=view.findViewById(R.id.singup_email_text);
        passwordTv=view.findViewById(R.id.singup_password_text);

        singUpBtn=view.findViewById((R.id.singup_singup_btn));
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
                Navigation.findNavController(v).navigate(R.id.action_singUpFragment_to_postActivity);

            }
        });

        return view;
    }

    void takePhoto(){
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //    private Bitmap imageBitmap;
//    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras= data.getExtras();
            imageBitmap = rotateImage((Bitmap) extras.get("data"));
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static Bitmap rotateImage(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    void saveUser(){
        final String ownerName = ownerNameTv.getText().toString();
        final String dogName = dogNameTv.getText().toString();
        final String email = emailTv.getText().toString();
        final String password = passwordTv.getText().toString();

        Date d = new Date();
        StoreModel.uploadImage(imageBitmap, "my_photo" + d.getTime(), new StoreModel.Listener() {
            @Override
            public void onSuccess(String url) {
                Log.d("TAG", "url: " + url);
                User u = new User(ownerName, dogName,email,password,url);
                UserModel.instance.addUser(u, new UserModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        NavController navCtrl = Navigation.findNavController(view);
                        navCtrl.navigateUp();
                    }
                });
            }
//
            @Override
            public void onFail() {
//                progressbr.setVisibility(View.INVISIBLE);
//                Snackbar mySnackbar = Snackbar.make(view,R.string.fail_to_save_student, Snackbar.LENGTH_LONG);
//                mySnackbar.show();
            }
        });
    }



//
//    private void openGallery() {
//        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_IMAGE);
//    }
//
//
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
//            Bundle extras = data.getExtras();
//            imageBitmap = rotateImage((Bitmap)extras.get(""+data));
//            imageView.setImageBitmap(imageBitmap);
////            imageUri = data.getData();
////            imageView.setImageURI(imageUri);
//        }
//    }
//
//    public static Bitmap rotateImage(Bitmap source) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }


}
