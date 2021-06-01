package com.openclassrooms.oc_p7.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Restaurant {

    private int restaurantId;
    private String name;
    private String text;
    private String url;
    private String address;
    private LatLng latLng;
    private String type;
    private String distance;
    private Float rate;
    private String phone;
    private String website;
    private String isFavorite;
    private String isChosen;
    private List<User> attendees;

    public Restaurant(String name) {
        this.name = name;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
