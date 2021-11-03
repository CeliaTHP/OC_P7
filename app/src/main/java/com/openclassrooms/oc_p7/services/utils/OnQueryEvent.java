package com.openclassrooms.oc_p7.services.utils;

import com.google.android.libraries.places.api.model.Place;

public class OnQueryEvent {

    private String queryForRestaurants;
    private String queryForWorkmates;
    private Place requestedPlace;


    public Place getRequestedPlace() {
        return requestedPlace;
    }

    public void setRequestedPlace(Place requestedPlace) {
        this.requestedPlace = requestedPlace;
    }

    public String getQueryForRestaurants() {
        return queryForRestaurants;
    }

    public void setQueryForRestaurants(String query) {
        this.queryForRestaurants = query;
    }

    public String getQueryForWorkmates() {
        return queryForWorkmates;
    }

    public void setQueryForWorkmates(String query) {
        this.queryForWorkmates = query;
    }


}
