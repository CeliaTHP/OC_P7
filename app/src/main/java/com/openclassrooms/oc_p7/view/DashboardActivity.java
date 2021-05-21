package com.openclassrooms.oc_p7.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.oc_p7.R;

import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = " DashboardActivity ";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBar toolbar;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;
    SearchView searchView;
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
        setContentView(R.layout.activity_dashboard);
        setNavigation();

        setToolbar();
        //initAutoCompleteSupportFragment();

        //  configureDrawerLayout();
        // configureNavigationView();
    }


    /**
     * Setting up the toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar = getSupportActionBar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    /*
    public void initAutoCompleteSupportFragment() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.d(TAG, "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });

    }

     */
    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields).setCountry("FR")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

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
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.side_menu_lunch:
                Log.d(TAG, "Nav Lunch");
            case R.id.side_menu_settings:
                Log.d(TAG, "Nav Settings");
            case R.id.side_menu_logout:
                Log.d(TAG, "Nav Logout");

        }
        if (drawerLayout.isDrawerOpen(drawerLayout))
            drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    private void configureNavigationView() {
        // navigationView = (NavigationView) findViewById(R.id.drawer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "HOME CHOSEN");
                return true;
            case R.id.toolbar_search:
                Log.d(TAG, "SEARCH CHOSEN");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayShowHomeEnabled(true);

         */
    }


    void setNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }

}