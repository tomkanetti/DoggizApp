package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    private static final String INVALID_EMAIL_MESSAGE = "Email is not valid";
    private static final CharSequence INVALID_PASSWORD_MESSAGE = "Password must to be minimum 4 length";
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
                //hideKeyboard();
                if(validateForm() ) {
                    signUp();
                    Navigation.findNavController(v).navigate(R.id.action_singUpFragment_to_postActivity2);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void takePhoto() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

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

    private void saveUser(final String imageUrl) {
        final String ownerName = ownerNameTv.getText().toString();
        final String dogName = dogNameTv.getText().toString();
        final String email = emailTv.getText().toString();
        final String password = passwordTv.getText().toString();

        UserModel.instance.signUp(email, password, new UserModel.Listener<String>() {
            @Override
            public void onComplete(String data) {
                    User user = new User(ownerName,dogName,email,password,imageUrl);
                    UserModel.instance.addUser(user, new UserModel.Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                            NavController navCtrl = Navigation.findNavController(view);
                            navCtrl.navigateUp();
                        }
                    });
                 }
            });
    }

    void signUp() {
        Date d = new Date();
        if (imageBitmap != null) {
            StoreModel.uploadImage(imageBitmap, "my_photo" + d.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(final String url) {
                    saveUser(url);
                }
                @Override
                public void onFail() {
                }
            });
        } else
            Log.d("TAG","signUp ELSE");
            saveUser("");
    }

    public void hideKeyboard() {
        if (view != null) {
            FragmentActivity activity = getActivity();
            InputMethodManager inputManager;
            if (activity != null) {
                inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null)
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private boolean validateForm() {
        return checkEmail(emailTv)
//                && checkName(firstNameEt)
//                && checkName(lastNameEt)
                && checkPassword(passwordTv);
    }

    private boolean checkEmail(TextView emailEt) {
        String email = emailEt.getText().toString();
        if (email.contains("@") && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.trim().isEmpty())
            return true;
        emailEt.setError(INVALID_EMAIL_MESSAGE);
        return false;
    }

//    private boolean checkName(EditText nameEt) {
//        String name = nameEt.getText().toString();
//        if (!name.trim().isEmpty() && Character.isUpperCase(name.charAt(0)))
//            return true;
//        nameEt.setError(INVALID_NAME_MESSAGE);
//        return false;
//    }

    private boolean checkPassword(TextView passwordTv) {
        String pwd = passwordTv.getText().toString();
        if (pwd.length() < 3) {
            passwordTv.setError(INVALID_PASSWORD_MESSAGE);
            return false;
        }
        return true;

    }

}
