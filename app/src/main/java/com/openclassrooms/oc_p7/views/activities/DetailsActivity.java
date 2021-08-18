package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnWorkmateClickListener;
import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.factories.DetailViewModelFactory;
import com.openclassrooms.oc_p7.services.firestore_helpers.FavoriteHelper;
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
        initListeners();
        initObservers();

        //detailsViewModel.getWorkmateList();

        setContentView(activityDetailsBinding.getRoot());
    }

    private void initExtras(Intent intent) {
        if (intent.getStringExtra("restaurantId") != null) {
            Log.d(TAG, "restaurantId Extra");
            restaurant = new Restaurant(intent.getStringExtra("restaurantId"), null, null, 0.0, 0.0);
            detailsViewModel.setWorkmatesForRestaurant(restaurant);
            detailsViewModel.setRestaurantDetails(restaurant, new OnSuccessListener() {

                @Override
                public void onSuccess(Object o) {
                    restaurant = (Restaurant) o;
                    initUI(restaurant);
                    Log.d("NEW_DETAILS", restaurant.toString());

                }
            });


        }
    }

    private void initViewModels() {
        DetailViewModelFactory detailViewModelFactory = Injection.provideDetailViewModelFactory(this);
        detailsViewModel =
                ViewModelProviders.of(this, detailViewModelFactory).get(DetailsViewModel.class);

    }

    private void initUI(Restaurant restaurant) {

        activityDetailsBinding.detailsRestaurantName.setText(restaurant.getName());
        activityDetailsBinding.detailsRestaurantAddress.setText(restaurant.getAddress());

        //Setting the corresponding star
        FavoriteHelper.getFavorite(restaurant).addOnSuccessListener(snapshot -> {
            Log.d(TAG, snapshot.get("id") + " ");
            if (snapshot.get("id") != null && snapshot.get("id").toString().equals(restaurant.getId())) {
                restaurant.setIsLiked(true);
                activityDetailsBinding.detailsRestaurantLikePic.setImageResource(R.drawable.ic_star_details);
                Log.d(TAG, snapshot.get("id") + " is Fav ");
            }


        });

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

        initRecyclerView(restaurant.getAttendees());
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
        if (workmateList != null) {
            activityDetailsBinding.detailsRestaurantWorkmatesRv.setAdapter(new WorkmateAdapter(workmateList, true, new OnWorkmateClickListener() {
                @Override
                public void onWorkmateClick(Workmate workmate) {

                }
            }));
            activityDetailsBinding.detailsRestaurantWorkmatesRv.setLayoutManager(new LinearLayoutManager(this));
            if (activityDetailsBinding.detailsRestaurantWorkmatesRv.getAdapter().getItemCount() < 1)
                activityDetailsBinding.detailsEmptyList.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "emptyWorkmateList");
        }
    }

    private void initObservers() {


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
        activityDetailsBinding.detailsPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.getPhone() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + restaurant.getPhone()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getParent(), getString(R.string.details_no_info), Toast.LENGTH_LONG).show();
                }
            }
        });

        activityDetailsBinding.detailsLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!restaurant.getIsLiked()) {
                    restaurant.setIsLiked(true);
                    activityDetailsBinding.detailsRestaurantLikePic.setImageResource(R.drawable.ic_star_details);
                    FavoriteHelper.addFavorite(restaurant);
                } else {
                    restaurant.setIsLiked(false);
                    activityDetailsBinding.detailsRestaurantLikePic.setImageResource(R.drawable.ic_star_details_not_full);
                    FavoriteHelper.deleteFavorite(restaurant);
                }

            }
        });

        activityDetailsBinding.detailsWebsiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.getWebsite() != null) {
                    Uri uriUrl = Uri.parse(restaurant.getWebsite());
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                } else {
                    Toast.makeText(getParent(), getString(R.string.details_no_info), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

}