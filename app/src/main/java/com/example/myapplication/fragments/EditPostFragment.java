package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.example.myapplication.model.Post;
import com.squareup.picasso.Picasso;

public class EditPostFragment extends Fragment {

    EditPostViewModel viewModel;
    View view;
    Button savePostBtn, deletePostBtn, editImageBtn;
    TextView postTitle, postDescription;
    ImageView postImg;
    Post post;

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

        post = EditPostFragmentArgs.fromBundle(getArguments()).getPost();

        if(post!=null){
            update_Post_display();
        }

        return view;
    }

    private void update_Post_display() {
        postTitle.setText(post.getTitle());
        postDescription.setText(post.getDescription());
        if (post.getImage() != null && !post.getImage().equals(""))
            Picasso.get().load(post.getImage()).placeholder(R.drawable.f).into(postImg);
        else postImg.setImageResource(R.drawable.f);
    }
}
