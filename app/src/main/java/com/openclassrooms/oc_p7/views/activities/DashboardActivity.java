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
import com.google.android.libraries.places.api.Places;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDashboardBinding;
import com.openclassrooms.oc_p7.databinding.DrawerHeaderBinding;
import com.openclassrooms.oc_p7.services.firestore_database.UserDatabase;
import com.openclassrooms.oc_p7.services.utils.OnDestinationChangedEvent;
import com.openclassrooms.oc_p7.services.utils.OnMapQueryEvent;
import com.openclassrooms.oc_p7.services.utils.OnRestaurantQueryEvent;
import com.openclassrooms.oc_p7.services.utils.OnWorkmateQueryEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = " DashboardActivity ";

    private SearchView searchView;
    private Toolbar toolbar;

    private int currentFragment;

    private OnRestaurantQueryEvent onRestaurantQueryEvent = new OnRestaurantQueryEvent();
    private OnWorkmateQueryEvent onWorkmateQueryEvent = new OnWorkmateQueryEvent();
    private OnMapQueryEvent onMapQueryEvent = new OnMapQueryEvent();

    private ActivityDashboardBinding activityDashboardBinding;
    private DrawerHeaderBinding drawerHeaderBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        drawerHeaderBinding = DrawerHeaderBinding.bind(activityDashboardBinding.drawerNavView.getHeaderView(0));
        setContentView(activityDashboardBinding.getRoot());
        EventBus.getDefault().register(this);

        initPlaces();
        setToolbar();
        setDrawerLayout();
        setHeaderInfos();

    }

    @Subscribe
    public void onDestinationChangedEvent(OnDestinationChangedEvent onDestinationChangedEvent) {
        invalidateOptionsMenu();
        currentFragment = onDestinationChangedEvent.getDestinationInt();
    }

    private void initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.GOOGLE_MAP_API_KEY_DEV), Locale.FRANCE);
        }

    }


    private void setHeaderInfos() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

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
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query.length() >= 3) {
                            if (currentFragment == 0) {
                                //MAP FRAGMENT
                                onMapQueryEvent.setQueryForMap(query);
                                EventBus.getDefault().post(onMapQueryEvent);

                            } else if (currentFragment == 1) {
                                //LIST FRAGMENT
                                onRestaurantQueryEvent.setQueryForRestaurant(query);
                                EventBus.getDefault().post(onRestaurantQueryEvent);
                            } else {
                                //WORKMATES FRAGMENT
                                onWorkmateQueryEvent.setQueryForWorkmate(query);
                                EventBus.getDefault().post(onWorkmateQueryEvent);
                            }
                        } else if (query.length() != 0) {
                            Toast toast = Toast.makeText(DashboardActivity.this, R.string.toolbar_query_too_short, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 20);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(DashboardActivity.this, R.string.toolbar_query_too_short, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM, 0, 20);
                            toast.show();
                        }

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (currentFragment == 2) {
                            //WORKMATES FRAGMENT
                            onWorkmateQueryEvent.setQueryForWorkmate(newText);
                            EventBus.getDefault().post(onWorkmateQueryEvent);
                        }

                        return true;
                    }
                });

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setQuery(null, false);

                onMapQueryEvent.setQueryForMap(null);
                EventBus.getDefault().post(onMapQueryEvent);

                onRestaurantQueryEvent.setQueryForRestaurant(null);
                EventBus.getDefault().post(onRestaurantQueryEvent);

                return true;
            }

        };

        menu.findItem(R.id.toolbar_search).setOnActionExpandListener(onActionExpandListener);
        searchView = (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        searchView.setQueryHint(getString(R.string.searchview_hint));

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(null, false);

                onMapQueryEvent.setQueryForMap(null);
                EventBus.getDefault().post(onMapQueryEvent);

                onRestaurantQueryEvent.setQueryForRestaurant(null);
                EventBus.getDefault().post(onRestaurantQueryEvent);


            }
        });


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
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, item.getItemId() + "");
            activityDashboardBinding.drawerLayout.open();
            return true;
        }
        Log.d(TAG, item.getItemId() + "");
        return super.onOptionsItemSelected(item);
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
        UserDatabase.getUser(FirebaseAuth.getInstance().getUid())
                .addOnSuccessListener(snapshot -> {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}