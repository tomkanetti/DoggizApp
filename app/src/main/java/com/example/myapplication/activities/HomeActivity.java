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
import com.example.myapplication.model.Post;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class  HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        appBarLayout=findViewById(R.id.appBarLayout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return true;
            }
        });




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
        //setOnNavControllerDestinationChanged();

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.home_nav_host);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void setOnNavControllerDestinationChanged() {
        if (!isHide) {
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(destination.getLabel());
                        if (destination.getId() != navController.getGraph().getStartDestination()) {
                            actionBar.setDisplayHomeAsUpEnabled(true);
                            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });
                        } else {
                            actionBar.setDisplayHomeAsUpEnabled(false);
                            toggle.syncState();
                            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    drawer.openDrawer(GravityCompat.START);
                                }
                            });
                        }
                    }
                }
            });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        //hideAppBar();
    }

    public void hideAppBar(){
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
                email.setText(data.getEmail());
                dogName.setText(data.dogName);
                ownerName.setText(data.ownerName);

                if (data.imgUrl != null && !data.imgUrl.equals("")) {
                    Log.d("TAG", " if - UsersListFragment - bind");
                    Picasso.get().load(data.imgUrl).placeholder(R.drawable.f).into(userImage);
                } else {
                    Log.d("TAG", " else - UsersListFragment - bind");
                    userImage.setImageResource(R.drawable.f);
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }
}