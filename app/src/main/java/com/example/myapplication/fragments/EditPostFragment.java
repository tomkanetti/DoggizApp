package com.example.myapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.fragments.Drawer.feed.FeedFragmentDirections;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.PostModel;
import com.example.myapplication.model.StoreModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class EditPostFragment extends Fragment {

    private static final int PICK_IMAGE =100 ;
    EditPostViewModel viewModel;
    View view;
    Button savePostBtn, deletePostBtn, editImageBtn;
    TextView postTitle, postDescription;
    ImageView postImg;
    Post post;
    ProgressBar progressBar;

    Bitmap imageBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_post,container,false);

        savePostBtn = view.findViewById(R.id.edit_post_save_Btn);
        deletePostBtn = view.findViewById(R.id.edit_post_delete);
        editImageBtn = view.findViewById(R.id.edit_post_image_Btn);
        postTitle = view.findViewById(R.id.edit_post_title);
        postDescription = view.findViewById(R.id.edit_post_description);
        postImg = view.findViewById(R.id.edit_post_image);
        progressBar = view.findViewById(R.id.edit_post_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        post = EditPostFragmentArgs.fromBundle(getArguments()).getPost();

        if ( post != null ){
            update_Post_display();
        }

        savePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditChanges();
            }
        });

        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                PostModel.instance.deletePost(post, new PostModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        if (data) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Navigation.findNavController(view).navigate(FeedFragmentDirections.actionGlobalFeedFragment());
                        }
                    }
                });
            }
        });


        return view;
    }

    private void update_Post_display() {
        postTitle.setText(post.getTitle());
        postDescription.setText(post.getDescription());
        if (post.getImage() != null && !post.getImage().equals(""))
            Picasso.get().load(post.getImage()).placeholder(R.drawable.f).into(postImg);
        else postImg.setImageResource(R.drawable.f);
    }

    private void saveEditChanges() {
        progressBar.setVisibility(View.VISIBLE);
        post.setTitle(postTitle.getText().toString());
        post.setDescription(postDescription.getText().toString());

        Date d = new Date();
        if( imageBitmap != null ) {
            StoreModel.uploadImage(imageBitmap, "hello" + d.getTime(), new StoreModel.Listener() {
                @Override
                public void onSuccess(String url) {
                    post.setImage(url);
                }
                @Override
                public void onFail() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        PostModel.instance.updatePostChanges(post, new PostModel.CompListener() {
            @Override
            public void onComplete() {
                Navigation.findNavController(view).navigateUp();
            }
        });

    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageBitmap = rotateImage((Bitmap) bitmap);
                postImg.setImageBitmap(imageBitmap);
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
