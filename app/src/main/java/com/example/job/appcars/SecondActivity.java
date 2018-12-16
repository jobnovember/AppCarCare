package com.example.job.appcars;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e20b3d")));
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.nav_view);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
               R.string.drawer_open,
               R.string.drawer_close
        ){};
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //fetch user data
        //FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if(mFirebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                onNavMenuClick(menuItem);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        replaceFragment(new Profile());

    }

    private void onNavMenuClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.nav_profile) {
            Profile fragment = new Profile();
            replaceFragment(fragment);
        } else if(id == R.id.nav_booking) {
            Booking fragment = new Booking();
            replaceFragment(fragment);
        } else if(id == R.id.nav_notify) {
            Notify fragment = new Notify();
            replaceFragment(fragment);
        } else if(id == R.id.nav_logout) {
            mFirebaseAuth.signOut();
            finish();
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
        } else {

        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
