package com.example.myapplication.fragments.Drawer.feed;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Dao;


import com.example.myapplication.R;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.StoreModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class FeedFragment extends DialogFragment {
    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FeedViewModel feedViewModel;
    View view;
    LiveData<User> userLiveData;

    Dialog popAddPost ;
    ImageView popupUserImage, popupAddImageBtn,  popupPostImage;
    Button popUpShareBtn;
    TextView popupTitle,popupDescription;
    User user;


    private Bitmap pickedImgBit = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_feed,container,false);

        iniPopup();

        FloatingActionButton writePost = (FloatingActionButton) view.findViewById(R.id.feed_writePost_btn);
        writePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });

        return view;
    }

    private void iniPopup() {

        popAddPost = new Dialog(getActivity());
        popAddPost.setContentView(R.layout.popup_share_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.sharePost_user_image);
        popupPostImage = popAddPost.findViewById(R.id.sharePost_img_imgV);
        popupTitle = popAddPost.findViewById(R.id.sharePost_title_txt);
        popupDescription = popAddPost.findViewById(R.id.sharePost_description_txt);
        popupAddImageBtn = popAddPost.findViewById(R.id.sharePost_addImg_B);
        popUpShareBtn = popAddPost.findViewById(R.id.sharePost_share_btn);

        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User u) {
                user=u;
                if (user != null){
                    if (user.imgUrl != null && !user.imgUrl.equals("")) {
                        Log.d("TAG", " if - UsersListFragment - bind");
                        Picasso.get().load(user.imgUrl).placeholder(R.drawable.f).into(popupUserImage);
                    } else {
                        Log.d("TAG", " else - UsersListFragment - bind");
                        popupUserImage.setImageResource(R.drawable.f);
                    }
                }
            }
        });

        popupAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        popUpShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePost();
                popAddPost.dismiss();
            }
        });

    }

    public void sharePost() {
        Date d = new Date();
        if (popupPostImage != null) {
            StoreModel.uploadImage(pickedImgBit, "post_image" + d.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(String url) {
                    savePost(url);
                }

                @Override
                public void onFail() {

                }
            });
        }
    }


    public void savePost(final String imageUrl) {
        final String title = popupTitle.getText().toString();
        final String description = popupDescription.getText().toString();

        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                Post post = new Post();

                post.setUserEmail(data.getEmail());
                post.setUserImage(data.getImgUrl());
                post.setTitle(title);
                post.setDescription(description);
                post.setImage(imageUrl);
                post.setDelete(false);
//                post.setId(post.getLastUpdate().toString());
                PostModel.instance.addPost(post, new PostModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        NavController navController = Navigation.findNavController(view);
                        navController.navigateUp();
                    }
                });
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                pickedImgBit =rotateImage((Bitmap) bitmap);
                popupPostImage.setImageBitmap(pickedImgBit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}