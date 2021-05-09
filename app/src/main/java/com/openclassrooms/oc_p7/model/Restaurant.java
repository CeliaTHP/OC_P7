package com.openclassrooms.oc_p7.model;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Restaurant {

    private ImageView picture;
    private String name;
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

    public Restaurant(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(String isChosen) {
        this.isChosen = isChosen;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }
}
