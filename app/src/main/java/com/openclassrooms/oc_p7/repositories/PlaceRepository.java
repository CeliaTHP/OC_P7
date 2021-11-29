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
import com.openclassrooms.oc_p7.models.pojo_models.responses.NearbyPlaceResponse;
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

    public LiveData<List<Restaurant>> getRequestedRestaurantListMutableLiveData() {
        return requestedRestaurantsListMutableLiveData;
    }

    public LiveData<ErrorCode> getErrorCode() {
        return errorCode;
    }

    //Get nearby restaurants with google places API
    public void getNearbyPlaces(Location location) {

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


    }

    //Get restaurants matching with the input
    public void getRequestedRestaurants(String input, LatLng latLng) {
        String language = Locale.getDefault().getLanguage();
        String locationStringQuery = latLng.latitude + "," + latLng.longitude;

        //Executor to execute the following code in the same thread (easier for tests)
        executor.execute(() -> {
            Call<AutocompleteResponse> call =
                    placesApi.getRequestedPlaces(apiKey, language, locationStringQuery, radiusQuery, input);
            try {
                Response<AutocompleteResponse> response = call.execute();
                if (response.isSuccessful()) {
                    List<Restaurant> requestedRestaurantList = getRequestedRestaurantList(response.body().predictions);
                    requestedRestaurantsListMutableLiveData.postValue(requestedRestaurantList);
                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);
                }

            } catch (IOException e) {
                errorCode.postValue(ErrorCode.CONNECTION_ERROR);
            }

        });
    }

    //Complete restaurants informations on the list view
    public void updateRestaurantDetails(String restaurantId) {
        List<Restaurant> restaurantList = restaurantListMutableLiveData.getValue();
        //Executor to execute the following code in the same thread (easier for tests)
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

    //Complete requested restaurants informations
    public void updateRequestedRestaurantDetails(String restaurantId) {
        //Executor to execute the following code in the same thread (easier for tests)
        List<Restaurant> restaurantList = requestedRestaurantsListMutableLiveData.getValue();
        executor.execute(() -> {
            Call<DetailsPlaceResponse> call =
                    placesApi.getDetailsById(apiKey, restaurantId);
            try {
                Response<DetailsPlaceResponse> response = call.execute();
                if (response.isSuccessful()) {
                    if (response.body() == null || restaurantList == null)
                        return;
                    for (Restaurant restaurant : restaurantList) {
                        if (restaurant.getId().equals(restaurantId)) {
                            {
                                setRestaurantInfos(response.body().result, restaurant);
                                Log.d(TAG, restaurant.toString());
                            }
                        }
                    }
                    requestedRestaurantsListMutableLiveData.postValue(restaurantList);

                } else {
                    errorCode.postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);
                }

            } catch (IOException e) {
                errorCode.postValue(ErrorCode.CONNECTION_ERROR);
            }
        });
    }

    //Complete restaurants informations on the details view
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


    //Create and return a restaurant list with the API response
    public static List<Restaurant> getRestaurantList(List<RestaurantPojo> restaurantPojoList) {
        List<Restaurant> restaurantList = new ArrayList<>();
        for (RestaurantPojo restaurantPojo : restaurantPojoList) {
            Restaurant restaurant = createRestaurant(restaurantPojo);
            restaurantList.add(restaurant);
        }
        return restaurantList;
    }

    //Create and return a restaurant list with the autocomplete API response
    public static List<Restaurant> getRequestedRestaurantList(List<PredictionPojo> predictionPojoList) {
        List<Restaurant> restaurantList = new ArrayList<>();
        for (PredictionPojo predictionPojo : predictionPojoList) {
            if (predictionPojo.types.contains("restaurant")) {
                Restaurant restaurant = createRequestedRestaurant(predictionPojo);
                restaurantList.add(restaurant);
            }

        }
        return restaurantList;
    }


    //Build our restaurant object from nearby places API response
    private static Restaurant createRestaurant(RestaurantPojo restaurantPojo) {
        return new Restaurant(restaurantPojo.placeId, restaurantPojo.name, restaurantPojo.vicinity, restaurantPojo.geometry.location.lat, restaurantPojo.geometry.location.lng);
    }

    //Build our restaurant object from autocomplete API reponse
    static private Restaurant createRequestedRestaurant(PredictionPojo predictionPojo) {
        return new Restaurant(predictionPojo.placeId, null, null, 0.0, 0.0);
    }

    //Updating our restaurant with details from details places API
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

