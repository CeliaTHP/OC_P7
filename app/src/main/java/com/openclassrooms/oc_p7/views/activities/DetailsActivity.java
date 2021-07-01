package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;


public class DetailsActivity extends BaseActivity {

    private Restaurant restaurant = null;
    private PlacesApi placesApi = Injection.provideApiClient();

    ActivityDetailsBinding activityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDetailsBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(this), null, false);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        initUI(restaurant);
        initListeners();

        setContentView(activityDetailsBinding.getRoot());
    }

    private void initUI(Restaurant restaurant) {

        activityDetailsBinding.detailsRestaurantName.setText(restaurant.getName());
        activityDetailsBinding.detailsRestaurantAddress.setText(restaurant.getAddress());


    }

    private void initListeners() {

        activityDetailsBinding.detailsRestaurantCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDetailsBinding.detailsRestaurantCheck.setColorFilter(getResources().getColor(R.color.green));
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