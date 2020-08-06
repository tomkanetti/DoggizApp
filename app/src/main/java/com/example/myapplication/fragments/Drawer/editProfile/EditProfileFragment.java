package com.example.myapplication.fragments.Drawer.editProfile;

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

import com.example.myapplication.R;

public class EditProfileFragment extends Fragment {
    View view;
    TextView ownerName, dogName;
    ImageView profileImg;
    Button saveChanges;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile,container,false);

        ownerName = view.findViewById(R.id.edit_profile_owner_name);
        dogName = view.findViewById(R.id.edit_profile_dog_name);
        profileImg = view.findViewById(R.id.edit_profile_img);



        return view;
    }
}
