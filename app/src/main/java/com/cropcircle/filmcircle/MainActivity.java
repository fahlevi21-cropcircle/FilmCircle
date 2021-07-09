package com.cropcircle.filmcircle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cropcircle.filmcircle.models.user.User;
import com.cropcircle.filmcircle.ui.auth.AuthActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null && navigationView.getHeaderView(0) != null){
            View headerView = navigationView.getHeaderView(0);
            TextView username = (TextView) headerView.findViewById(R.id.navigation_header_username);
            TextView name = (TextView) headerView.findViewById(R.id.navigation_header_name);
            ImageView logoutBtn = (ImageView) headerView.findViewById(R.id.navigation_header_logout);
            ImageView profileImage = (ImageView) headerView.findViewById(R.id.navigation_header_image);

            PreferenceManager manager = new PreferenceManager(this);
            User user = manager.getUserdata();
            if (user != null && user.getName() != null) {
                username.setText(user.getUsername());
                name.setText(user.getName());
                String imageUrl = Constants.IMG_PROFILE_180 + user.getAvatar().getTmdb().getAvatarPath();
                Glide.with(headerView).load(imageUrl).into(profileImage);
            }

            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manager.destroyPreferences();
                    startActivity(new Intent(MainActivity.this, AuthActivity.class));
                    finish();
                }
            });
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_actors, R.id.nav_collections, R.id.nav_discovers, R.id.nav_genre, R.id.nav_collections_tv)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}