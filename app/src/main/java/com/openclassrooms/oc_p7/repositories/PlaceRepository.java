package com.openclassrooms.oc_p7.repositories;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.MyApplication;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.pojo_models.details.DetailPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.general.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {

    private PlacesApi placesApi = Injection.provideApiClient();


    private String TAG = "PlaceRepository";

    private ArrayList<RestaurantPojo> placeList = new ArrayList<>();

    public MutableLiveData<List<RestaurantPojo>> nearbyPlacesLiveData = new MutableLiveData<>();
    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();

    public void getNearbyPlaces(Location location) {
        //String fakeLocation = "49.024979226793775,2.463881854135891";
//        String location = currentLocationLiveData.getValue().getLatitude() + "," + currentLocationLiveData.getValue().getLongitude();

        String radiusQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_radius);
        String restaurantQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_restaurant);

        String locationStringQuery = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

        Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(BuildConfig.GoogleMapApiKey, locationStringQuery, radiusQuery, restaurantQuery);
        call.enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                //TODO use Rx
                placeList.addAll(response.body().restaurantPojos);
                nearbyPlacesLiveData.postValue(placeList);
                Log.d(TAG, placeList.toString());

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    public void getDetailsById() {
        placesApi.getDetailsById(BuildConfig.GoogleMapApiKey, "ChIJZRd-EQRA5kcRnyzZQ1QWzVw").enqueue(new Callback<DetailPlaceResponse>() {
            @Override
            public void onResponse(Call<DetailPlaceResponse> call, Response<DetailPlaceResponse> response) {
                Log.d(TAG, response.body().result.name);

            }

            @Override
            public void onFailure(Call<DetailPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());


            }
        });
    }


}
