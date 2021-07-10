package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.factories.DetailViewModelFactory;
import com.openclassrooms.oc_p7.services.firestore_helpers.UserHelper;
import com.openclassrooms.oc_p7.view_models.DetailsViewModel;
import com.openclassrooms.oc_p7.views.adapters.SliderAdapter;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.List;


public class DetailsActivity extends BaseActivity {

    private final static String TAG = "DetailsActivity";
    private Restaurant restaurant = null;
    private DetailsViewModel detailsViewModel;


    ActivityDetailsBinding activityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDetailsBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(this), null, false);

        initViewModels();

        Intent intent = getIntent();
        initExtras(intent);

        //TODO : detailsViewModel to get workmate
        initUI(restaurant);
        initListeners();
        initObservers();

        detailsViewModel.getWorkmateList();

        setContentView(activityDetailsBinding.getRoot());
    }

    private void initExtras(Intent intent) {
        if (intent.getSerializableExtra("restaurant") != null)
            restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        else if (intent.getSerializableExtra("restaurantId") != null) {
            restaurant = new Restaurant(intent.getSerializableExtra("restaurantId").toString(), null, null, 0.0, 0.0);
            detailsViewModel.getRestaurantDetails(restaurant, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d(TAG, "onSuccess initExtra");
                    initUI((Restaurant) o);

                }
            });

        }
    }

    private void initViewModels() {
        DetailViewModelFactory detailViewModelFactory = Injection.provideDetailViewModelFactory(this);
        detailsViewModel =
                ViewModelProviders.of(this, detailViewModelFactory).get(DetailsViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, restaurant.getIsLiked() + " ");
        initUI(restaurant);
    }

    private void initUI(Restaurant restaurant) {

        activityDetailsBinding.detailsRestaurantName.setText(restaurant.getName());
        activityDetailsBinding.detailsRestaurantAddress.setText(restaurant.getAddress());

        //Setting the corresponding star
        if (restaurant.getIsLiked())
            activityDetailsBinding.detailsRestaurantLikePic.setImageResource(R.drawable.ic_star_details);
        //Setting the corresponding check
        UserHelper.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(snapshot -> {
            if (snapshot.get("restaurantId") != null) {
                Log.d(TAG, "restaurantId not null : " + snapshot.get("restaurantId").toString());
                if (snapshot.get("restaurantId").toString().equals(restaurant.getId())) {
                    activityDetailsBinding.detailsRestaurantCheck.setColorFilter(getResources().getColor(R.color.green));
                    Log.d(TAG, "restaurant chosen by user");

                }
            }
        });


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
        detailsViewModel.workmateListLiveData.observe(this, workmateList -> {
            Log.d(TAG, "workmateListLiveData" + workmateList.size());
            detailsViewModel.getWorkmatesForRestaurant(workmateList, restaurant);

        });

        detailsViewModel.workmateForRestaurantListLiveData.observe(this, workmateForRestaurantList -> {
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
                    UserHelper.updateUserRestaurantId(restaurant.getId(), FirebaseAuth.getInstance().getUid());
                } else {
                    activityDetailsBinding.detailsRestaurantCheck.setColorFilter(getResources().getColor(R.color.pastel_green));
                    restaurant.setIsChosen(false);
                    UserHelper.updateUserRestaurantId(null, FirebaseAuth.getInstance().getUid());

                }
            }
        });

        activityDetailsBinding.detailsLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!restaurant.getIsLiked()) {
                    restaurant.setIsLiked(true);
                    activityDetailsBinding.detailsRestaurantLikePic.setImageResource(R.drawable.ic_star_details);
                } else {
                    restaurant.setIsLiked(false);
                    activityDetailsBinding.detailsRestaurantLikePic.setImageResource(R.drawable.ic_star_details_not_full);
                }

            }
        });
    }

}