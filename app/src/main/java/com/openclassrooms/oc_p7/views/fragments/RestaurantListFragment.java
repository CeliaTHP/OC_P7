package com.openclassrooms.oc_p7.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentListRestaurantsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.MapViewModel;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.RestaurantAdapter;

import java.util.List;

public class RestaurantListFragment extends Fragment implements OnRestaurantClickListener {

    private final static String TAG = "RestaurantListFragment";
    private WorkmateViewModel workmateViewModel;
    private MapViewModel mapViewModel;
    private FragmentListRestaurantsBinding fragmentListRestaurantsBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListRestaurantsBinding = FragmentListRestaurantsBinding.inflate(LayoutInflater.from(this.getContext()));

        initViewModels();
        initPlaces();
        initObservers();

        return fragmentListRestaurantsBinding.getRoot();
    }

    private void initViewModels() {

        MapViewModelFactory mapViewModelFactory = Injection.provideMapViewModelFactory(getContext());
        mapViewModel = ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel.class);

        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(getContext());
        workmateViewModel = ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }

    private void initPlaces() {
        mapViewModel.getLocationInformations(getContext());
    }

    private void initObservers() {
        mapViewModel.nearbyPlacesLiveData.observe(getViewLifecycleOwner(), restaurantPojos -> {
            Log.d(TAG, "nearbyPlacesObserver from Restaurant");

            for (RestaurantPojo restaurantPojo : restaurantPojos)
                Log.d(TAG, restaurantPojo.name);

            initRecyclerView(restaurantPojos);
        });
    }


    private void initRecyclerView(List<RestaurantPojo> restaurantPojoList) {
        fragmentListRestaurantsBinding.restaurantRecyclerView.setAdapter(new RestaurantAdapter(restaurantPojoList, mapViewModel.currentLocationLiveData.getValue(), this));
        ;
        fragmentListRestaurantsBinding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onRestaurantClick(RestaurantPojo restaurant) {
        Log.d(TAG, "click on : " + restaurant.name);
        Intent intent = new Intent(this.getActivity(), DetailsActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);

    }
}