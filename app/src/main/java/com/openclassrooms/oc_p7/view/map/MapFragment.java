package com.openclassrooms.oc_p7.view.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.FragmentMapBinding;
import com.openclassrooms.oc_p7.view.LoginActivity;
import com.openclassrooms.oc_p7.view_model.LoginViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private MapViewModel mapViewModel;
    private FragmentMapBinding fragmentMapBinding;

    private LoginViewModel loginViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(this.getContext()), null, false);
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        return fragmentMapBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapViewModel.initMap(supportMapFragment);

        initListeners();
        initPlaces();

    }

    public void initPlaces() {
        Places.initialize(getContext(), String.valueOf(R.string.map_key));
        PlacesClient placesClient = Places.createClient(getContext());
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "MAP READY");
    }

}