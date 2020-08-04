package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.NavGraphDirections;
import com.example.myapplication.R;
import com.example.myapplication.fragments.Drawer.UserProfile.UserProfileFragment;
import com.example.myapplication.fragments.Drawer.UserProfile.UserProfileFragmentDirections;
import com.example.myapplication.fragments.Drawer.feed.FeedFragment;
import com.example.myapplication.fragments.Drawer.feed.FeedFragmentDirections;
import com.example.myapplication.fragments.Drawer.usersList.UsersListFragment;
import com.example.myapplication.fragments.Drawer.usersList.UsersListFragmentDirections;
import com.example.myapplication.fragments.MainFragment;
import com.example.myapplication.fragments.MainFragmentDirections;
import com.example.myapplication.fragments.PostDetailsFragment;
import com.example.myapplication.fragments.PostDetailsFragmentDirections;
import com.example.myapplication.model.Post;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class  HomeActivity extends AppCompatActivity implements FeedFragment.Delegate, UsersListFragment.Delegate, PostDetailsFragment.Delegate, UserProfileFragment.Delegate {
    NavController navController;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    AppBarLayout appBarLayout;
    TextView dogName,ownerName,email;
    ImageView userImage;
    Boolean isHide=false;
    AppBarConfiguration appBarConfiguration;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        appBarLayout=findViewById(R.id.appBarLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
//        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);

        navController = Navigation.findNavController(this, R.id.home_nav_host);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                getSupportActionBar().setTitle(destination.getLabel());
            }
        });
//        setOnNavControllerDestinationChanged();

        if (user == null) {
            Log.d("TAG", "USER == NULL ?");
            hideAppBar();
        }

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logout();
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.userProfileFragment)
                    Log.d("TAG", "ENTER TO FUNCTIONS- IF");
                    navController.navigate(FeedFragmentDirections.actionGlobalUserProfileFragment(user));
                if(menuItem.getItemId()==R.id.feedFragment)
                    navController.navigate(FeedFragmentDirections.actionGlobalFeedFragment());
                if(menuItem.getItemId()==R.id.usersListFragment)
                    navController.navigate(FeedFragmentDirections.actionGlobalUsersListFragment());
                if(menuItem.getItemId()==R.id.editProfileFragment)
                    navController.navigate(FeedFragmentDirections.actionGlobalEditProfileFragment(user));
                drawer.close();
                return true;
            }
        });
    }

    private void logout() {
        UserModel.instance.logout();
        navController.navigate(MainFragmentDirections.actionGlobalMainFragment());
        drawer.closeDrawers();
        hideAppBar();
    }


//    @Override
//    protected void onStart() {
//        Log.d("TAG", "ON START");
//        super.onStart();
//        hideAppBar();
//    }

    public void hideAppBar() {
        Log.d("TAG","hideAppBar");
        appBarLayout.setExpanded(false,false);
        appBarLayout.setVisibility(View.GONE);
        isHide=true;

    }

    public void updateUI() {
        appBarLayout.setExpanded(true,true);
        appBarLayout.setVisibility(View.VISIBLE);
        Menu menu = navigationView.getMenu();
        menu.setGroupVisible(R.id.group, true);
        isHide=false;

        dogName=navigationView.findViewById(R.id.nav_header_profileName_textView);
        ownerName=navigationView.findViewById(R.id.nav_header_ownerName_textView);
        email=navigationView.findViewById(R.id.nav_header_profileEmail_textView);
        userImage=navigationView.findViewById(R.id.nav_header_profileImage_imageView);

        UserModel.instance.getCurrentUserDetails(new UserModel.Listener<User>() {
            @Override
            public void onComplete(User data) {
                user=data;
                email.setText(data.getEmail());
                dogName.setText(data.dogName);
                ownerName.setText(data.ownerName);

                if (data.imgUrl != null && !data.imgUrl.equals("")) {
                    Picasso.get().load(data.imgUrl).placeholder(R.drawable.f).into(userImage);
                } else {
                    userImage.setImageResource(R.drawable.f);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Post post) {
        navController.navigate(FeedFragmentDirections.actionGlobalPostDetailsFragment(post,user));
    }

    @Override
    public void onItemSelected(User user) {
        navController.navigate(UsersListFragmentDirections.actionGlobalUserProfileFragment(user));
    }

    @Override
    public void onItemSelectedFromPostDetail(Post post) {
        navController.navigate(PostDetailsFragmentDirections.actionGlobalEditPostFragment(post));
    }

    @Override
    public void onItemSelectedFromUserProfile(Post post) {
        navController.navigate(UserProfileFragmentDirections.actionGlobalPostDetailsFragment(post,user));
    }
}