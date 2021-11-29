package com.openclassrooms.oc_p7.services.utils;

public class OnRestaurantQueryEvent {

    //Notifies when a query happens from restaurant list with event bus

    private String queryForRestaurant = null;

    public String getQueryForRestaurant() {
        return queryForRestaurant;
    }

    public void setQueryForRestaurant(String queryForRestaurant) {
        this.queryForRestaurant = queryForRestaurant;
    }


}
