package com.openclassrooms.oc_p7.repositories;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.prediction_pojo.PredictionPojo;
import com.openclassrooms.oc_p7.models.pojo_models.responses.AutocompleteResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.Photo;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.RestaurantPojo;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private final String apiKey;


    public MutableLiveData<List<Restaurant>> restaurantListMutableLiveData;
    public MutableLiveData<Restaurant> restaurantMutableLiveData;
    public MutableLiveData<List<Restaurant>> requestedRestaurantsListMutableLiveData;
    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();


    public PlaceRepository(PlacesApi placesApi,
                           String apiKey,
                           Executor executor,
                           MutableLiveData<List<Restaurant>> restaurantListMutableLiveData,
                           MutableLiveData<Restaurant> restaurantMutableLiveData,
                           MutableLiveData<List<Restaurant>> requestedRestaurantsListMutableLiveData,
                           String radiusQuery,
                           String restaurantQuery, MutableLiveData<ErrorCode> errorCode) {
        this.placesApi = placesApi;
        this.apiKey = apiKey;
        this.executor = executor;
        this.restaurantListMutableLiveData = restaurantListMutableLiveData;
        this.restaurantMutableLiveData = restaurantMutableLiveData;
        this.requestedRestaurantsListMutableLiveData = requestedRestaurantsListMutableLiveData;
        this.radiusQuery = radiusQuery;
        this.restaurantQuery = restaurantQuery;
        this.errorCode = errorCode;
    }


    public LiveData<List<Restaurant>> getRestaurantListMutableLiveData() {
        return restaurantListMutableLiveData;
    }

    public LiveData<List<Restaurant>> getRequestedRestaurantIdListMutableLiveData() {
        return requestedRestaurantsListMutableLiveData;
    }

    public LiveData<ErrorCode> getErrorCode() {
        return errorCode;
    }

    public void getNearbyPlaces(Location location) {
/*
        String locationStringQuery = location.getLatitude() + "," + location.getLongitude();

        //Executor to execute the following code in the same thread (easier for tests)
        executor.execute(() -> {
            Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(apiKey, locationStringQuery, radiusQuery, restaurantQuery);
            try {
                Response<NearbyPlaceResponse> response = call.execute();
                if (response.isSuccessful()) {
                    NearbyPlaceResponse nearbyPlaceResponse = response.body();
                    if (nearbyPlaceResponse != null && nearbyPlaceResponse.restaurantPojos != null) {
                        List<Restaurant> restaurantList = getRestaurantList(nearbyPlaceResponse.restaurantPojos);
                        restaurantListMutableLiveData.postValue(restaurantList);
                    }
                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);
                }
            } catch (IOException e) {
                errorCode.postValue(ErrorCode.CONNECTION_ERROR);

            }
        });

 */
    }

    public void updateRestaurantDetails(String restaurantId) {
        //Executor to execute the following code in the same thread (easier for tests)
        List<Restaurant> restaurantList = restaurantListMutableLiveData.getValue();
        executor.execute(() -> {
            Call<DetailsPlaceResponse> call =
                    placesApi.getDetailsById(apiKey, restaurantId);
            try {
                Response<DetailsPlaceResponse> response = call.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (restaurantList != null) {
                            for (Restaurant restaurant : restaurantList) {
                                if (restaurant.getId().equals(restaurantId)) {
                                    {
                                        setRestaurantInfos(response.body().result, restaurant);
                                    }
                                }
                            }
                            restaurantListMutableLiveData.postValue(restaurantList);

                        }
                    }

                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

                }

            } catch (IOException e) {
                errorCode.postValue(ErrorCode.CONNECTION_ERROR);
            }
        });
    }

    public void getRestaurantDetails(String restaurantId) {
        executor.execute(() -> {
            Call<DetailsPlaceResponse> call =
                    placesApi.getDetailsById(apiKey, restaurantId);
            try {
                Response<DetailsPlaceResponse> response = call.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Restaurant restaurantToCreate = createRestaurant(response.body().result);
                        setRestaurantInfos(response.body().result, restaurantToCreate);
                        restaurantMutableLiveData.postValue(restaurantToCreate);
                    }
                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

                }
            } catch (IOException e) {
                errorCode.postValue(ErrorCode.CONNECTION_ERROR);
            }
        });

    }

    public void getRequestedRestaurants(String input, LatLng latLng) {
        String language = Locale.getDefault().getLanguage();
        String locationStringQuery = latLng.latitude + "," + latLng.longitude;
        List<Restaurant> requestedRestaurantList = new ArrayList<>();


        Log.d(TAG, language + locationStringQuery);
        executor.execute(() -> {

            Call<AutocompleteResponse> call =
                    placesApi.getRequestedPlaces(apiKey, language, locationStringQuery, radiusQuery, input);
            try {
                Response<AutocompleteResponse> response = call.execute();
                if (response.isSuccessful()) {
                    for (PredictionPojo predictionPojo : response.body().predictions) {
                        if (predictionPojo.types.contains("restaurant")) {
                            requestedRestaurantList.add(new Restaurant(predictionPojo.placeId, predictionPojo.description, null, 0.0, 0.0));
                            Log.d(TAG, "isRestaurant" + predictionPojo.description);

                        }
                    }
                    Log.d(TAG, "requestedId List : " + requestedRestaurantList.size());

                    requestedRestaurantsListMutableLiveData.postValue(requestedRestaurantList);

                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);
                }
            } catch (IOException e) {
                errorCode.postValue(ErrorCode.CONNECTION_ERROR);
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
        return new Restaurant(restaurantPojo.placeId, restaurantPojo.name, restaurantPojo.vicinity, restaurantPojo.geometry.location.lat, restaurantPojo.geometry.location.lng);
    }

    /*
    static private Restaurant createRestaurantWithPrediction(PredictionPojo predictionPojo) {
        return new Restaurant(predictionPojo.placeId, predictionPojo.description, predictionPojo.ad, predictionPojo.geometry.location.lat, predictionPojo.geometry.location.lng);
    }


     */

    private void setRestaurantInfos(RestaurantPojo restaurantPojo, Restaurant restaurant) {


        List<String> photos = new ArrayList<>();

        if (restaurantPojo != null) {
            if (restaurantPojo.name != null)
                restaurant.setName(restaurantPojo.name);

            if (restaurantPojo.formattedAddress != null)
                restaurant.setAddress(restaurantPojo.formattedAddress);

            if (restaurantPojo.geometry.location.lat != 0.0)
                restaurant.setLat(restaurantPojo.geometry.location.lat);

            if (restaurantPojo.geometry.location.lng != 0.0)
                restaurant.setLng(restaurantPojo.geometry.location.lng);

            if (restaurantPojo.placeId != null)
                restaurant.setId(restaurantPojo.placeId);

            if (restaurantPojo.rating != 0.0)
                restaurant.setRating(restaurantPojo.rating);

            if (restaurantPojo.openingHours != null)
                restaurant.setOpeningHours(restaurantPojo.openingHours.weekday_text);

            if (restaurantPojo.internationalPhoneNumber != null)
                restaurant.setPhone(restaurantPojo.internationalPhoneNumber);

            if (restaurantPojo.website != null)
                restaurant.setWebsite(restaurantPojo.website);

            if (restaurantPojo.photos != null) {
                for (Photo photoUrl : restaurantPojo.photos) {
                    photos.add(photoUrl.photo_reference);
                }
                restaurant.setPhotoReference(photos);
            } else
                restaurant.setPhotoReference(null);

        }

        restaurant.setHasDetails(true);

    }

}

