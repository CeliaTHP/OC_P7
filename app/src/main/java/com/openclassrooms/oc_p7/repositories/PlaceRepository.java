package com.openclassrooms.oc_p7.repositories;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.MyApplication;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.details.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.details.RestaurantDetailsPojo;
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
    private ArrayList<Restaurant> restaurantList = new ArrayList<>();


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
                for (RestaurantPojo restaurantPojo : placeList) {
                    getDetailsById(restaurantPojo);
                }
                nearbyPlacesLiveData.postValue(placeList);
                Log.d(TAG, placeList.toString());

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


    }

    public void getDetailsById(RestaurantPojo restaurantPojo) {

        placesApi.getDetailsById(BuildConfig.GoogleMapApiKey, restaurantPojo.place_id).enqueue(new Callback<DetailsPlaceResponse>() {
            @Override
            public void onResponse(Call<DetailsPlaceResponse> call, Response<DetailsPlaceResponse> response) {
                Log.d(TAG, response.body().result.name);
                Restaurant restaurant = createRestaurant(response.body().result);
                setRestaurantInfos(response.body().result, restaurant);
                restaurantList.add(restaurant);
                Log.d(TAG, "added : " + restaurant.getId() + " " + restaurant.getName() + " " + restaurant.getAddress() + " " + restaurant.getWebsite() + " " + restaurant.getRating() + " " + restaurant.getPhone() + " " + restaurant.getOpeningHours());

            }

            @Override
            public void onFailure(Call<DetailsPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private Restaurant createRestaurant(RestaurantDetailsPojo restaurantDetailsPojo) {
        return new Restaurant(restaurantDetailsPojo.place_id, restaurantDetailsPojo.name, restaurantDetailsPojo.formatted_address, restaurantDetailsPojo.geometry.location.lat, restaurantDetailsPojo.geometry.location.lng);
    }

    private void setRestaurantInfos(RestaurantDetailsPojo restaurantDetailsPojo, Restaurant restaurant) {

        restaurant.setRating(restaurantDetailsPojo.rating);
        if (restaurantDetailsPojo.opening_hours != null)
            restaurant.setOpeningHours(restaurantDetailsPojo.opening_hours.weekday_text);
        if (restaurantDetailsPojo.international_phone_number != null)
            restaurant.setPhone(restaurantDetailsPojo.international_phone_number);
        if (restaurantDetailsPojo.website != null) {
            restaurant.setWebsite(restaurantDetailsPojo.website);
        }


    }


}
