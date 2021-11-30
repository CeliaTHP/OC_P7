package com.openclassrooms.oc_p7.utils;

import com.openclassrooms.oc_p7.models.pojo_models.prediction_pojo.PredictionPojo;
import com.openclassrooms.oc_p7.models.pojo_models.responses.AutocompleteResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.Geometry;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.Location;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.RestaurantPojo;

import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class APIUtils {

    @SuppressWarnings("unchecked")
    public static <T> Call<T> getCallMock(Boolean isSuccessFull, T response) throws IOException {
        Call<T> callMock = Mockito.mock(Call.class);
        Response<T> responseMock = Mockito.mock(Response.class);
        Mockito.when(callMock.execute()).thenReturn(responseMock);
        Mockito.when(responseMock.isSuccessful()).thenReturn(isSuccessFull);
        Mockito.when(responseMock.body()).thenReturn(response);
        return callMock;
    }

    public static NearbyPlaceResponse getNearbyPlaceResponse() {
        NearbyPlaceResponse nearByPlaceResponse = new NearbyPlaceResponse();
        nearByPlaceResponse.restaurantPojos = getRestaurantPojoList();
        return nearByPlaceResponse;
    }

    public static AutocompleteResponse getRequestedPlaceResponse() {
        AutocompleteResponse autocompleteResponse = new AutocompleteResponse();
        autocompleteResponse.predictions = getPredictionPojoList();
        return autocompleteResponse;
    }

    public static DetailsPlaceResponse getDetailsPlaceResponse() {
        DetailsPlaceResponse detailsPlaceResponse = new DetailsPlaceResponse();
        detailsPlaceResponse.result = getRestaurantPojoList().get(0);
        return detailsPlaceResponse;
    }

    public static List<RestaurantPojo> getRestaurantPojoList() {
        List<RestaurantPojo> restaurantPojoList = new ArrayList<>();
        RestaurantPojo restaurantPojo = new RestaurantPojo();
        restaurantPojo.placeId = "place_id";
        restaurantPojo.name = "name";
        restaurantPojo.vicinity = "vicinity";
        Location location = new Location();
        location.lat = 1.0;
        location.lng = 1.0;
        Geometry geometry = new Geometry();
        geometry.location = location;
        restaurantPojo.geometry = geometry;
        restaurantPojoList.add(restaurantPojo);
        return restaurantPojoList;
    }

    public static List<PredictionPojo> getPredictionPojoList() {
        List<PredictionPojo> predictionPojoList = new ArrayList<>();
        PredictionPojo predictionPojo = new PredictionPojo();
        predictionPojo.placeId = "place_id";
        predictionPojo.description = "name";

        List<String> typeList = new ArrayList<>();
        typeList.add("restaurant");
        predictionPojo.types = typeList;
        predictionPojoList.add(predictionPojo);
        return predictionPojoList;
    }

}