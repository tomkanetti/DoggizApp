package com.example.myapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;

import com.example.myapplication.R;
import com.example.myapplication.activities.HomeActivity;
import com.example.myapplication.fragments.Drawer.feed.FeedFragmentDirections;
import com.example.myapplication.model.StoreModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Date;


public class SignUpFragment extends Fragment {
    private static final int PICK_IMAGE = 100;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String INVALID_EMAIL_MESSAGE = "Email is not valid";
    private static final CharSequence INVALID_PASSWORD_MESSAGE = "Password must to be minimum 6 length";
    View view;

    Button singUpBtn;
    ImageView imageView;
    FloatingActionButton button;
    TextView ownerNameTv;
    TextView dogNameTv;
    TextView emailTv;
    TextView passwordTv;
    Bitmap imageBitmap;
    HomeActivity activity;
    ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_sign_up,container,false);

        imageView = view.findViewById(R.id.user_list_userImg);
        button = view.findViewById(R.id.feed_writePost_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        ownerNameTv=view.findViewById(R.id.signup_ownerName_text);
        dogNameTv=view.findViewById(R.id.signup_dogName_text);
        emailTv=view.findViewById(R.id.signup_email_text);
        passwordTv=view.findViewById(R.id.signup_password_text);
        progressBar = view.findViewById(R.id.signup_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        activity = (HomeActivity) getActivity();

        singUpBtn=view.findViewById((R.id.signup_singup_btn));
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm() ) {
                    signUp();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                //imageBitmap = activity.rotateImage((Bitmap) bitmap);
                imageBitmap =rotateImage((Bitmap) bitmap);
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
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
                            Navigation.findNavController(view).navigate(FeedFragmentDirections.actionGlobalFeedFragment());
                            activity.updateUI();
                        }
                    });
                 }
            });
    }

    void signUp() {
        progressBar.setVisibility(View.VISIBLE);
        Date d = new Date();
        if (imageBitmap != null) {
            StoreModel.uploadImage(imageBitmap, "my_photo" + d.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(final String url) {
                    saveUser(url);
                }
                @Override
                public void onFail() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar.make(view, "Fail to sign up", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        } else
            saveUser("");
    }

    private boolean validateForm() {
        return checkEmail(emailTv)
                && checkPassword(passwordTv);
    }

    private boolean checkEmail(TextView emailEt) {
        String email = emailEt.getText().toString();
        if (email.contains("@") && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.trim().isEmpty())
            return true;
        emailEt.setError(INVALID_EMAIL_MESSAGE);
        return false;
    }

    private boolean checkPassword(TextView passwordTv) {
        String pwd = passwordTv.getText().toString();
        if (pwd.length() < 6) {
            passwordTv.setError(INVALID_PASSWORD_MESSAGE);
            return false;
        }
        return true;

    }
}
