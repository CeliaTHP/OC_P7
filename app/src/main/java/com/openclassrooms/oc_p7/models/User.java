package com.openclassrooms.oc_p7.models;

import java.util.List;

public class User {

    private String uid;
    private String name;
    private String email;
    private String picUrl;
    private String restaurantId;
    private String restaurantName;
    private String restaurantType;
    private List<Workmate> workmates;

    public User(String uid, String name, String email, String picUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public List<Workmate> getWorkmates() {
        return workmates;
    }

    public void setWorkmates(List<Workmate> workmates) {
        this.workmates = workmates;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(String restaurantType) {
        this.restaurantType = restaurantType;
    }
}
