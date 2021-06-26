package com.openclassrooms.oc_p7.views.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.FragmentMapBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.LoginViewModel;
import com.openclassrooms.oc_p7.view_models.MapViewModel;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";


    private MapViewModel mapViewModel;
    private WorkmateViewModel workmateViewModel;

    private ArrayList<RestaurantPojo> nearbyPlaceList = new ArrayList<>();
    private List<String> placeIdList = new ArrayList<>();

    private FragmentMapBinding fragmentMapBinding;
    private MapView mapView;

    private Bundle savedInstanceState;

    private LoginViewModel loginViewModel;

    private GoogleMap googleMap;

    private Boolean shouldReload = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.savedInstanceState = savedInstanceState;

        //initBinding
        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(this.getContext()), null, false);

        initViewModels();
        initMap(savedInstanceState);
        initObservers();
        initListeners();
        workmateViewModel.getWorkmateList();

        return fragmentMapBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkAndRequestPermissions();

    }

    private void initViewModels() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        MapViewModelFactory mapViewModelFactory = Injection.provideMapViewModelFactory(getContext());
        mapViewModel =
                ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel.class);

        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(getContext());
        workmateViewModel =
                ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }


    public void initListeners() {

        fragmentMapBinding.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick locationButton");
                LatLng currentLatLng = new LatLng(mapViewModel.currentLocationLiveData.getValue().getLatitude(), mapViewModel.currentLocationLiveData.getValue().getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
            }
        });

    }

    private void initObservers() {

        mapViewModel.currentLocationLiveData.observe(getViewLifecycleOwner(), location -> {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (googleMap != null) {
                googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("YOU").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
            }
        });

        //TODO DOES NOT HAVE TIME TO LOAD
        /*
        mapViewModel.restaurantLiveData.observe(getViewLifecycleOwner(), restaurantList -> {
            Log.d(TAG, "restaurantLiveData observer : " + restaurantList);
            for (Restaurant restaurant : restaurantList) {
                if (googleMap != null) {
                    LatLng latLng = new LatLng(restaurant.getLat(), restaurant.getLng());
                    if (placeIdList.contains(restaurant.getId()))
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    else
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()));
                }
            }
        });

*/

        mapViewModel.nearbyPlacesLiveData.observe(getViewLifecycleOwner(), placeList -> {
            Log.d(TAG, "placeListLiveData onChanged");
            for (RestaurantPojo place : placeList) {
                if (googleMap != null) {
                    LatLng latLng = new LatLng(place.geometry.location.lat, place.geometry.location.lng);
                    if (placeIdList.contains(place.place_id))
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(place.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    else
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(place.name));
                }
            }
        });


        workmateViewModel.workmatePlaceIdListLiveData.observe(getViewLifecycleOwner(), idList -> {
            placeIdList = idList;
            refreshMap();
        });

    }

    private void initMap(Bundle savedInstanceState) {
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
        this.googleMap = googleMap;
        if (getTheme())
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapstyle));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                /*
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                googleMap.addMarker(markerOptions);
                 */

            }
        });
        refreshMap();

    }


    private void refreshMap() {
        Log.d(TAG, "Refresh Map");
        if (googleMap != null) {
            googleMap.clear();
            getCurrentLocation();
        }
    }


    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "shouldRequestPermissions");
            checkAndRequestPermissions();
        } else {
            mapViewModel.getLocationInformations(getContext());
        }
    }

    private void checkAndRequestPermissions() {
        Log.d(TAG, "FINE LOCATION PERMISSION : " + ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION));
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 19);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Code : " + requestCode + " Permissions : " + Arrays.toString(permissions) + " result :" + Arrays.toString(grantResults));
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            shouldReload = true;
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
        verifyMapState();
        Log.d(TAG, "onResume");
    }

    public void verifyMapState() {
        // if(shouldShowPermissionDialog) Dialog if user denied gps access

        if (shouldReload) {
            Log.d(TAG, "shouldReload");
            //  refreshMap();
            shouldReload = false;
        }
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