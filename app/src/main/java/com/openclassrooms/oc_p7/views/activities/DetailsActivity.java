package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;


public class DetailsActivity extends BaseActivity {

    private RestaurantPojo restaurant = null;
    private PlacesApi placesApi = Injection.provideApiClient();

    ActivityDetailsBinding activityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDetailsBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(this), null, false);

        Intent intent = getIntent();
        restaurant = (RestaurantPojo) intent.getSerializableExtra("restaurant");

        initUI(restaurant);

        setContentView(activityDetailsBinding.getRoot());
    }

    private void initUI(RestaurantPojo restaurant) {

        activityDetailsBinding.detailsRestaurantName.setText(restaurant.name);
        activityDetailsBinding.detailsRestaurantAddress.setText(restaurant.vicinity);

    }
}