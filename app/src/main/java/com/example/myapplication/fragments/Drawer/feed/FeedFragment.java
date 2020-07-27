package com.example.myapplication.fragments.Drawer.feed;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class FeedFragment extends DialogFragment {

    private FeedViewModel feedViewModel;
    View view;
    LiveData<User> userLiveData;

    Dialog popAddPost ;
    ImageView popupUserImage,popupPostImage,popupAddImageBtn;
    Button popUpShareBtn;
    TextView popupTitle,popupDescription;
    User user;
    private Uri pickedImgUri = null;


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
}