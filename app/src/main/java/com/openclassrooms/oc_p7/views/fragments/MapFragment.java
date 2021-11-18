package com.openclassrooms.oc_p7.views.fragments;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentMapBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.services.utils.OnMapQueryEvent;
import com.openclassrooms.oc_p7.services.utils.ReminderBroadcast;
import com.openclassrooms.oc_p7.view_models.LoginViewModel;
import com.openclassrooms.oc_p7.view_models.MapViewModel;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.RequestAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";


    private MapViewModel mapViewModel;
    private WorkmateViewModel workmateViewModel;
    private RequestAdapter adapter;

    private List<Restaurant> restaurantList = new ArrayList<>();

    private FragmentMapBinding fragmentMapBinding;
    private MapView mapView;

    private Bundle savedInstanceState;
    private Bundle bundle;

    private LoginViewModel loginViewModel;

    private LatLng currentLatLng;
    private GoogleMap googleMap;


    private Marker requestedMarker = null;
    private List<String> requestedRestaurantNameList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.savedInstanceState = savedInstanceState;


        //init Binding
        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(this.getContext()), null, false);

        //init EventBus
        EventBus.getDefault().register(this);


        if (getArguments() != null) initBundle();
        initViewModels();
        initMap(savedInstanceState);
        initObservers();
        initListeners();
        initRecyclerView();
        workmateViewModel.getWorkmateList();

        return fragmentMapBinding.getRoot();
    }

    @Subscribe
    public void onMapQueryEvent(OnMapQueryEvent onMapQueryEvent) {
        Log.d(TAG, "onMapQuery Event : " + onMapQueryEvent.getQueryForMap());
        if (onMapQueryEvent.getQueryForMap() != null)
            mapViewModel.getRequestedRestaurants(onMapQueryEvent.getQueryForMap(), currentLatLng);
        else {
            adapter.setData(new ArrayList<>());
        }

    }

    public void initRecyclerView() {
        adapter = new RequestAdapter(new ArrayList<>(), new OnRestaurantClickListener() {
            @Override
            public void onRestaurantClick(Restaurant restaurant) {
                focusToQuery(restaurant);
            }
        });
        fragmentMapBinding.mapRecyclerView.setAdapter(adapter);
        fragmentMapBinding.mapRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentMapBinding.mapRecyclerView.addItemDecoration(new DividerItemDecoration(fragmentMapBinding.getRoot().getContext(), LinearLayoutManager.VERTICAL));

    }


    public void focusToQuery(Restaurant restaurant) {
        adapter.setData(new ArrayList<>());
        if (restaurant.getLat() != null && restaurant.getLng() != 0.0) {
            LatLng latLng = new LatLng(restaurant.getLat(), restaurant.getLng());
            if (requestedMarker != null) {
                requestedMarker.remove();
                requestedMarker = null;

            }
            requestedMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            requestedMarker.setTag(restaurant.getId());
            requestedMarker.showInfoWindow();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }

    }

    private void initBundle() {
        bundle = getArguments();
        LatLng latLng = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestPermissions();

    }


    private void initViewModels() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        MapViewModelFactory mapViewModelFactory = Injection.provideMapViewModelFactory(FirebaseFirestore.getInstance(), getContext(), this);
        mapViewModel =
                ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel.class);

        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(FirebaseFirestore.getInstance(), getContext());
        workmateViewModel =
                ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }

    private void testNotification() {

        NotificationManager notificationManager = (NotificationManager) fragmentMapBinding.getRoot().getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(fragmentMapBinding.getRoot().getContext(), ReminderBroadcast.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*
        intent.putExtra(ReminderBroadcast.RESTAURANT_NAME, "restaurantName");
        intent.putExtra(ReminderBroadcast.RESTAURANT_ADDRESS, "restaurantAddress");
        intent.putExtra(ReminderBroadcast.RESTAURANT_WORKMATES, "list of workmates eating here");
        intent.putExtra(ReminderBroadcast.RESTAURANT_PIC, "https://i.pravatar.cc/150?img=5");


         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ReminderBroadcast.prepareToCreateNotification(fragmentMapBinding.getRoot().getContext());
            }
        }, 5000);


    }


    public void initListeners() {

        fragmentMapBinding.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick locationButton");
                testNotification();
                if (mapViewModel.currentLocationLiveData.getValue() != null) {
                    LatLng currentLatLng = new LatLng(mapViewModel.currentLocationLiveData.getValue().getLatitude(), mapViewModel.currentLocationLiveData.getValue().getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
                }

            }
        });

    }

    private void initObservers() {

        mapViewModel.placeRepositoryErrorCodeMutableLiveData.observe(getViewLifecycleOwner(), errorCode -> {
            if (errorCode == ErrorCode.CONNECTION_ERROR) {
                Toast.makeText(fragmentMapBinding.getRoot().getContext(), getString(R.string.map_data_format_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(fragmentMapBinding.getRoot().getContext(), getString(R.string.map_response_error), Toast.LENGTH_LONG).show();
            }

        });
        mapViewModel.workmateRepositoryErrorCodeMutableLiveData.observe(getViewLifecycleOwner(), errorCode -> {
            if (errorCode == ErrorCode.EXECUTION_EXCEPTION) {
                Toast.makeText(fragmentMapBinding.getRoot().getContext(), getString(R.string.workmate_execution_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(fragmentMapBinding.getRoot().getContext(), getString(R.string.workmate_interrupted_error), Toast.LENGTH_LONG).show();
            }

        });

        mapViewModel.currentLocationLiveData.observe(getViewLifecycleOwner(), location -> {
            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
            if (googleMap != null) {
                googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(getString(R.string.map_here)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();

            }
        });

        mapViewModel.restaurantListLiveData.observe(getViewLifecycleOwner(), restaurantList -> {
            this.restaurantList = restaurantList;
            Log.d(TAG, "observer,  size : " + restaurantList.size());

            workmateViewModel.getWorkmateForRestaurantList(mapViewModel.restaurantListLiveData);

            for (Restaurant restaurant : restaurantList) {
                Log.d(TAG, "observer mapFragment : " + restaurant.toString());
                //getDetails
                if (googleMap != null) {
                    LatLng latLng = new LatLng(restaurant.getLat(), restaurant.getLng());

                    if (restaurant.getAttendees() != null && restaurant.getAttendees().size() >= 1) {
                        Log.d(TAG, "getAttendees != null " + restaurant.getName());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        marker.setTag(restaurant.getId());

                        Log.d(TAG, "a workmate chosed : " + restaurant.getName());
                    } else {
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()));
                        marker.setTag(restaurant.getId());

                    }


                }
            }

        });

        mapViewModel.requestedRestaurantList.observe(getViewLifecycleOwner(), requestedRestaurantList -> {
            if (requestedRestaurantList.isEmpty())
                Toast.makeText(fragmentMapBinding.getRoot().getContext(), getString(R.string.map_not_found), Toast.LENGTH_SHORT).show();

            adapter.setData(requestedRestaurantList);


            /*
            requestedRestaurantNameList.clear();

            for (Restaurant restaurant : requestedRestaurantList) {
                Log.d(TAG, "observer " + restaurant.toString());
                requestedRestaurantNameList.add(restaurant.getName() + " , " + restaurant.getAddress());

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(fragmentMapBinding.getRoot().getContext(), R.layout.dropdown_list, requestedRestaurantNameList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fragmentMapBinding.mapListView.setVisibility(View.VISIBLE);
            fragmentMapBinding.mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "requestedRestaurantListSize onClick : " + position + " size : " + requestedRestaurantList.size());
                    focusToQuery(requestedRestaurantList.get(position));
                    //startDetailsActivity(restaurantList.get(position).getId());
                    fragmentMapBinding.mapListView.setVisibility(View.GONE);


                }
            });
            fragmentMapBinding.mapListView.setClickable(true);
            fragmentMapBinding.mapListView.setAdapter(arrayAdapter);


             */
        });

    }

    private void initMap(Bundle savedInstanceState) {
        //requestedPlace = new MutableLiveData<>();
        mapView = fragmentMapBinding.googleMap;
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
    }

    public Boolean getTheme() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                Log.d(TAG, "NightMode");
                return true;
            case Configuration.UI_MODE_NIGHT_NO:
                Log.d(TAG, "LightMode");
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getTheme())
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapstyle));

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "MAP READY");
        googleMap.clear();
        this.googleMap = googleMap;
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull @NotNull Marker marker) {
                Log.d(TAG, marker.getTitle() + " " + marker.getId() + " " + marker.getSnippet() +
                        " " + marker.getAlpha() + " " + marker.getPosition() + " " + marker.getTag());
                if (marker.getTag() != null)
                    if (marker.getTag() != null)
                        startDetailsActivityForRestaurant(marker.getTag().toString());
                    else {
                        Toast.makeText(fragmentMapBinding.getRoot().getContext(), R.string.map_no_restaurant, Toast.LENGTH_LONG).show();
                    }

            }
        });

        if (getTheme())
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapstyle));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {


            }
        });
        refreshMap();

    }

    private void startDetailsActivityForRestaurant(String restaurantId) {


        Intent intent = new Intent(fragmentMapBinding.getRoot().getContext(), DetailsActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);


        /*
        String chosenRestaurantId = null;

        for (Restaurant restaurant : restaurantList) {

            if (restaurant.getName().equals(title)) {
                chosenRestaurantId = restaurant.getId();

            }
        }
        if (chosenRestaurantId != null) {
            Intent intent = new Intent(fragmentMapBinding.getRoot().getContext(), DetailsActivity.class);
            intent.putExtra("restaurantId", chosenRestaurantId);
            startActivity(intent);
        } else {
            Toast.makeText(fragmentMapBinding.getRoot().getContext(), R.string.map_no_restaurant, Toast.LENGTH_LONG).show();
        }

         */

    }

    private void startDetailsActivity(String restaurantId) {
        Intent intent = new Intent(fragmentMapBinding.getRoot().getContext(), DetailsActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }

    private void refreshMap() {
        Log.d(TAG, "Refresh Map");
        if (googleMap != null) {
            getCurrentLocation();
        }
    }


    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "shouldRequestPermissions");
            requestPermissions();
        } else {
            Log.d(TAG, "shouldNotRequestPermissions");

            mapViewModel.getLocationInformations(getContext());
        }
    }


    private void requestPermissions() {
        Log.d(TAG, "FINE LOCATION PERMISSION : " + ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION));

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 19);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Code : " + requestCode + " Permissions : " + Arrays.toString(permissions) + " result :" + Arrays.toString(grantResults));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //refreshMap();
            //TODO : force refresh on RequestResult
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mapView != null) mapView.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) mapView.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
        Log.d(TAG, "onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {

            EventBus.getDefault().unregister(this);


        }
        if (mapView != null) mapView.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }


}