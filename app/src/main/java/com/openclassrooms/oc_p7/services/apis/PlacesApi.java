package com.openclassrooms.oc_p7.services.apis;

import com.openclassrooms.oc_p7.models.pojo_models.responses.AutocompleteResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.NearbyPlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {

    @GET("place/nearbysearch/json")
    Call<NearbyPlaceResponse> getNearbyPlaces(
            @Query("key") String googleApiKey,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String type
    );

    @GET("place/details/json")
    Call<DetailsPlaceResponse> getDetailsById(
            @Query("key") String googleApiKey,
            @Query("place_id") String placeId
    );

    @GET("place/autocomplete/json")
    Call<AutocompleteResponse> getRequestedPlaces(
            @Query("key") String googleApiKey,
            @Query("language") String language,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("input") String input
    );


}
