package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends BaseActivity {

    private Restaurant restaurant = null;
    private WorkmateRepository workmateRepository;
    private PlacesApi placesApi = Injection.provideApiClient();
    private WorkmateViewModel workmateViewModel;
    private List<String> placeIdList = new ArrayList<>();

    ActivityDetailsBinding activityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDetailsBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(this), null, false);
        workmateRepository = Injection.provideWorkmateRepository(this);
        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        initViewModels();
        initUI(restaurant);
        initListeners();
        initObservers();

        workmateViewModel.getWorkmateList();

        setContentView(activityDetailsBinding.getRoot());
    }

    private void initViewModels() {
        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(this);
        workmateViewModel =
                ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }

    private void initUI(Restaurant restaurant) {

        activityDetailsBinding.detailsRestaurantName.setText(restaurant.getName());
        activityDetailsBinding.detailsRestaurantAddress.setText(restaurant.getAddress());

    }

    private void initRecyclerView(List<Workmate> workmateList) {
        activityDetailsBinding.detailsRestaurantWorkmatesRv.setAdapter(new WorkmateAdapter(workmateList, true));
        activityDetailsBinding.detailsRestaurantWorkmatesRv.setLayoutManager(new LinearLayoutManager(this));
        if (activityDetailsBinding.detailsRestaurantWorkmatesRv.getAdapter().getItemCount() < 1)
            activityDetailsBinding.detailsEmptyList.setVisibility(View.VISIBLE);
    }

    private void initObservers() {

        workmateViewModel.workmateListLiveData.observe(this, workmates -> {
            initRecyclerView(filterList(workmates));
        });

    }

    private List<Workmate> filterList(List<Workmate> workmateList) {
        List<Workmate> filteredList = new ArrayList<>();
        for (Workmate workmate : workmateList) {
            if (workmate.getRestaurantId() != null) {
                if (workmate.getRestaurantId().equals(restaurant.getId()))
                    filteredList.add(workmate);
            }
        }
        return filteredList;
    }

    private void initListeners() {

        activityDetailsBinding.detailsRestaurantCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!restaurant.getIsChosen()) {
                    activityDetailsBinding.detailsRestaurantCheck.setColorFilter(getResources().getColor(R.color.green));
                    restaurant.setIsChosen(true);
                } else {
                    activityDetailsBinding.detailsRestaurantCheck.setColorFilter(getResources().getColor(R.color.pastel_green));
                    restaurant.setIsChosen(false);

                }
            }
        });
        activityDetailsBinding.detailsRestaurantLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setFavorite

            }
        });
    }

}