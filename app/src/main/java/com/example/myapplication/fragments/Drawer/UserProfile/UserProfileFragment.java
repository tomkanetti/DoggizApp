package com.example.myapplication.fragments.Drawer.UserProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel mViewModel;
    TextView dogName;
    TextView ownerName;
    ImageView userImage;
    View view;
    User user;
    LiveData<User> liveData;
    UserProfileViewModel viewModel;
    User data = new User();

    public UserProfileFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(UserProfileViewModel.class);
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        dogName=view.findViewById(R.id.profile_dogName_text);
        ownerName=view.findViewById(R.id.profile_ownerName_text);
        userImage=view.findViewById(R.id.profile_image);
//        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
//            @Override
//            public void onComplete(User u) {
//                bind(u);
//                user=u;
//            }
//        });
//
        user=UserProfileFragmentArgs.fromBundle((getArguments())).getUser();
        if(user!=null)
            bind(user);

        return view;
    }

    public void bind(User u) {
        dogName.setText(u.dogName);
        ownerName.setText(u.ownerName);
        if (u.imgUrl != null && !u.imgUrl.equals("")) {
            Log.d("TAG", " if - UsersListFragment - bind");
            Picasso.get().load(u.imgUrl).placeholder(R.drawable.f).into(userImage);
        } else {
            Log.d("TAG", " else - UsersListFragment - bind");
            userImage.setImageResource(R.drawable.f);
        }
        dogName.setVisibility(View.VISIBLE);
        ownerName.setVisibility(View.VISIBLE);
        userImage.setVisibility(View.VISIBLE);
    }
}