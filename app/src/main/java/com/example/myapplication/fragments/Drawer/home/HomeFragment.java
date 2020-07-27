package com.example.myapplication.fragments.Drawer.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View view;
    NavController navController;
    TextView dogName;
    TextView email;
    ImageView userImage;
    User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        view= inflater.inflate(R.layout.fragment_home,container,false);

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        View view2= inflater.inflate(R.layout.nav_header_main,container,false);

        dogName=view2.findViewById(R.id.nav_header_profileName_textView);
        email=view2.findViewById(R.id.nav_header_profileEmail_textView);
        userImage=view2.findViewById(R.id.nav_header_profileImage_imageView);

        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User u) {
                user= new User(u);
                bind(u);
            }
        });
        return view;


    }


    public void bind(User u) {
        if(u!=null) {
            dogName.setText(user.dogName);
            email.setText(user.email);
            if (user.imgUrl != null && !user.imgUrl.equals("")) {
                Log.d("TAG", " if - UsersListFragment - bind");
                Picasso.get().load(user.imgUrl).placeholder(R.drawable.f).into(userImage);
            } else {
                Log.d("TAG", " else - UsersListFragment - bind");
                userImage.setImageResource(R.drawable.f);
            }

        }
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