package com.openclassrooms.oc_p7.repositories;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.details.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.details.RestaurantDetailsPojo;
import com.openclassrooms.oc_p7.models.pojo_models.general.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.general.Photo;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class PlaceRepository {

    private final String TAG = "PlaceRepository";

    private final PlacesApi placesApi;
    private final Executor executor;
    private final String radiusQuery;
    private final String restaurantQuery;
    private MutableLiveData<ErrorCode> errorCode;
    private Boolean shouldPostValue = true;

    public PlaceRepository(PlacesApi placesApi,
                           Executor executor,
                           MutableLiveData<List<Restaurant>> restaurantLiveData,
                           String radiusQuery,
                           String restaurantQuery, MutableLiveData<ErrorCode> errorCode) {
        this.placesApi = placesApi;
        this.executor = executor;
        this.restaurantLiveData = restaurantLiveData;
        this.radiusQuery = radiusQuery;
        this.restaurantQuery = restaurantQuery;
        this.errorCode = errorCode;
    }

    public MutableLiveData<List<Restaurant>> restaurantLiveData;

    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();

    public enum ErrorCode {
        UNSUCCESSFUL_RESPONSE,
        DATA_FORMAT_ERROR,

    }

    public LiveData<List<Restaurant>> getRestaurantLiveData() {
        return restaurantLiveData;
    }

    public void getNearbyPlaces(Location location) {

        String locationStringQuery = location.getLatitude() + "," + location.getLongitude();

        //Executor to execute the following code in the same thread (easier for tests)
        executor.execute(() -> {
            Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(BuildConfig.GoogleMapApiKey, locationStringQuery, radiusQuery, restaurantQuery);
            try {
                Response<NearbyPlaceResponse> response = call.execute();
                if (response.isSuccessful()) {
                    NearbyPlaceResponse nearbyPlaceResponse = response.body();
                    if (nearbyPlaceResponse != null && nearbyPlaceResponse.restaurantPojos != null) {
                        List<Restaurant> restaurantList = getRestaurantList(nearbyPlaceResponse.restaurantPojos);
                        restaurantLiveData.postValue(restaurantList);
                    }
                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);
                }
            } catch (IOException e) {
                errorCode.postValue(ErrorCode.DATA_FORMAT_ERROR);

            }
        });
    }

    public void getRestaurantDetails(String restaurantId) {

        //Executor to execute the following code in the same thread (easier for tests)
        Log.d(TAG, "getRestaurantDetails " + restaurantId);
        List<Restaurant> restaurantList = restaurantLiveData.getValue();

        executor.execute(() -> {
            Call<DetailsPlaceResponse> call =
                    placesApi.getDetailsById(BuildConfig.GoogleMapApiKey, restaurantId);
            try {
                Response<DetailsPlaceResponse> response = call.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (restaurantList != null && !restaurantList.isEmpty()) {
                            Log.d(TAG, "list case");
                            for (Restaurant restaurant : restaurantList) {
                                if (restaurant.getId().equals(restaurantId)) {
                                    setRestaurantInfos(response.body().result, restaurant);

                                }

                            }


                        } else {
                            Log.d(TAG, "details case");
                            setRestaurantInfos(response.body().result, new Restaurant(restaurantId, null, null, 0.0, 0.0));

                            // TODO
                            // get restaurant and post it

                        }


                    }
                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);
                    Log.d(TAG, "onFailure: " + response.errorBody());

                }


            } catch (IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                errorCode.postValue(ErrorCode.DATA_FORMAT_ERROR);
            }


        });
    }

    static public List<Restaurant> getRestaurantList(List<RestaurantPojo> restaurantPojoList) {
        List<Restaurant> restaurantList = new ArrayList<>();
        for (RestaurantPojo restaurantPojo : restaurantPojoList) {
            Restaurant restaurant = createRestaurant(restaurantPojo);
            restaurantList.add(restaurant);
        }
        return restaurantList;
    }

    static private Restaurant createRestaurant(RestaurantPojo restaurantPojo) {
        return new Restaurant(restaurantPojo.place_id, restaurantPojo.name, restaurantPojo.vicinity, restaurantPojo.geometry.location.lat, restaurantPojo.geometry.location.lng);
    }

    private void setRestaurantInfos(RestaurantDetailsPojo restaurantDetailsPojo, Restaurant restaurant) {

        Log.d(TAG, "setRestaurantInfos for " + restaurant.getName());

        List<String> photos = new ArrayList<>();

        if (restaurantDetailsPojo != null) {
            if (restaurantDetailsPojo.name != null)
                restaurant.setName(restaurantDetailsPojo.name);

            if (restaurantDetailsPojo.formatted_address != null)
                restaurant.setAddress(restaurantDetailsPojo.formatted_address);

            if (restaurantDetailsPojo.geometry.location.lat != 0.0)
                restaurant.setLat(restaurantDetailsPojo.geometry.location.lat);

            if (restaurantDetailsPojo.geometry.location.lng != 0.0)
                restaurant.setLng(restaurantDetailsPojo.geometry.location.lng);

            if (restaurantDetailsPojo.place_id != null)
                restaurant.setId(restaurantDetailsPojo.place_id);

            if (restaurantDetailsPojo.rating != 0.0)
                restaurant.setRating(restaurantDetailsPojo.rating);

            if (restaurantDetailsPojo.opening_hours != null)
                restaurant.setOpeningHours(restaurantDetailsPojo.opening_hours.weekday_text);

            if (restaurantDetailsPojo.international_phone_number != null)
                restaurant.setPhone(restaurantDetailsPojo.international_phone_number);

            if (restaurantDetailsPojo.website != null)
                restaurant.setWebsite(restaurantDetailsPojo.website);

            if (restaurantDetailsPojo.photos != null) {
                for (Photo photoUrl : restaurantDetailsPojo.photos) {
                    photos.add(photoUrl.photo_reference);
                }
                restaurant.setPhotoReference(photos);
            } else
                restaurant.setPhotoReference(null);
            /*
            Log.d(TAG, restaurant.getName() + " " + restaurant.getRating() + " " + restaurant.getOpeningHours() + " " + restaurant.getPhone() + " "
                    + restaurant.getWebsite() + " " + restaurant.getPhotoReferences().size() + restaurant.getPhotoReferences() + " ");

             */
        }
        Log.d(TAG, "has setRestaurantInfos : " + restaurant.getName());

    }

}

