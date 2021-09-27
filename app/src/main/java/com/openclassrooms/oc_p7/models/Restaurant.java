package com.openclassrooms.oc_p7.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    private Boolean hasDetails = false;
    private Boolean hasWorkmates = false;

    public Restaurant(String id, String name, String address, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        isChosen = false;
        isLiked = false;
    }

    public Boolean getHasWorkmates() {
        return hasWorkmates;
    }

    public void setHasWorkmates(Boolean hasWorkmates) {
        this.hasWorkmates = hasWorkmates;
    }

    public void setPhotoReferences(List<String> photoReferences) {
        this.photoReferences = photoReferences;
    }

    public Boolean getChosen() {
        return isChosen;
    }

    public void setChosen(Boolean chosen) {
        isChosen = chosen;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Boolean getHasDetails() {
        return hasDetails;
    }

    public void setHasDetails(Boolean hasDetails) {
        this.hasDetails = hasDetails;
    }

    public String toString() {
        return hasWorkmates + " " + hasDetails + " " + id + " " + name + " " + attendees + " " + address + " " + distance + " " + rating + " " + isChosen + " " + isLiked + " " + openingHours + " " + photoReferences;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Double.compare(that.lat, lat) == 0 &&
                Double.compare(that.lng, lng) == 0 &&
                Double.compare(that.distance, distance) == 0 &&
                Double.compare(that.rating, rating) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(type, that.type) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(website, that.website) &&
                Objects.equals(photoReferences, that.photoReferences) &&
                Objects.equals(openingHours, that.openingHours) &&
                Objects.equals(attendees, that.attendees) &&
                Objects.equals(isChosen, that.isChosen) &&
                Objects.equals(isLiked, that.isLiked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, lat, lng, type, phone, website, photoReferences, distance, rating, openingHours, attendees, isChosen, isLiked);
    }
}

