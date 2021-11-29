package com.openclassrooms.oc_p7.injections;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import com.openclassrooms.oc_p7.services.factories.DetailViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.services.firestore_database.WorkmateDatabase;

import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    //Provide repositories and view model factories to views

    private static PlaceRepository providePlaceRepository(Context context) {
        String radiusQuery = context.getString(R.string.query_radius);
        String restaurantQuery = context.getString(R.string.query_restaurant);
        String apiKey = context.getString(R.string.GOOGLE_MAP_API_KEY_DEV);
        return new PlaceRepository(Injection.provideApiClient(), apiKey, Executors.newSingleThreadExecutor(), new MutableLiveData<>(), new MutableLiveData<>(), new MutableLiveData<>(), radiusQuery, restaurantQuery, new MutableLiveData<>());
    }

    private static WorkmateRepository provideWorkmateRepository(FirebaseFirestore firebaseFirestore) {
        return new WorkmateRepository(firebaseFirestore, new WorkmateDatabase(), Executors.newSingleThreadExecutor(), new MutableLiveData<>(), new MutableLiveData<>());
    }

    public static MapViewModelFactory provideMapViewModelFactory(FirebaseFirestore firebaseFirestore, Context context, LifecycleOwner lifecycleOwner) {
        PlaceRepository placeRepository = providePlaceRepository(context);
        WorkmateRepository workmateRepository = provideWorkmateRepository(firebaseFirestore);
        return new MapViewModelFactory(placeRepository, workmateRepository, Injection.provideFusedLocationProviderClient(context), lifecycleOwner);
    }

    public static WorkmateViewModelFactory provideWorkmateViewModelFactory(FirebaseFirestore firebaseFirestore) {
        WorkmateRepository workmateRepository = provideWorkmateRepository(firebaseFirestore);
        return new WorkmateViewModelFactory(workmateRepository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(FirebaseFirestore firebaseFirestore, Context context) {
        WorkmateRepository workmateRepository = provideWorkmateRepository(firebaseFirestore);
        PlaceRepository placeRepository = providePlaceRepository(context);
        return new DetailViewModelFactory(workmateRepository, placeRepository);
    }


    private static FusedLocationProviderClient provideFusedLocationProviderClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    private static PlacesApi provideApiClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PlacesApi.class);

    }


}
