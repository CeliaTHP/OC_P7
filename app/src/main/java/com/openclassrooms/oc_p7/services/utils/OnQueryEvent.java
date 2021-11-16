package com.openclassrooms.oc_p7.services.utils;

import com.google.android.libraries.places.api.model.Place;

public class OnQueryEvent {

    private String queryForRestaurants = null;
    private String queryForWorkmates = null;
    private Place requestedPlace = null;
    private String queryForMap = null;


    public String getQueryForRestaurants() {
        return queryForRestaurants;
    }

    public void setQueryForRestaurants(String query) {
        this.queryForRestaurants = query;
    }


}
