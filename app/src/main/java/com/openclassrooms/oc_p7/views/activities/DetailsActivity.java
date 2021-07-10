package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.factories.DetailViewModelFactory;
import com.openclassrooms.oc_p7.view_models.DetailViewModel;
import com.openclassrooms.oc_p7.views.adapters.SliderAdapter;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.List;


public class DetailsActivity extends BaseActivity {

    private final static String TAG = "DetailsActivity";
    private Restaurant restaurant = null;
    private DetailViewModel detailViewModel;

    ActivityDetailsBinding activityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDetailsBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(this), null, false);
        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        //TODO : detailsViewModel to get workmate
        initViewModels();
        initUI(restaurant);
        initListeners();
        initObservers();

        detailViewModel.getWorkmateList();

        setContentView(activityDetailsBinding.getRoot());
    }

    private void initViewModels() {
        DetailViewModelFactory detailViewModelFactory = Injection.provideDetailViewModelFactory(this);
        detailViewModel =
                ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel.class);

    }

    private void initUI(Restaurant restaurant) {

        activityDetailsBinding.detailsRestaurantName.setText(restaurant.getName());
        activityDetailsBinding.detailsRestaurantAddress.setText(restaurant.getAddress());

        initSlider();


    }

    private void initSlider() {

        SliderAdapter sliderAdapter = new SliderAdapter();
        if (restaurant.getPhotoReferences() != null && !restaurant.getPhotoReferences().isEmpty()) {
            for (String picUrl : restaurant.getPhotoReferences())
                sliderAdapter.addItem(picUrl);

        } else {
            sliderAdapter.addItem(null);
        }
        activityDetailsBinding.detailsRestaurantPicSlider.setSliderAdapter(sliderAdapter);
        activityDetailsBinding.detailsRestaurantPicSlider.setIndicatorEnabled(true);
        activityDetailsBinding.detailsRestaurantPicSlider.setIndicatorAnimation(IndicatorAnimationType.COLOR);
        activityDetailsBinding.detailsRestaurantPicSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        activityDetailsBinding.detailsRestaurantPicSlider.startAutoCycle();

    }

    private void initRecyclerView(List<Workmate> workmateList) {
        activityDetailsBinding.detailsRestaurantWorkmatesRv.setAdapter(new WorkmateAdapter(workmateList, true));
        activityDetailsBinding.detailsRestaurantWorkmatesRv.setLayoutManager(new LinearLayoutManager(this));
        if (activityDetailsBinding.detailsRestaurantWorkmatesRv.getAdapter().getItemCount() < 1)
            activityDetailsBinding.detailsEmptyList.setVisibility(View.VISIBLE);
    }

    private void initObservers() {
        detailViewModel.workmateListLiveData.observe(this, workmateList -> {
            Log.d(TAG, "workmateListLiveData" + workmateList.size());
            detailViewModel.getWorkmatesForRestaurant(workmateList, restaurant);

        });

        detailViewModel.workmateForRestaurantListLiveData.observe(this, workmateForRestaurantList -> {
            initRecyclerView(workmateForRestaurantList);
            Log.d(TAG, "workmateForRestaurantListLiveData" + workmateForRestaurantList.size());
        });

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