package com.openclassrooms.oc_p7.injections;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.MyApplication;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import com.openclassrooms.oc_p7.services.factories.DetailViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;

import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    public static PlaceRepository providePlaceRepository(Context context) {
        String radiusQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_radius);
        String restaurantQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_restaurant);
        String apiKey = context.getString(R.string.GOOGLE_MAP_API_KEY_DEV);
        return new PlaceRepository(Injection.provideApiClient(), apiKey, Executors.newSingleThreadExecutor(), new MutableLiveData<>(), new MutableLiveData<>(), radiusQuery, restaurantQuery, new MutableLiveData<>());
    }

    public static WorkmateRepository provideWorkmateRepository(FirebaseFirestore firebaseFirestore, Context context) {
        return new WorkmateRepository(firebaseFirestore, new WorkmateHelper(), Executors.newSingleThreadExecutor(), new MutableLiveData<>(), new MutableLiveData<>());
    }

    public static MapViewModelFactory provideMapViewModelFactory(FirebaseFirestore firebaseFirestore, Context context, LifecycleOwner lifecycleOwner) {
        PlaceRepository placeRepository = providePlaceRepository(context);
        WorkmateRepository workmateRepository = provideWorkmateRepository(firebaseFirestore, context);
        return new MapViewModelFactory(placeRepository, workmateRepository, Injection.provideFusedLocationProviderClient(firebaseFirestore, context), lifecycleOwner);
    }

    public static WorkmateViewModelFactory provideWorkmateViewModelFactory(FirebaseFirestore firebaseFirestore, Context context) {
        WorkmateRepository workmateRepository = provideWorkmateRepository(firebaseFirestore, context);
        return new WorkmateViewModelFactory(workmateRepository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(FirebaseFirestore firebaseFirestore, Context context) {
        WorkmateRepository workmateRepository = provideWorkmateRepository(firebaseFirestore, context);
        PlaceRepository placeRepository = providePlaceRepository(context);
        return new DetailViewModelFactory(workmateRepository, placeRepository);
    }


    public static FusedLocationProviderClient provideFusedLocationProviderClient(FirebaseFirestore firebaseFirestore, Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    public static PlacesApi provideApiClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PlacesApi.class);

    }


}
