package com.example.matrixhive.mvpdemo1.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matrixhive.mvpdemo1.R;
import com.example.matrixhive.mvpdemo1.detailsFragment.AccidentDetailsFragment;
import com.example.matrixhive.mvpdemo1.login.LoginActivity;
import com.example.matrixhive.mvpdemo1.profile.ProfileFragment;
import com.example.matrixhive.mvpdemo1.register.RequestModel;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationContract.view {

    private static final String SHARED_PREF_NAME = "demo.db";
    private SharedPreferences preferences;
    private boolean logged;
    private NavigationPresenter presenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        boolean addNewFragment = true;

        if (savedInstanceState != null) {
            addNewFragment = false;
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        if (addNewFragment) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        presenter = new NavigationPresenter(this);
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        logged = preferences.getBoolean("logged", false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                SharedPreferences.Editor editor = preferences.edit();
                if (logged) {
                    editor.clear();
                    editor.apply();
                    presenter.loginIntent();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        presenter.displaySelectedScreen(item.getItemId());
        return true;
    }

    public void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_details:
                fragment = new AccidentDetailsFragment();
                toolbar.setTitle(R.string.accident_details);
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                toolbar.setTitle(R.string.profile);
                break;

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.layoutConstraint, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void loginIntent() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
