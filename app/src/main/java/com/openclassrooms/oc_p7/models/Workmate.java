package com.openclassrooms.oc_p7.models;

public class Workmate {

    private String uid;
    private String name;
    private String email;
    private String picUrl;
    private String restaurantId;
    private String restaurantName;


    public Workmate(String uid, String name, String email, String picUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;
        Workmate workmateToCompare = (Workmate) o;

        return this.getUid().equals(workmateToCompare.getUid())
                && this.getName().equals(workmateToCompare.getName())
                && this.getEmail().equals(workmateToCompare.getEmail())
                && this.getPicUrl().equals(workmateToCompare.getPicUrl());

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


}
