package com.openclassrooms.oc_p7.view.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.openclassrooms.oc_p7.databinding.FragmentMapBinding;
import com.openclassrooms.oc_p7.injection.Injection;
import com.openclassrooms.oc_p7.view_model.LoginViewModel;

import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";


    private MapViewModel mapViewModel;
    private FragmentMapBinding fragmentMapBinding;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;

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

        return fragmentMapBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkAndRequestPermissions();

    }

    public void initViewModels() {
        //INIT MAPVIEWMODEL
        MapViewModelFactory mapViewModelFactory = Injection.provideMapViewModelFactory();
        mapViewModel =
                ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel.class);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }


    public void initListeners() {

        fragmentMapBinding.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapViewModel.updateCurrentLocation(mapViewModel.currentLocationLiveData.getValue());
            }
        });


        /*
        fragmentMapBinding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginViewModel.isUserConnected()) {
                    Log.d(TAG, loginViewModel.getUserDisplayName());
                    Log.d(TAG, " SERVICE : " + GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext()));

                } else
                    Log.d(TAG, "User not connected");
            }
        });

        fragmentMapBinding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

         */


    }

    private void initObservers() {
        mapViewModel.currentLocationLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mapViewModel.updateCurrentLocation(location);
            }
        });

        mapViewModel.placeListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                googleMap.clear();
                for (Place place : places) {
                    googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
                }
            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = fragmentMapBinding.googleMap;
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "MAP READY");
        this.googleMap = googleMap;
        mapViewModel.mapLiveData.postValue(googleMap);
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
        mapViewModel.getNearbyPlaces(getActivity());
        getCurrentPlace();
    }


    private void getCurrentPlace() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Success getCurrentPlace");
                        //mapViewModel.setCurrentLocation(task.getResult());

                        mapViewModel.setCurrentLocation(task.getResult());


                    } else {
                        Log.d(TAG, "Fail getCurrentPlace");
                    }

                }
            });

        }

    }


    private void checkAndRequestPermissions() {
        Log.d(TAG, "FINE LOCATION PERMISSION : " + ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION));
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 19);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Code : " + requestCode + " Permissions : " + Arrays.toString(permissions) + " result :" + Arrays.toString(grantResults));
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
            refreshMap();
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