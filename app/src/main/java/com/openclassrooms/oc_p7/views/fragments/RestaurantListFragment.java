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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentListRestaurantsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.services.utils.OnRestaurantQueryEvent;
import com.openclassrooms.oc_p7.view_models.MapViewModel;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.RequestAdapter;
import com.openclassrooms.oc_p7.views.adapters.RestaurantAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class RestaurantListFragment extends Fragment implements OnRestaurantClickListener {

    private final static String TAG = "RestaurantListFragment";

    private WorkmateViewModel workmateViewModel;
    private MapViewModel mapViewModel;
    private FragmentListRestaurantsBinding fragmentListRestaurantsBinding;
    private RestaurantAdapter restaurantAdapter;

    private RequestAdapter requestAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListRestaurantsBinding = FragmentListRestaurantsBinding.inflate(LayoutInflater.from(this.getContext()));

        //init EventBus
        EventBus.getDefault().register(this);

        initViewModels();
        initPlaces();
        initRecyclerView();
        initRequestRecyclerView();
        initObservers();

        workmateViewModel.getWorkmateList();

        return fragmentListRestaurantsBinding.getRoot();
    }

    @Subscribe
    public void onQueryEvent(OnRestaurantQueryEvent onQueryEvent) {
        if (onQueryEvent.getQueryForRestaurant() != null) {
            LatLng currentLatLng = new LatLng(mapViewModel.currentLocationLiveData.getValue().getLatitude(), mapViewModel.currentLocationLiveData.getValue().getLongitude());
            mapViewModel.getRequestedRestaurants(onQueryEvent.getQueryForRestaurant(), currentLatLng);
        } else {
            requestAdapter.setData(new ArrayList<>());
        }
    }


    public void initRequestRecyclerView() {
        requestAdapter = new RequestAdapter(new ArrayList<>(), new OnRestaurantClickListener() {
            @Override
            public void onRestaurantClick(Restaurant restaurant) {
                requestAdapter.setData(new ArrayList<>());
                startDetailsActivity(restaurant.getId());
            }
        });
        fragmentListRestaurantsBinding.requestRecyclerView.setAdapter(requestAdapter);
        fragmentListRestaurantsBinding.requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentListRestaurantsBinding.requestRecyclerView.addItemDecoration(new DividerItemDecoration(fragmentListRestaurantsBinding.getRoot().getContext(), LinearLayoutManager.VERTICAL));

    }

    private void startDetailsActivity(String restaurantId) {
        Intent intent = new Intent(fragmentListRestaurantsBinding.getRoot().getContext(), DetailsActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }

    private void initViewModels() {

        MapViewModelFactory mapViewModelFactory = Injection.provideMapViewModelFactory(FirebaseFirestore.getInstance(), getContext(), this);
        mapViewModel = ViewModelProviders.of(this, mapViewModelFactory).get(MapViewModel.class);

        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(FirebaseFirestore.getInstance());
        workmateViewModel = ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }

    private void initPlaces() {
        mapViewModel.getLocationInformations(getContext());
    }

    private void initObservers() {

        mapViewModel.currentLocationLiveData.observe(getViewLifecycleOwner(), currentLocation -> {
            restaurantAdapter.setCurrentLocation(currentLocation);

        });

        mapViewModel.placeRepositoryErrorCodeMutableLiveData.observe(getViewLifecycleOwner(), errorCode -> {
            if (errorCode == ErrorCode.CONNECTION_ERROR) {
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.map_data_format_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.map_response_error), Toast.LENGTH_LONG).show();

            }

        });

        mapViewModel.workmateRepositoryErrorCodeMutableLiveData.observe(getViewLifecycleOwner(), errorCode -> {
            if (errorCode == ErrorCode.EXECUTION_EXCEPTION) {
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.workmate_execution_error), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.workmate_interrupted_error), Toast.LENGTH_LONG).show();
            }

        });

        mapViewModel.restaurantListLiveData.observe(getViewLifecycleOwner(), restaurantList -> {
            restaurantAdapter.setData(restaurantList);
            workmateViewModel.getWorkmateForRestaurantList(mapViewModel.restaurantListLiveData);
            Log.d(TAG, "nearbyPlacesObserver from Restaurant" + restaurantList.size());

        });

        mapViewModel.requestedRestaurantList.observe(getViewLifecycleOwner(), requestedRestaurantList -> {
            if (requestedRestaurantList.isEmpty())
                Toast.makeText(fragmentListRestaurantsBinding.getRoot().getContext(), getString(R.string.map_not_found), Toast.LENGTH_SHORT).show();

            requestAdapter.setData(requestedRestaurantList);
        });

    }

    private void initRecyclerView() {
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>(20), this, mapViewModel);
        fragmentListRestaurantsBinding.restaurantRecyclerView.setAdapter(restaurantAdapter);
        fragmentListRestaurantsBinding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        Intent intent = new Intent(this.getActivity(), DetailsActivity.class);
        intent.putExtra("restaurantId", restaurant.getId());
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}