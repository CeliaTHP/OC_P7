package com.openclassrooms.oc_p7.view.map;

import android.Manifest;
import android.content.Intent;
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

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.databinding.FragmentMapBinding;
import com.openclassrooms.oc_p7.view.LoginActivity;
import com.openclassrooms.oc_p7.view_model.LoginViewModel;

import java.util.Arrays;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";


    private MapViewModel mapViewModel;
    private FragmentMapBinding fragmentMapBinding;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;


    private LoginViewModel loginViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(this.getContext()), null, false);
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        mapView = fragmentMapBinding.googleMap;
        initMap(savedInstanceState);

        return fragmentMapBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // mapViewModel.initMap(fragmentMapBinding.googleMap);

        initListeners();
        checkAndRequestPermissions();

    }


    public void initListeners() {
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

        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                fragmentMapBinding.textMap.setText(s);
            }
        });

    }

    private void initMap(Bundle savedInstanceState) {
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "MAP READY");
        map = googleMap;
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
        mapViewModel.initPlaces(googleMap, getActivity());
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
                        mapViewModel.currentLocation.postValue(task.getResult());
/*
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            LatLng lastLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            map.addMarker(new MarkerOptions().position(lastLatLng).title("YOU").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), 20));

 */
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Code : " + requestCode + " Permissions : " + Arrays.toString(permissions) + " result :" + Arrays.toString(grantResults));

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mapView != null) mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
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