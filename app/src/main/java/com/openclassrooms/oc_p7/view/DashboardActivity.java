package com.openclassrooms.oc_p7.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDashboardBinding;
import com.openclassrooms.oc_p7.databinding.DrawerHeaderBinding;

import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = " DashboardActivity ";
    private NavController drawerNavController;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    SearchView searchView;

    ActivityDashboardBinding activityDashboardBinding;
    DrawerHeaderBinding drawerHeaderBinding;

    /*
        public static final int RESULT_CANCELED    = 0;
     Standard activity result: operation succeeded.
    public static final int RESULT_OK           = -1;
     Start of user-defined activity results.
    public static final int RESULT_FIRST_USER   = 1;
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        drawerHeaderBinding = DrawerHeaderBinding.bind(activityDashboardBinding.drawerNavView.getHeaderView(0));
        setContentView(activityDashboardBinding.getRoot());

        setToolbar();
        setDrawerLayout();
        setHeaderInfos();

    }


    public void setHeaderInfos() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d(TAG, "email " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
            drawerHeaderBinding.sideMenuName.setText(getString(R.string.drawer_header_name, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

            drawerHeaderBinding.sideMenuEmail.setText(getString(R.string.drawer_header_email, FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            Glide.with(this)
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .centerCrop()
                    .into(drawerHeaderBinding.sideMenuPicture);
        } else {
            Log.d(TAG, "User null");
        }
    }

    /**
     * Setting up the toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);// set drawable icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        setUpSearchView(menu);

        return true;

    }

    /**
     * Setting up the searchView for AutoComplete
     */

    public void setUpSearchView(Menu menu) {

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //onSearchCalled();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };

        menu.findItem(R.id.toolbar_search).setOnActionExpandListener(onActionExpandListener);
        searchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        searchView.setQueryHint("Search location...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() >= 3) {
                    Log.d(TAG, "Can start query : " + query);
                    //onSearchCalled();
                } else if (query.length() != 0) {
                    Log.d(TAG, "Query too short " + query);
                } else {
                    Log.d(TAG, "Empty query " + query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "text changed : " + newText);

                return true;
            }
        });
    }

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).setCountry("FR")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

        //add location bias

        Log.d(TAG, "onSearchCalled");


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                toolbar.collapseActionView();
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                toolbar.collapseActionView();
                Log.d(TAG, "Autocomplete canceled");

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activityDashboardBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
            activityDashboardBinding.drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Log.d(TAG, item.getItemId() + "");
                activityDashboardBinding.drawerLayout.open();
                return true;
            case R.id.toolbar_search:
                Log.d(TAG, "SEARCH CHOSEN");
                return true;


            default:
                Log.d(TAG, item.getItemId() + "");
                return super.onOptionsItemSelected(item);
        }
    }


    void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    private void setDrawerLayout() {
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_nav_host_fragment)).getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.drawer_home, R.id.drawer_lunch, R.id.drawer_settings, R.id.drawer_logout).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, activityDashboardBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityDashboardBinding.drawerLayout.addDrawerListener(toggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.drawer_home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_home:
                Log.d(TAG, "Home pressed");
                break;
            case R.id.drawer_lunch:
                Log.d(TAG, "Lunch pressed");
                break;
            case R.id.drawer_settings:
                goToSettings();
                Log.d(TAG, "Setting pressed");
                break;
            case R.id.drawer_logout:
                logOut();
                Log.d(TAG, "Logout pressed");
                break;
        }
        activityDashboardBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        goToLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}