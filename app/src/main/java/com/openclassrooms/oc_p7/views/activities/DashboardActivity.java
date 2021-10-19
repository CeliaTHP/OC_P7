package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDashboardBinding;
import com.openclassrooms.oc_p7.databinding.DrawerHeaderBinding;
import com.openclassrooms.oc_p7.services.firestore_helpers.UserHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = " DashboardActivity ";
    private NavController drawerNavController;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private SearchView searchView;
    private int fragmentNum;

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

        initPlaces();
        setToolbar();
        setDrawerLayout();
        setHeaderInfos();

    }

    public void updateFragmentNum(int fragmentNum) {
        this.fragmentNum = fragmentNum;

    }

    private void initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.GOOGLE_MAP_API_KEY_DEV), Locale.FRANCE);
        }

    }


    private void setHeaderInfos() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d(TAG, "email " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
            if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
                drawerHeaderBinding.sideMenuName.setVisibility(View.VISIBLE);
                drawerHeaderBinding.sideMenuName.setText(getString(R.string.drawer_header_name, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

            }
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                drawerHeaderBinding.sideMenuEmail.setVisibility(View.VISIBLE);
                drawerHeaderBinding.sideMenuEmail.setText(getString(R.string.drawer_header_email, FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            }
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        .centerCrop()
                        .into(drawerHeaderBinding.sideMenuPicture);
            }
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


    private void getCurrentFragment() {

    }


    /**
     * Setting up the searchView for AutoComplete
     */

    public void setUpSearchView(Menu menu) {

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                getCurrentFragment();
                onSearchCalled();
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
                } else if (query.length() != 0) {
                    Toast toast = Toast.makeText(DashboardActivity.this, R.string.toolbar_query_too_short, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                    Log.d(TAG, "Query too short " + query);
                } else {
                    Toast toast = Toast.makeText(DashboardActivity.this, R.string.toolbar_query_too_short, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
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


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Log.d(TAG, "onSuggestionSelected : ");
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Log.d(TAG, "onSuggestionClick : ");
                return false;
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
                //mapViewModel.focusMap(place);
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
                break;
            case R.id.drawer_lunch:
                goToLunch();
                break;
            case R.id.drawer_settings:
                goToSettings();
                break;
            case R.id.drawer_logout:
                logOut();
                break;
        }
        activityDashboardBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToSettings() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
        }
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

    private void goToLunch() {
        UserHelper.getUser(FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(snapshot -> {
                    Log.d(TAG, "onSuccess");
                    if (snapshot.get("restaurantId") != null) {
                        Intent intent = new Intent(this, DetailsActivity.class);
                        intent.putExtra("restaurantId", snapshot.get("restaurantId").toString());
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, getString(R.string.drawer_no_lunch), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setToolbar();
        setDrawerLayout();
    }


}