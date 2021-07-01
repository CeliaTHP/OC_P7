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

import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentListRestaurantsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.MapViewModel;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.RestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListFragment extends Fragment implements OnRestaurantClickListener {

    private final static String TAG = "RestaurantListFragment";
    private WorkmateViewModel workmateViewModel;
    private MapViewModel mapViewModel;
    private FragmentListRestaurantsBinding fragmentListRestaurantsBinding;
    private RestaurantAdapter adapter;

    private List<Restaurant> mRestaurantList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListRestaurantsBinding = FragmentListRestaurantsBinding.inflate(LayoutInflater.from(this.getContext()));

        initViewModels();
        initPlaces();
        initObservers();
        initRecyclerView();

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
        mapViewModel.restaurantLiveData.observe(getViewLifecycleOwner(), restaurantList -> {
            mRestaurantList = restaurantList;
            Log.d(TAG, "nearbyPlacesObserver from Restaurant");

            for (Restaurant restaurant : restaurantList) {
                mapViewModel.getRestaurantDetails(restaurant, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, " onSuccess");
                        initRecyclerView();
                    }
                });
            }
            adapter.notifyDataSetChanged();

        });
    }


    private void initRecyclerView() {
        adapter = new RestaurantAdapter(mRestaurantList, mapViewModel.currentLocationLiveData.getValue(), this);
        fragmentListRestaurantsBinding.restaurantRecyclerView.setAdapter(adapter);
        fragmentListRestaurantsBinding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        Log.d(TAG, "click on : " + restaurant.getName());
        Intent intent = new Intent(this.getActivity(), DetailsActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);

    }
}