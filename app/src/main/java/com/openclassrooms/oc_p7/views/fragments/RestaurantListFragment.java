package com.openclassrooms.oc_p7.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentListRestaurantsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.MapViewModel;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.RestaurantAdapter;

import java.util.Collections;

public class RestaurantListFragment extends Fragment implements OnRestaurantClickListener {

    private final static String TAG = "RestaurantListFragment";
    private WorkmateViewModel workmateViewModel;
    private MapViewModel mapViewModel;
    private FragmentListRestaurantsBinding fragmentListRestaurantsBinding;
    private RestaurantAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListRestaurantsBinding = FragmentListRestaurantsBinding.inflate(LayoutInflater.from(this.getContext()));


        initViewModels();
        initPlaces();
        initObservers();

        workmateViewModel.getWorkmateList();


        return fragmentListRestaurantsBinding.getRoot();
    }

    private void initViewModels() {

        MapViewModelFactory mapViewModelFactory = Injection.provideMapViewModelFactory(getContext(), this);
        mapViewModel = ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel.class);

        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(getContext());
        workmateViewModel = ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }

    private void initPlaces() {
        mapViewModel.getLocationInformations(getContext());
    }

    private void initObservers() {

        mapViewModel.currentLocationLiveData.observe(getViewLifecycleOwner(), currentLocation -> {
            initRecyclerView();

        });

        mapViewModel.errorCodeMutableLiveData.observe(getViewLifecycleOwner(), errorCode -> {
            if (errorCode == ErrorCode.CONNECTION_ERROR) {
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.map_data_format_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.map_response_error), Toast.LENGTH_LONG).show();

            }


        });
        mapViewModel.restaurantListLiveData.observe(getViewLifecycleOwner(), restaurantList -> {

            workmateViewModel.getWorkmateForRestaurantList(mapViewModel.restaurantListLiveData);
            adapter.setData(restaurantList);
            adapter.notifyDataSetChanged();

            Log.d(TAG, "nearbyPlacesObserver from Restaurant");

        });
    }


    private void initRecyclerView() {
        adapter = new RestaurantAdapter(Collections.emptyList(), mapViewModel.currentLocationLiveData.getValue(), this, mapViewModel);
        fragmentListRestaurantsBinding.restaurantRecyclerView.setAdapter(adapter);
        fragmentListRestaurantsBinding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onRestaurantClick(Restaurant restaurant) {

        Log.d(TAG, "click on : " + restaurant.getName());
        Intent intent = new Intent(this.getActivity(), DetailsActivity.class);
        intent.putExtra("restaurantId", restaurant.getId());
        startActivity(intent);

    }
}