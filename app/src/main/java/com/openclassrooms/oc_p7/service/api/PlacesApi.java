package com.openclassrooms.oc_p7.service.api;

import com.openclassrooms.oc_p7.model.pojo_models.NearbyPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("place/nearbysearch/json")
    Call<NearbyPlaceResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("key") String googleApiKey,
            @Query("radius") String radius,
            @Query("type") String type
    );


}
