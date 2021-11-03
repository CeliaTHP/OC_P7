package com.openclassrooms.oc_p7.services.utils;

import com.google.android.libraries.places.api.model.Place;

public class OnQueryEvent {

    private String query;
    private Place requestedPlace;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Place getRequestedPlace() {
        return requestedPlace;
    }

    public void setRequestedPlace(Place requestedPlace) {
        this.requestedPlace = requestedPlace;
    }
}
