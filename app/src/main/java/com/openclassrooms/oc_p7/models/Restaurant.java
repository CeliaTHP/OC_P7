package com.openclassrooms.oc_p7.models;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    private String id;
    private String name;
    private String address;
    private double lat;
    private double lng;
    private String type;
    private String phone;
    private String website;
    private List<String> photoReferences;

    private double distance;
    private double rating;

    private List<String> openingHours;
    private List<Workmate> attendees;

    private Boolean isChosen;
    private Boolean isLiked;

    public Restaurant(String id, String name, String address, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        isChosen = false;
        isLiked = false;
    }

    public String toString() {
        return id + " " + name + " " + attendees + " " + address + " " + distance + " " + rating + " " + isChosen + " " + isLiked + " " + openingHours;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public List<String> getPhotoReferences() {
        return photoReferences;
    }

    public void setPhotoReference(List<String> photoReferences) {
        this.photoReferences = photoReferences;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<String> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Workmate> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Workmate> attendees) {
        this.attendees = attendees;
    }

    public Boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Boolean chosen) {
        isChosen = chosen;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean liked) {
        isLiked = liked;
    }
}

