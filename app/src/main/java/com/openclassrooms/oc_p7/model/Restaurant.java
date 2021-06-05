package com.openclassrooms.oc_p7.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurant")
public class Restaurant {

    @PrimaryKey
    @NonNull
    private String placeId;

    @ColumnInfo(name = "name")
    private String name;
    //MUST BE CONVERTED
//   private List<String> types;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "rating")
    private double rating;

    @ColumnInfo(name = "is_open_now")
    private boolean isOpenNow;
    // private List<Photo> photos;
    @ColumnInfo(name = "distance")
    private long distance;
    @ColumnInfo(name = "is_chosen")
    private Boolean isChosen;
    @ColumnInfo(name = "number")
    private String number;
    @ColumnInfo(name = "website_url")

    private String websiteUrl;
    // private List<Workmate> attendees;

    public Restaurant(String placeId, String name,/* List<String> types,*/ double latitude, double longitude, double rating, Boolean openNow, Boolean isChosen) {
        this.placeId = placeId;
        this.name = name;
        //this.types = types;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        //this.openNow = openNow;
        //this.photos = photos;
        this.isChosen = isChosen;
        // this.attendees = attendees;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
/*
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

 */

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public boolean isOpenNow() {
        return isOpenNow;
    }

    public void setOpenNow(boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

    /*
        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

*/
    public long getDistance() {
        return distance;
    }


    public void setDistance(long distance) {
        this.distance = distance;
    }

    public Boolean getChosen() {
        return isChosen;
    }

    public void setChosen(Boolean chosen) {
        isChosen = chosen;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
/*
    public List<Workmate> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Workmate> attendees) {
        this.attendees = attendees;
    }

 */
}
