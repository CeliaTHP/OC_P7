package com.openclassrooms.oc_p7.model;

import com.openclassrooms.oc_p7.model.pojo_models.general.Restaurant;

public class Workmate {

    private String name;
    private String email;
    private String picUrl;
    private Restaurant restaurant;

    public Workmate(String name, String email, String picUrl) {
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
